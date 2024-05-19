package org.invexion.negotiation.service;

import io.minio.*;
import io.minio.messages.Item;
import lombok.extern.slf4j.Slf4j;
import org.invexion.utils.CustomMultipartFile;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.invexion.negotiation.model.dto.FileDto;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

@Slf4j
@Service
public class MinioService {

    /*
        Mandatory field for fileDTO is only Multipart file.
     */

    @Autowired
    private MinioClient minioClient;

    @Value("${minio.bucket.name}")
    private String bucketName;


    public List<FileDto> getListObjects() {
        List<FileDto> objects = new ArrayList<>();
        try {
            Iterable<Result<Item>> result = minioClient.listObjects(ListObjectsArgs.builder()
                    .bucket(bucketName)
                    .recursive(true)
                    .build());
            for (Result<Item> item : result) {
                FileDto fileDto = new FileDto();
                fileDto.setFilename(item.get().objectName());
                fileDto.setSize(item.get().size());
                fileDto.setUrl(getPreSignedUrl(item.get().objectName()));
                objects.add(fileDto);
            }
            return objects;
        } catch (Exception e) {

            e.printStackTrace();
        }

        return objects;
    }

    private String getPreSignedUrl(String filename) {
        return "http://localhost:8080/file/".concat(filename);
    }


    public FileDto uploadFile(MultipartFile multipartFile) {
        String fileId = String.valueOf(UUID.randomUUID());
        try {
            minioClient.putObject(PutObjectArgs.builder()
                    .bucket(bucketName)
                    .object(fileId)
                    .stream(multipartFile.getInputStream(), multipartFile.getSize(), -1)
                    .build());

            FileDto fileDto = new FileDto();
            fileDto.setSize(multipartFile.getSize());
            fileDto.setUrl(getPreSignedUrl(fileId));
            fileDto.setFilename(multipartFile.getOriginalFilename());
            fileDto.setFile(multipartFile);

            fileDto.setS3UUID(fileId);

            return fileDto;
        } catch (Exception e) {
            System.out.println("Happened error when get uploading object from minio");
            e.printStackTrace();
        }

        return null;
    }

    public InputStream getObject(String filename) {
        InputStream stream;
        try {
            stream = minioClient.getObject(GetObjectArgs.builder()
                    .bucket(bucketName)
                    .object(filename)
                    .build());
        } catch (Exception e) {
            System.out.println("Happened error when get list objects from minio");
            return null;
        }

        return stream;
    }

    public List<FileDto> uploadZip(MultipartFile file) throws IOException {
        if (!file.getOriginalFilename().endsWith(".zip")) {
            throw new IllegalArgumentException("Only .zip files are supported.");
        }

        List<FileDto> uploadedFiles = new ArrayList<>();

        try (InputStream inputStream = file.getInputStream()) {
            try (ZipInputStream zipInputStream = new ZipInputStream(inputStream)) {
                ZipEntry entry;
                while ((entry = zipInputStream.getNextEntry()) != null) {
                    if (!entry.isDirectory()) {
                        String fileName = entry.getName();
                        long fileSize = entry.getSize();
                        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
                        byte[] buffer = new byte[1024];
                        int length;
                        while ((length = zipInputStream.read(buffer)) > 0) {
                            outputStream.write(buffer, 0, length);
                        }
                        outputStream.close();
                        byte[] fileBytes = outputStream.toByteArray();
                        ByteArrayInputStream fileInputStream = new ByteArrayInputStream(fileBytes);
                        FileDto uploadedFile = uploadFile(CustomMultipartFile.create(fileName,fileName,null,fileBytes));
                        uploadedFiles.add(uploadedFile);
                    }
                    zipInputStream.closeEntry();
                }
            }
            return uploadedFiles;
        } catch (Exception e) {
            System.out.println("Error occurred during ZIP file upload to MinIO");
            e.printStackTrace();
            throw new IOException("Failed to upload ZIP file", e);
        }
    }
}
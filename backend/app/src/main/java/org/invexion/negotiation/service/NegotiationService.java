package org.invexion.negotiation.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.invexion.analyzefacade.FacadeNegotiationAnalysis;
import org.invexion.analyzefacade.NegotiationAnalyzer;
import org.invexion.negotiation.model.FileType;
import org.invexion.negotiation.model.Negotiation;
import org.invexion.negotiation.model.repository.NegotiationRepository;
import org.invexion.negotiation.model.dto.FileDto;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class NegotiationService {

    private final NegotiationRepository negotiationRepository;
//    private final NegotiationAnalyzer negotiationAnalyzer;
    private final AnalyzerService analyzerService;
    private final MinioService minioService;

    public List<Negotiation> getAllNegotiations() {
        return negotiationRepository.findAll();
    }

//    public List<NegotiationDto> getAllNegotiations() {
//        return negotiationRepository.findAll().parallelStream()
//                .map(negotiation -> new NegotiationDto(
//                        negotiation.getId(),
//                        negotiation.getMetadata(),
//                        negotiation.getIsAnalyzed())
//                )
//                .toList();
//    }

    public Long getAllNegotiationsCount() {
        return negotiationRepository.count();
    }

    public Long getViolatedNegotiationsCount() {
        return negotiationRepository.countViolated();
    }

    public Long getNonViolatedNegotiationsCount() {
        return negotiationRepository.countNonViolated();
    }

    public void deleteNegotiationById(Long negotiationId) {
        negotiationRepository.deleteById(negotiationId);
    }

    private FileType getFileType(String filename){
        if(filename==null){
            return null;
        }
        String ext = filename.substring(filename.lastIndexOf("."));
        if(ext.equals(".txt")){
            return FileType.TEXT;
        }else{
            return FileType.AUDIO;
        }
    }

    public void saveAndAnalyzeNegotiation(FileDto fileDto){
        String fileName = fileDto.getFile().getOriginalFilename();
        Negotiation negotiation = Negotiation.builder()
                .fileStorageId(fileDto.getS3UUID())
                .fileName(fileName)
                .fileType(getFileType(fileName))
                .isAnalyzed(false)
                .build();

        negotiationRepository.save(negotiation);

        analyzerService.analyzeSoundNegotiation(fileDto.getS3UUID(),fileDto.getFilename());
    }

    public Object getListObjects() {
        return minioService.getListObjects();
    }

    public FileDto uploadFile(MultipartFile file) {
        FileDto fileDto = minioService.uploadFile(file);

        saveAndAnalyzeNegotiation(fileDto);

        return fileDto;
    }

    public List<FileDto> uploadZip(MultipartFile file) throws IOException {
        List<FileDto> fileDtos = minioService.uploadZip(file);
        fileDtos.parallelStream().forEach(this::saveAndAnalyzeNegotiation);
        return fileDtos;
    }


    public InputStream getObject(String filename) {
        return minioService.getObject(filename);
    }

    public List<Negotiation> findAllById(List<Long> listIds) {
        return negotiationRepository.findAllById(listIds);
    }
}

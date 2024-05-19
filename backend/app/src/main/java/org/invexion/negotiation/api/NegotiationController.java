package org.invexion.negotiation.api;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
//import org.invexion.backend.api.dto.NegotiationDto;
import org.apache.commons.io.IOUtils;
import org.invexion.exception.StorageFileNotFoundException;
import org.invexion.metric.dto.CalculateMetricsDto;
import org.invexion.negotiation.model.Negotiation;
import org.invexion.negotiation.model.NegotiationAnalysis;
import org.invexion.negotiation.model.repository.NegotiationAnalysisRepository;
import org.invexion.negotiation.service.MinioService;
import org.invexion.negotiation.service.NegotiationService;
import org.invexion.negotiation.model.dto.FileDto;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.web.servlet.HandlerMapping.BEST_MATCHING_PATTERN_ATTRIBUTE;

@RestController
@RequestMapping("/api/negotiations")
@RequiredArgsConstructor
@CrossOrigin
public class NegotiationController {

    private final NegotiationService negotiationService;
    private final NegotiationAnalysisRepository negotiationAnalysisRepository;


    @GetMapping(value="/files")
    public ResponseEntity<Object> getFiles() {
        return ResponseEntity.ok(negotiationService.getListObjects());
    }

    //entrypoint to App
    @PostMapping(value = "/file/upload")
    public ResponseEntity<FileDto> upload(@RequestParam("file") MultipartFile file) {
        return ResponseEntity.ok().body(negotiationService.uploadFile(file));
    }

    @PostMapping(value = "/files/upload")
    public ResponseEntity<List<FileDto>> uploadFiles(@RequestParam("files") MultipartFile[] files) {
        List<FileDto> response = Arrays.asList(files).parallelStream().map(negotiationService::uploadFile).toList();
        return ResponseEntity.ok().body(response);
    }

    @PostMapping(value="/zip/upload")
    public ResponseEntity<List<FileDto>> uploadZip(@RequestParam("file") MultipartFile file) throws IOException {
        return ResponseEntity.ok().body(negotiationService.uploadZip(file));
    }

    @GetMapping(value = "files/**")
    public ResponseEntity<Object> getFile(HttpServletRequest request) throws IOException {
        String pattern = (String) request.getAttribute(BEST_MATCHING_PATTERN_ATTRIBUTE);
        String filename = new AntPathMatcher().extractPathWithinPattern(pattern, request.getServletPath());
        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .body(IOUtils.toByteArray(negotiationService.getObject(filename)));
    }


    @GetMapping
    public ResponseEntity<List<Negotiation>> getAllNegotiations(){
        return ResponseEntity.ok(negotiationService.getAllNegotiations());
    }


    @ExceptionHandler(StorageFileNotFoundException.class)
    public ResponseEntity<?> handleStorageFileNotFound(StorageFileNotFoundException exc) {
        return ResponseEntity.notFound().build();
    }

    @ExceptionHandler(IOException.class)
    public ResponseEntity<?> handleIOException(IOException exc) {
        exc.printStackTrace();
        return ResponseEntity.badRequest().build();
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<?> handleIllegalArgument(IllegalArgumentException exc) {
        return ResponseEntity.badRequest().body(exc.getMessage());
    }


}
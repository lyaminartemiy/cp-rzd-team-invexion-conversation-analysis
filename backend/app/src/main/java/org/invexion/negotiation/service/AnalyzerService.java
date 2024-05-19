package org.invexion.negotiation.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.invexion.analyzefacade.FacadeNegotiationAnalysis;
import org.invexion.analyzefacade.NegotiationAnalyzer;
import org.invexion.negotiation.model.Negotiation;
import org.invexion.negotiation.model.NegotiationAnalysis;
import org.invexion.negotiation.model.repository.NegotiationAnalysisRepository;
import org.invexion.negotiation.model.ViolationInterval;
import org.invexion.negotiation.model.repository.ViolationIntervalRepository;
import org.invexion.negotiation.model.repository.NegotiationRepository;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

@Service
@Slf4j
@RequiredArgsConstructor
public class AnalyzerService {
    private final NegotiationAnalysisRepository negotiationAnalysisRepository;
    private final ViolationIntervalRepository violationIntervalRepository;
    private final NegotiationRepository negotiationRepository;

    private final NegotiationAnalyzer negotiationAnalyzer;
    private final MinioService minioService;

    private record UuidFilenameDto(String uuid, String filename){
    }
    private Queue<UuidFilenameDto> messageQueue = new ConcurrentLinkedQueue<>();

    public void analyzeSoundNegotiation(String s3UUID, String filename){
        log.info("Adding sound negotiation with UUID to the queue: "+ s3UUID);
        messageQueue.offer(new UuidFilenameDto(s3UUID,filename));
    }

    public void processQueue() {
        while (true) {
            if (!messageQueue.isEmpty()) {
                UuidFilenameDto entry = messageQueue.poll();
                String s3UUID = entry.uuid;
                String extention = entry.filename.substring(entry.filename.lastIndexOf("."));

                String text;
                if(!extention.equals(".txt")){
                    text=negotiationAnalyzer.analyzeSoundByS3UUID(
                            s3UUID
                    ).decodedText();
                }else{
                    InputStream inputStream = minioService.getObject(s3UUID);

                    StringBuilder textBuilder = new StringBuilder();
                    try (BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream))) {
                        String line;
                        while ((line = reader.readLine()) != null) {
                            textBuilder.append(line).append("\n");
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    log.info("analyzing .txt containing: "+ textBuilder);

                    text= textBuilder.toString();
                }

                log.info("Processing message with UUID: " + s3UUID);

                FacadeNegotiationAnalysis response = negotiationAnalyzer.analyzeText(
                        text
                );

                log.info("text alalyzation response: "+ response.toString());

                Negotiation parentNegotiation = negotiationRepository.findByS3UUID(s3UUID);
                parentNegotiation.setIsAnalyzed(true);
                negotiationRepository.save(parentNegotiation);

                NegotiationAnalysis analysis = NegotiationAnalysis.builder()
                        .negotiationText(response.negotiationText())
                        .negotiation(parentNegotiation)
                        .isViolated(!response.violatedPhrases().isEmpty())
                        .build();
                negotiationAnalysisRepository.save(analysis);

                if (!response.violatedPhrases().isEmpty()) {
                    violationIntervalRepository.saveAll(response.violatedPhrases().parallelStream().map(phrase -> ViolationInterval.builder()
                            .beginIndex(phrase.beginIndex())
                            .endIndex(phrase.endIndex())
                            .negotiationAnalysis(analysis)
                            .violatedText(phrase.violatedText())
                            .violatedRegulation(phrase.violatedRegulation())
                            .build()
                    ).toList());
                }
            } else {
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    log.error("Error while sleeping", e);
                    Thread.currentThread().interrupt();
                }
            }
        }
    }


    @Scheduled(fixedRate = 1000) // Executes every 1000 milliseconds (1 second)
    public void scheduledProcessQueue() {
        processQueue();
    }


}
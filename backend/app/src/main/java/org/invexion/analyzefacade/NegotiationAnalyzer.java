package org.invexion.analyzefacade;

import org.springframework.cloud.openfeign  .FeignClient;
import org.springframework.web.bind.annotation.*;

@FeignClient(name = "negotiationAnalyzer", url = "http://hackaton-ml:8000")
public interface NegotiationAnalyzer {
    @GetMapping("/analyze/segment/")
    FacadeNegotiationAnalysis analyzeText(
            @RequestParam("text") String text
    );

    @GetMapping("/speech2text/")
    SpeechRecognitionResult analyzeSoundByS3UUID(
            @RequestParam("uuid") String uuid
    );
}

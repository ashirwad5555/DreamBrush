package com.example.demo.Controller;

import com.example.demo.StabilityAiService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@CrossOrigin(origins = "*")  // For development, allow requests from any origin
public class ImageGenerationController {

    private final StabilityAiService stabilityAiService;

    public ImageGenerationController(StabilityAiService stabilityAiService) {
        this.stabilityAiService = stabilityAiService;
    }

    @PostMapping("/api/generate-image")
    public ResponseEntity<String> generateImage(@RequestBody ImagePromptRequest request) {
        String response = stabilityAiService.generateImage(request.getPrompt());
        return ResponseEntity.ok(response);
    }

    // Request model
    public static class ImagePromptRequest {
        private String prompt;

        public String getPrompt() {
            return prompt;
        }

        public void setPrompt(String prompt) {
            this.prompt = prompt;
        }
    }
}
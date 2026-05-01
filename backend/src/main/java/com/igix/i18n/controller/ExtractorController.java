package com.igix.i18n.controller;

import com.igix.i18n.dto.ExtractRequest;
import com.igix.i18n.extractor.ResourceExtractor;
import com.igix.i18n.model.ExtractedFile;
import com.igix.i18n.service.ExtractorService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class ExtractorController {

    private final ExtractorService extractorService;

    @PostMapping("/extract")
    public ResponseEntity<Map<String, Object>> extract(@Valid @RequestBody ExtractRequest request) {
        log.info("Received extraction request: {}", request);

        List<ExtractedFile> files = extractorService.extract(request);

        Map<String, Object> response = new HashMap<>();
        response.put("success", true);
        response.put("count", files.size());
        response.put("files", files);

        return ResponseEntity.ok(response);
    }

    @GetMapping("/extractors")
    public ResponseEntity<List<Map<String, Object>>> getAvailableExtractors() {
        List<ResourceExtractor> extractors = extractorService.getAvailableExtractors();

        List<Map<String, Object>> extractorInfo = extractors.stream()
                .map(extractor -> {
                    Map<String, Object> info = new HashMap<>();
                    info.put("name", extractor.getName());
                    info.put("description", extractor.getDescription());
                    info.put("order", extractor.getOrder());
                    return info;
                })
                .toList();

        return ResponseEntity.ok(extractorInfo);
    }

    @GetMapping("/health")
    public ResponseEntity<Map<String, String>> health() {
        Map<String, String> response = new HashMap<>();
        response.put("status", "UP");
        response.put("service", "I18n Extractor");
        return ResponseEntity.ok(response);
    }
}

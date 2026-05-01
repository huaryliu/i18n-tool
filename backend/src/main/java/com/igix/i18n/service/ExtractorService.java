package com.abc.i18n.service;

import com.abc.i18n.dto.ExtractRequest;
import com.abc.i18n.extractor.ResourceExtractor;
import com.abc.i18n.model.ExtractedFile;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class ExtractorService {

    private final List<ResourceExtractor> extractors;

    public List<ExtractedFile> extract(ExtractRequest request) {
        // 如果languages为空，默认提取所有支持的语言
        Set<String> languages = request.getLanguages();
        if (languages == null || languages.isEmpty()) {
            languages = new HashSet<>();
            languages.add("zh-CHS");
            languages.add("en");
            languages.add("zh-CHT");
            log.info("No languages specified, extracting all supported languages: {}", languages);
        }

        Set<String> finalLanguages = languages;
        log.info("Starting extraction from root path: {} with languages: {}",
                request.getRootPath(), finalLanguages);

        return extractors.stream()
                .filter(extractor -> isEnabled(extractor, request.getEnabledExtractors()))
                .sorted(Comparator.comparingInt(ResourceExtractor::getOrder))
                .flatMap(extractor -> {
                    log.info("Running extractor: {}", extractor.getName());
                    return extractor.extract(request.getRootPath(), finalLanguages).stream();
                })
                .collect(Collectors.toList());
    }

    private boolean isEnabled(ResourceExtractor extractor, List<String> enabledExtractors) {
        if (enabledExtractors == null || enabledExtractors.isEmpty()) {
            return true;
        }
        return enabledExtractors.contains(extractor.getName());
    }

    public List<ResourceExtractor> getAvailableExtractors() {
        return extractors.stream()
                .sorted(Comparator.comparingInt(ResourceExtractor::getOrder))
                .collect(Collectors.toList());
    }
}

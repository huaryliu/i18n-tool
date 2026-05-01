package com.igix.i18n.extractor.impl;

import com.igix.i18n.extractor.ResourceExtractor;
import com.igix.i18n.model.ExtractedFile;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Slf4j
@Component
public class FrontendJsonExtractor implements ResourceExtractor {

    private static final String WEB_DIR = "web";

    @Override
    public List<ExtractedFile> extract(String rootPath, Set<String> languages) {
        List<ExtractedFile> files = new ArrayList<>();

        log.info("Starting frontend JSON extraction from root path: {}", rootPath);
        log.info("Looking for languages: {}", languages);

        Path webPath = Paths.get(rootPath, WEB_DIR);
        log.info("Resolved web directory path: {}", webPath);

        if (!Files.exists(webPath)) {
            log.warn("Web directory not found: {}", webPath);
            return files;
        }

        try {
            extractFromDirectory(webPath, languages, files);
        } catch (IOException e) {
            log.error("Error extracting frontend JSON files", e);
        }

        log.info("Frontend JSON extraction completed. Total files found: {}", files.size());
        return files;
    }

    private void extractFromDirectory(Path basePath, Set<String> languages, List<ExtractedFile> files) throws IOException {
        log.info("Scanning web directory for frontend JSON files: {}", basePath);

        // 首先列出所有JSON文件进行调试
        List<Path> allJsonFiles = Files.walk(basePath)
                .filter(Files::isRegularFile)
                .filter(p -> p.toString().endsWith(".json"))
                .toList();

        log.info("Found {} JSON files in web directory", allJsonFiles.size());

        // 遍历web目录，查找所有JSON文件（不扫描.properties文件）
        allJsonFiles.forEach(path -> {
            try {
                String relativePath = basePath.relativize(path).toString().replace("\\", "/");
                String fileName = path.getFileName().toString();
                log.info("Processing frontend file: {} (relative path: {})", fileName, relativePath);

                String language = determineLanguage(fileName, languages);
                log.info("Determined language for file {}: {}", fileName, language);

                if (language != null) {
                    String content = Files.readString(path);
                    // 添加 web/ 前缀以显示完整的相对路径
                    String fullPath = WEB_DIR + "/" + relativePath;
                    files.add(ExtractedFile.builder()
                            .path(fullPath)
                            .originalPath(path.toString())
                            .content(content)
                            .language(language)
                            .fileType("json")
                            .size(Files.size(path))
                            .isDirectory(false)
                            .build());
                    log.info("Added frontend file: {} for language: {}", fullPath, language);
                } else {
                    log.warn("Skipping file: {} (no language match for: {})", fileName, languages);
                }
            } catch (IOException e) {
                log.error("Error reading file: {}", path, e);
            }
        });

        log.info("Found {} frontend JSON files", files.size());
    }

    private String determineLanguage(String fileName, Set<String> languages) {
        String fileNameLower = fileName.toLowerCase();

        log.debug("Determining language for file: {} (requested languages: {})", fileName, languages);

        // 英文文件 - 支持多种命名格式 (en.json等)
        if (fileNameLower.equals("en.json") || fileNameLower.startsWith("en.")) {
            boolean languageSupported = languages.contains("en");
            log.debug("File {} identified as en, supported: {}", fileName, languageSupported);
            return languageSupported ? "en" : null;
        }

        // 简体中文文件 - 支持多种命名格式 (zh-CHS.json, zh_CHS.json, zh-Hans.json等)
        if (fileNameLower.equals("zh-chs.json") ||
            fileNameLower.equals("zh_chs.json") ||
            fileNameLower.equals("zh-hans.json") ||
            fileNameLower.startsWith("zh-chs.") ||
            fileNameLower.startsWith("zh_chs.") ||
            fileNameLower.startsWith("zh-hans.")) {
            boolean languageSupported = languages.contains("zh-CHS");
            log.debug("File {} identified as zh-CHS, supported: {}", fileName, languageSupported);
            return languageSupported ? "zh-CHS" : null;
        }

        // 繁体中文文件 - 支持多种命名格式 (zh-CHT.json, zh_CHT.json, zh-Hant.json等)
        if (fileNameLower.equals("zh-cht.json") ||
            fileNameLower.equals("zh_cht.json") ||
            fileNameLower.equals("zh-hant.json") ||
            fileNameLower.startsWith("zh-cht.") ||
            fileNameLower.startsWith("zh_cht.") ||
            fileNameLower.startsWith("zh-hant.")) {
            boolean languageSupported = languages.contains("zh-CHT");
            log.debug("File {} identified as zh-CHT, supported: {}", fileName, languageSupported);
            return languageSupported ? "zh-CHT" : null;
        }

        log.debug("File {} could not be matched to any supported language", fileName);
        return null;
    }

    @Override
    public String getName() {
        return "FrontendJsonExtractor";
    }

    @Override
    public String getDescription() {
        return "Extracts frontend .json files from web directory (does not scan .properties files)";
    }

    @Override
    public int getOrder() {
        return 20;
    }
}

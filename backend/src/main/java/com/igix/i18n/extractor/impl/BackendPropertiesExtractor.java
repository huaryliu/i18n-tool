package com.abc.i18n.extractor.impl;

import com.abc.i18n.extractor.ResourceExtractor;
import com.abc.i18n.model.ExtractedFile;
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
public class BackendPropertiesExtractor implements ResourceExtractor {

    private static final String SERVER_DIR = "server";

    @Override
    public List<ExtractedFile> extract(String rootPath, Set<String> languages) {
        List<ExtractedFile> files = new ArrayList<>();

        log.info("Starting backend properties extraction from root path: {}", rootPath);
        log.info("Looking for languages: {}", languages);

        Path serverPath = Paths.get(rootPath, SERVER_DIR);
        log.info("Resolved server directory path: {}", serverPath);

        if (!Files.exists(serverPath)) {
            log.warn("Server directory not found: {}", serverPath);
            return files;
        }

        try {
            extractFromDirectory(serverPath, languages, files);
        } catch (IOException e) {
            log.error("Error extracting backend properties files", e);
        }

        log.info("Backend properties extraction completed. Total files found: {}", files.size());
        return files;
    }

    private void extractFromDirectory(Path basePath, Set<String> languages, List<ExtractedFile> files) throws IOException {
        log.info("Scanning server directory for backend properties files: {}", basePath);

        // 遍历server目录，查找所有.properties文件
        Files.walk(basePath)
                .filter(Files::isRegularFile)
                .filter(p -> p.toString().endsWith(".properties"))
                .filter(p -> !shouldExclude(p))  // 排除特定目录
                .forEach(path -> {
                    try {
                        String relativePath = basePath.relativize(path).toString().replace("\\", "/");
                        String fileName = path.getFileName().toString();
                        log.info("Processing backend file: {} (relative path: {})", fileName, relativePath);

                        String language = determineLanguage(relativePath, languages);
                        log.info("Determined language for file {}: {}", fileName, language);

                        if (language != null) {
                            String content = Files.readString(path);
                            // 添加 server/ 前缀以显示完整的相对路径
                            String fullPath = SERVER_DIR + "/" + relativePath;
                            files.add(ExtractedFile.builder()
                                    .path(fullPath)
                                    .originalPath(path.toString())
                                    .content(content)
                                    .language(language)
                                    .fileType("properties")
                                    .size(Files.size(path))
                                    .isDirectory(false)
                                    .build());
                            log.info("Added backend file: {} for language: {}", fullPath, language);
                        } else {
                            log.warn("Skipping file: {} (no language match for: {})", fileName, languages);
                        }
                    } catch (IOException e) {
                        log.error("Error reading file: {}", path, e);
                    }
                });

        log.info("Found {} backend properties files", files.size());
    }

    private boolean shouldExclude(Path path) {
        String pathString = path.toString().replace("\\", "/");

        // 排除runtime/java目录（JDK目录，扫描无意义）
        if (pathString.contains("/runtime/java/") || pathString.contains("\\runtime\\java\\")) {
            return true;
        }

        // 排除platform/dev目录
        if (pathString.contains("/platform/dev/") || pathString.contains("\\platform\\dev\\")) {
            return true;
        }

        return false;
    }

    private String determineLanguage(String path, Set<String> languages) {
        String pathLower = path.toLowerCase();

        if (pathLower.contains("/en/") || pathLower.contains("\\en\\") ||
                pathLower.contains("/en_") || pathLower.startsWith("en/")) {
            return languages.contains("en") ? "en" : null;
        }

        if (pathLower.contains("/zh-cht/") || pathLower.contains("\\zh-cht\\") ||
                pathLower.contains("/zh_cht/") || pathLower.contains("\\zh_cht\\")) {
            return languages.contains("zh-CHT") ? "zh-CHT" : null;
        }

        if (pathLower.contains("/zh-chs/") || pathLower.contains("\\zh-chs\\")) {
            return languages.contains("zh-CHS") ? "zh-CHS" : null;
        }

        if (languages.contains("zh-CHS")) {
            return "zh-CHS";
        }

        return null;
    }

    @Override
    public String getName() {
        return "BackendPropertiesExtractor";
    }

    @Override
    public String getDescription() {
        return "Extracts backend .properties files from server directory (excludes runtime/java and platform/dev)";
    }

    @Override
    public int getOrder() {
        return 10;
    }
}

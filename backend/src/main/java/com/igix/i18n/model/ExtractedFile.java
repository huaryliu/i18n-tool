package com.igix.i18n.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ExtractedFile {
    private String path;
    private String originalPath;
    private String content;
    private String language;
    private String fileType;
    private long size;
    private boolean isDirectory;
}

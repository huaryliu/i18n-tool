package com.igix.i18n.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Data
public class ExtractRequest {
    @NotBlank(message = "Root path cannot be blank")
    private String rootPath;

    private Set<String> languages = new HashSet<>();

    private List<String> enabledExtractors;
}

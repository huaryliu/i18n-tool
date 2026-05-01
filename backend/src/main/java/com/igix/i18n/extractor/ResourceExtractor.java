package com.igix.i18n.extractor;

import com.igix.i18n.model.ExtractedFile;

import java.util.List;
import java.util.Set;

public interface ResourceExtractor {

    List<ExtractedFile> extract(String rootPath, Set<String> languages);

    String getName();

    String getDescription();

    default int getOrder() {
        return 100;
    }
}

# I18n Extractor Extensions

This directory contains custom extension JARs for the I18n Resource Extractor.

## How to Create Custom Extensions

1. **Create a new Maven project** with the following dependency:

```xml
<dependency>
    <groupId>com.igix</groupId>
    <artifactId>i18n-extractor-api</artifactId>
    <version>1.0.0</version>
</dependency>
```

2. **Implement the ResourceExtractor interface**:

```java
package com.example;

import com.igix.i18n.extractor.ResourceExtractor;
import com.igix.i18n.model.ExtractedFile;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Set;

@Component
public class CustomExtractor implements ResourceExtractor {
    @Override
    public List<ExtractedFile> extract(String rootPath, Set<String> languages) {
        // Your extraction logic here
        return List.of();
    }

    @Override
    public String getName() {
        return "CustomExtractor";
    }

    @Override
    public String getDescription() {
        return "Custom resource extractor for specific requirements";
    }
}
```

3. **Build your extension**:

```bash
mvn clean package
```

4. **Place the JAR file** in this directory (tool/extensions/)

5. **Configure the extension** in `config/application.yaml`:

```yaml
i18n:
  extensions:
    enabled: true
    directory: extensions
    classes:
      - com.example.CustomExtractor
```

## Extension Examples

### Database Extractor
Extract internationalization resources from a database.

### File System Extractor
Extract resources from custom file system locations.

### Remote API Extractor
Extract resources from remote API endpoints.

## Important Notes

- Extensions must be compatible with the same Spring Boot version (3.2.0)
- Ensure your extension JAR includes all required dependencies
- Test your extension before deploying to production
- Use unique package names to avoid conflicts

## Support

For extension development questions, please contact the development team.

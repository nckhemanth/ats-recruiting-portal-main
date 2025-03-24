package cloud.nckhemanth.ats.application;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.Set;
import java.util.UUID;

@Service
public class ResumeStorage {
    private static final Set<String> CONTENT_TYPES = Set.of("application/pdf", "application/msword",
            "application/vnd.openxmlformats-officedocument.wordprocessingml.document");
    private final Path directory;

    public ResumeStorage(@Value("${app.uploads-directory}") String directory) {
        this.directory = Path.of(directory).toAbsolutePath().normalize();
    }

    public String store(MultipartFile file) {
        if (file == null || file.isEmpty()) return null;
        if (file.getSize() > 10L * 1024 * 1024 || !CONTENT_TYPES.contains(file.getContentType())) {
            throw new IllegalArgumentException("Resume must be a PDF or Word document under 10 MB");
        }
        String extension = file.getOriginalFilename() != null && file.getOriginalFilename().contains(".")
                ? file.getOriginalFilename().substring(file.getOriginalFilename().lastIndexOf('.')) : ".bin";
        String name = UUID.randomUUID() + extension.toLowerCase();
        try {
            Files.createDirectories(directory);
            Files.copy(file.getInputStream(), directory.resolve(name), StandardCopyOption.REPLACE_EXISTING);
            return "/uploads/" + name;
        } catch (IOException exception) {
            throw new IllegalStateException("Unable to store resume", exception);
        }
    }
}


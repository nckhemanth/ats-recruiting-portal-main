package cloud.nckhemanth.ats.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.nio.file.Path;

@Configuration
public class WebConfiguration implements WebMvcConfigurer {
    private final Path uploads;

    public WebConfiguration(@Value("${app.uploads-directory}") String uploadsDirectory) {
        this.uploads = Path.of(uploadsDirectory).toAbsolutePath().normalize();
    }

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/uploads/**").addResourceLocations(uploads.toUri().toString());
    }
}


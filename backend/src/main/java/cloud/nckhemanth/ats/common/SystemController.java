package cloud.nckhemanth.ats.common;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.time.Instant;
import java.util.Map;

@RestController
@RequestMapping("/api")
public class SystemController {
    @GetMapping("/health")
    Map<String, Object> health() {
        return Map.of("status", "ok", "service", "ats-recruiting-portal", "time", Instant.now());
    }
}


package cloud.nckhemanth.ats.job;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;

public final class JobDtos {
    private JobDtos() {}

    public record Stage(Long id, String name, int position) {
        public static Stage from(JobStage stage) { return new Stage(stage.getId(), stage.getName(), stage.getPosition()); }
    }

    public record Summary(Long id, String title, String company, String location, String department,
                          String employmentType, JobStatus status, BigDecimal minSalary,
                          BigDecimal maxSalary, Instant createdAt, long applicationsCount) {}

    public record Detail(Long id, String title, String company, String location, String department,
                         String employmentType, JobStatus status, String description, String requirements,
                         BigDecimal minSalary, BigDecimal maxSalary, Instant createdAt, List<Stage> stages,
                         long applicationsCount) {}
}


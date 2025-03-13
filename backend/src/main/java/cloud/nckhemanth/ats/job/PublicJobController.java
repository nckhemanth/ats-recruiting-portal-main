package cloud.nckhemanth.ats.job;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/jobs")
public class PublicJobController {
    private final JobService jobs;
    public PublicJobController(JobService jobs) { this.jobs = jobs; }

    @GetMapping
    public Page<JobDtos.Summary> list(@RequestParam(required = false) String query,
                                      @RequestParam(required = false) String location,
                                      @PageableDefault(size = 12, sort = "createdAt") Pageable pageable) {
        return jobs.publicJobs(query, location, pageable);
    }

    @GetMapping("/{id}")
    public JobDtos.Detail detail(@PathVariable Long id) { return jobs.detail(id); }
}


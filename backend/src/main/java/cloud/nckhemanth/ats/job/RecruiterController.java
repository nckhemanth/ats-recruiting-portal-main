package cloud.nckhemanth.ats.job;

import cloud.nckhemanth.ats.application.ApplicationDtos;
import cloud.nckhemanth.ats.application.ApplicationService;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/recruiter")
@PreAuthorize("hasRole('RECRUITER')")
public class RecruiterController {
    private final JobService jobs;
    private final ApplicationService applications;

    public RecruiterController(JobService jobs, ApplicationService applications) {
        this.jobs = jobs;
        this.applications = applications;
    }

    @GetMapping("/jobs")
    List<JobDtos.Summary> jobs(Authentication auth) { return jobs.recruiterJobs(auth.getName()); }

    @PostMapping("/jobs")
    @ResponseStatus(HttpStatus.CREATED)
    JobDtos.Detail create(Authentication auth, @RequestBody JobService.CreateJobRequest request) {
        return jobs.create(auth.getName(), request);
    }

    @GetMapping("/jobs/{jobId}")
    RecruiterJobDetail detail(Authentication auth, @PathVariable Long jobId) {
        return new RecruiterJobDetail(jobs.detail(jobId), applications.jobApplications(auth.getName(), jobId));
    }

    @PostMapping("/applications/{applicationId}/move")
    ApplicationDtos.View move(Authentication auth, @PathVariable Long applicationId,
                              @RequestBody MoveRequest request) {
        return applications.move(auth.getName(), applicationId, request.stageId());
    }

    @PostMapping("/applications/{applicationId}/notes")
    ApplicationDtos.View note(Authentication auth, @PathVariable Long applicationId,
                              @RequestBody NoteRequest request) {
        return applications.addNote(auth.getName(), applicationId, request.body());
    }

    public record RecruiterJobDetail(JobDtos.Detail job, List<ApplicationDtos.View> applications) {}
    public record MoveRequest(Long stageId) {}
    public record NoteRequest(String body) {}
}


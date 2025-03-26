package cloud.nckhemanth.ats.application;

import cloud.nckhemanth.ats.job.*;
import cloud.nckhemanth.ats.user.UserAccount;
import cloud.nckhemanth.ats.user.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Service
public class ApplicationService {
    private final ApplicationRepository applications;
    private final JobRepository jobs;
    private final UserRepository users;
    private final ResumeStorage resumes;

    public ApplicationService(ApplicationRepository applications, JobRepository jobs,
                              UserRepository users, ResumeStorage resumes) {
        this.applications = applications;
        this.jobs = jobs;
        this.users = users;
        this.resumes = resumes;
    }

    @Transactional(readOnly = true)
    public List<ApplicationDtos.View> candidateApplications(String email) {
        return applications.findByCandidateEmailIgnoreCaseOrderByUpdatedAtDesc(email).stream()
                .map(application -> ApplicationDtos.View.from(application, false)).toList();
    }

    @Transactional
    public ApplicationDtos.View apply(String email, Long jobId, String coverLetter, MultipartFile resume) {
        UserAccount candidate = users.findByEmailIgnoreCase(email).orElseThrow();
        JobPosting job = jobs.findDetailedById(jobId).orElseThrow();
        if (job.getStatus() != JobStatus.OPEN) throw new IllegalStateException("This role is no longer accepting applications");
        if (applications.existsByJobIdAndCandidateId(jobId, candidate.getId())) {
            throw new IllegalArgumentException("You already applied to this role");
        }
        JobStage firstStage = job.getStages().stream().findFirst().orElseThrow();
        JobApplication saved = applications.save(new JobApplication(job, candidate, firstStage,
                resumes.store(resume), coverLetter));
        return ApplicationDtos.View.from(saved, false);
    }

    @Transactional(readOnly = true)
    public List<ApplicationDtos.View> jobApplications(String recruiterEmail, Long jobId) {
        verifyJobOwner(recruiterEmail, jobId);
        return applications.findByJobIdOrderByUpdatedAtDesc(jobId).stream()
                .map(application -> ApplicationDtos.View.from(application, true)).toList();
    }

    @Transactional
    public ApplicationDtos.View move(String recruiterEmail, Long applicationId, Long stageId) {
        JobApplication application = applications.findDetailedById(applicationId).orElseThrow();
        verifyOwner(recruiterEmail, application);
        JobStage stage = application.getJob().getStages().stream()
                .filter(item -> item.getId().equals(stageId)).findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Stage does not belong to this job"));
        application.moveTo(stage);
        return ApplicationDtos.View.from(application, true);
    }

    @Transactional
    public ApplicationDtos.View addNote(String recruiterEmail, Long applicationId, String body) {
        JobApplication application = applications.findDetailedById(applicationId).orElseThrow();
        verifyOwner(recruiterEmail, application);
        if (body == null || body.isBlank()) throw new IllegalArgumentException("Note cannot be empty");
        UserAccount author = users.findByEmailIgnoreCase(recruiterEmail).orElseThrow();
        application.addNote(author, body);
        return ApplicationDtos.View.from(application, true);
    }

    private void verifyJobOwner(String email, Long jobId) {
        JobPosting job = jobs.findDetailedById(jobId).orElseThrow();
        if (!job.getRecruiter().getEmail().equalsIgnoreCase(email)) throw new IllegalStateException("Job access denied");
    }

    private void verifyOwner(String email, JobApplication application) {
        if (!application.getJob().getRecruiter().getEmail().equalsIgnoreCase(email)) {
            throw new IllegalStateException("Application access denied");
        }
    }
}


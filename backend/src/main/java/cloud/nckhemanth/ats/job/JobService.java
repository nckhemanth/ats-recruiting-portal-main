package cloud.nckhemanth.ats.job;

import cloud.nckhemanth.ats.application.ApplicationRepository;
import cloud.nckhemanth.ats.search.JobSearchService;
import cloud.nckhemanth.ats.user.UserAccount;
import cloud.nckhemanth.ats.user.UserRepository;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

@Service
public class JobService {
    private final JobRepository jobs;
    private final UserRepository users;
    private final ApplicationRepository applications;
    private final ObjectProvider<JobSearchService> search;

    public JobService(JobRepository jobs, UserRepository users, ApplicationRepository applications,
                      ObjectProvider<JobSearchService> search) {
        this.jobs = jobs;
        this.users = users;
        this.applications = applications;
        this.search = search;
    }

    @Transactional(readOnly = true)
    public Page<JobDtos.Summary> publicJobs(String query, String location, Pageable pageable) {
        if (query != null && !query.isBlank() && search.getIfAvailable() != null) {
            List<Long> ids = search.getObject().search(query, pageable);
            List<JobDtos.Summary> content = jobs.findAllById(ids).stream().map(this::summary).toList();
            return new PageImpl<>(content, pageable, content.size());
        }
        return jobs.findAll(JobSpecifications.openJobs(query, location), pageable).map(this::summary);
    }

    @Transactional(readOnly = true)
    public JobDtos.Detail detail(Long id) { return detailOf(jobs.findDetailedById(id).orElseThrow()); }

    @Transactional(readOnly = true)
    public List<JobDtos.Summary> recruiterJobs(String email) {
        return jobs.findByRecruiterEmailIgnoreCaseOrderByCreatedAtDesc(email).stream().map(this::summary).toList();
    }

    @Transactional
    public JobDtos.Detail create(String email, CreateJobRequest request) {
        UserAccount recruiter = users.findByEmailIgnoreCase(email).orElseThrow();
        JobPosting job = new JobPosting(request.title(), request.company(), request.location(), request.department(),
                request.employmentType(), request.description(), request.requirements(), request.minSalary(),
                request.maxSalary(), recruiter);
        List<String> stageNames = request.stages() == null || request.stages().isBlank()
                ? List.of("Applied", "Screen", "Interview", "Offer")
                : Arrays.stream(request.stages().split("\\R")).map(String::trim).filter(s -> !s.isBlank()).toList();
        for (int i = 0; i < stageNames.size(); i++) job.addStage(stageNames.get(i), i);
        JobPosting saved = jobs.save(job);
        JobSearchService index = search.getIfAvailable();
        if (index != null) index.index(saved);
        return detailOf(saved);
    }

    private JobDtos.Summary summary(JobPosting job) {
        return new JobDtos.Summary(job.getId(), job.getTitle(), job.getCompany(), job.getLocation(),
                job.getDepartment(), job.getEmploymentType(), job.getStatus(), job.getMinSalary(),
                job.getMaxSalary(), job.getCreatedAt(), applications.countByJobId(job.getId()));
    }

    private JobDtos.Detail detailOf(JobPosting job) {
        return new JobDtos.Detail(job.getId(), job.getTitle(), job.getCompany(), job.getLocation(),
                job.getDepartment(), job.getEmploymentType(), job.getStatus(), job.getDescription(),
                job.getRequirements(), job.getMinSalary(), job.getMaxSalary(), job.getCreatedAt(),
                job.getStages().stream().map(JobDtos.Stage::from).toList(), applications.countByJobId(job.getId()));
    }

    public record CreateJobRequest(String title, String company, String location, String department,
                                   String employmentType, String description, String requirements,
                                   BigDecimal minSalary, BigDecimal maxSalary, String stages) {}
}

package cloud.nckhemanth.ats.application;

import cloud.nckhemanth.ats.job.JobPosting;
import cloud.nckhemanth.ats.job.JobStage;
import cloud.nckhemanth.ats.user.UserAccount;
import jakarta.persistence.*;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "applications", uniqueConstraints = @UniqueConstraint(columnNames = {"job_id", "candidate_id"}))
public class JobApplication {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY) private Long id;
    @ManyToOne(fetch = FetchType.LAZY, optional = false) @JoinColumn(name = "job_id") private JobPosting job;
    @ManyToOne(fetch = FetchType.LAZY, optional = false) @JoinColumn(name = "candidate_id") private UserAccount candidate;
    @ManyToOne(fetch = FetchType.LAZY) @JoinColumn(name = "stage_id") private JobStage stage;
    @Enumerated(EnumType.STRING) @Column(nullable = false) private ApplicationStatus status;
    @Column(name = "resume_path") private String resumePath;
    @Column(name = "cover_letter", columnDefinition = "text") private String coverLetter;
    @Column(name = "created_at", nullable = false) private Instant createdAt;
    @Column(name = "updated_at", nullable = false) private Instant updatedAt;
    @OneToMany(mappedBy = "application", cascade = CascadeType.ALL, orphanRemoval = true)
    @OrderBy("createdAt asc") private List<ApplicationNote> notes = new ArrayList<>();

    protected JobApplication() {}
    public JobApplication(JobPosting job, UserAccount candidate, JobStage stage, String resumePath, String coverLetter) {
        this.job = job;
        this.candidate = candidate;
        this.stage = stage;
        this.resumePath = resumePath;
        this.coverLetter = coverLetter;
        this.status = ApplicationStatus.ACTIVE;
        this.createdAt = Instant.now();
        this.updatedAt = createdAt;
    }
    public void moveTo(JobStage stage) { this.stage = stage; this.updatedAt = Instant.now(); }
    public void addNote(UserAccount author, String body) { notes.add(new ApplicationNote(this, author, body)); }
    public Long getId() { return id; }
    public JobPosting getJob() { return job; }
    public UserAccount getCandidate() { return candidate; }
    public JobStage getStage() { return stage; }
    public ApplicationStatus getStatus() { return status; }
    public String getResumePath() { return resumePath; }
    public String getCoverLetter() { return coverLetter; }
    public Instant getCreatedAt() { return createdAt; }
    public Instant getUpdatedAt() { return updatedAt; }
    public List<ApplicationNote> getNotes() { return notes; }
}


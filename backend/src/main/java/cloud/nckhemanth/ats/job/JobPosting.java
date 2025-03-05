package cloud.nckhemanth.ats.job;

import cloud.nckhemanth.ats.user.UserAccount;
import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "jobs")
public class JobPosting {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false) private String title;
    @Column(nullable = false) private String company;
    @Column(nullable = false) private String location;
    private String department;
    @Column(name = "employment_type", nullable = false) private String employmentType;
    @Enumerated(EnumType.STRING) @Column(nullable = false) private JobStatus status;
    @Column(nullable = false, columnDefinition = "text") private String description;
    @Column(columnDefinition = "text") private String requirements;
    @Column(name = "min_salary") private BigDecimal minSalary;
    @Column(name = "max_salary") private BigDecimal maxSalary;
    @ManyToOne(fetch = FetchType.LAZY, optional = false) @JoinColumn(name = "recruiter_id")
    private UserAccount recruiter;
    @Column(name = "created_at", nullable = false) private Instant createdAt;
    @OneToMany(mappedBy = "job", cascade = CascadeType.ALL, orphanRemoval = true)
    @OrderBy("position asc")
    private List<JobStage> stages = new ArrayList<>();

    protected JobPosting() {}

    public JobPosting(String title, String company, String location, String department,
                      String employmentType, String description, String requirements,
                      BigDecimal minSalary, BigDecimal maxSalary, UserAccount recruiter) {
        this.title = title;
        this.company = company;
        this.location = location;
        this.department = department;
        this.employmentType = employmentType;
        this.description = description;
        this.requirements = requirements;
        this.minSalary = minSalary;
        this.maxSalary = maxSalary;
        this.recruiter = recruiter;
        this.status = JobStatus.OPEN;
        this.createdAt = Instant.now();
    }

    public void addStage(String name, int position) { stages.add(new JobStage(this, name, position)); }
    public Long getId() { return id; }
    public String getTitle() { return title; }
    public String getCompany() { return company; }
    public String getLocation() { return location; }
    public String getDepartment() { return department; }
    public String getEmploymentType() { return employmentType; }
    public JobStatus getStatus() { return status; }
    public String getDescription() { return description; }
    public String getRequirements() { return requirements; }
    public BigDecimal getMinSalary() { return minSalary; }
    public BigDecimal getMaxSalary() { return maxSalary; }
    public UserAccount getRecruiter() { return recruiter; }
    public Instant getCreatedAt() { return createdAt; }
    public List<JobStage> getStages() { return stages; }
}


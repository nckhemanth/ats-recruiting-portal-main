package cloud.nckhemanth.ats.job;

import jakarta.persistence.*;

@Entity
@Table(name = "job_stages")
public class JobStage {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne(fetch = FetchType.LAZY, optional = false) @JoinColumn(name = "job_id")
    private JobPosting job;
    @Column(nullable = false) private String name;
    @Column(nullable = false) private int position;

    protected JobStage() {}
    JobStage(JobPosting job, String name, int position) {
        this.job = job;
        this.name = name;
        this.position = position;
    }
    public Long getId() { return id; }
    public JobPosting getJob() { return job; }
    public String getName() { return name; }
    public int getPosition() { return position; }
}


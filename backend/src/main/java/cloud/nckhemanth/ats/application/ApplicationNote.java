package cloud.nckhemanth.ats.application;

import cloud.nckhemanth.ats.user.UserAccount;
import jakarta.persistence.*;
import java.time.Instant;

@Entity
@Table(name = "application_notes")
public class ApplicationNote {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY) private Long id;
    @ManyToOne(fetch = FetchType.LAZY, optional = false) @JoinColumn(name = "application_id") private JobApplication application;
    @ManyToOne(fetch = FetchType.LAZY) @JoinColumn(name = "author_id") private UserAccount author;
    @Column(nullable = false, columnDefinition = "text") private String body;
    @Column(name = "created_at", nullable = false) private Instant createdAt;

    protected ApplicationNote() {}
    ApplicationNote(JobApplication application, UserAccount author, String body) {
        this.application = application;
        this.author = author;
        this.body = body.trim();
        this.createdAt = Instant.now();
    }
    public Long getId() { return id; }
    public UserAccount getAuthor() { return author; }
    public String getBody() { return body; }
    public Instant getCreatedAt() { return createdAt; }
}


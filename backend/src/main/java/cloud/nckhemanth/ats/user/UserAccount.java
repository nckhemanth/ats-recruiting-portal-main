package cloud.nckhemanth.ats.user;

import jakarta.persistence.*;
import java.time.Instant;

@Entity
@Table(name = "users")
public class UserAccount {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false, unique = true)
    private String email;
    @Column(name = "password_hash", nullable = false)
    private String passwordHash;
    @Column(name = "full_name", nullable = false)
    private String fullName;
    @Enumerated(EnumType.STRING) @Column(nullable = false)
    private Role role;
    private String phone;
    private String location;
    @Column(columnDefinition = "text")
    private String bio;
    @Column(name = "created_at", nullable = false)
    private Instant createdAt;

    protected UserAccount() {}

    public UserAccount(String email, String passwordHash, String fullName, Role role,
                       String phone, String location) {
        this.email = email.toLowerCase().trim();
        this.passwordHash = passwordHash;
        this.fullName = fullName.trim();
        this.role = role;
        this.phone = phone;
        this.location = location;
        this.createdAt = Instant.now();
    }

    public void updateProfile(String fullName, String phone, String location, String bio) {
        this.fullName = fullName.trim();
        this.phone = phone;
        this.location = location;
        this.bio = bio;
    }

    public Long getId() { return id; }
    public String getEmail() { return email; }
    public String getPasswordHash() { return passwordHash; }
    public String getFullName() { return fullName; }
    public Role getRole() { return role; }
    public String getPhone() { return phone; }
    public String getLocation() { return location; }
    public String getBio() { return bio; }
    public Instant getCreatedAt() { return createdAt; }
}


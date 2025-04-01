package cloud.nckhemanth.ats.config;

import cloud.nckhemanth.ats.job.JobPosting;
import cloud.nckhemanth.ats.job.JobRepository;
import cloud.nckhemanth.ats.user.*;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.math.BigDecimal;
import java.util.List;

@Configuration
@ConditionalOnProperty(name = "app.seed-demo", havingValue = "true")
public class DemoDataConfiguration {
    @Bean
    CommandLineRunner demoData(UserRepository users, JobRepository jobs, PasswordEncoder passwords) {
        return args -> {
            if (users.count() > 0) return;
            UserAccount recruiter = users.save(new UserAccount("recruiter@recruitflow.dev",
                    passwords.encode("Recruiter123!"), "Maya Chen", Role.RECRUITER, null, "New York"));
            users.save(new UserAccount("candidate@recruitflow.dev", passwords.encode("Candidate123!"),
                    "Jordan Lee", Role.CANDIDATE, "+1 212 555 0147", "Brooklyn, NY"));

            List<JobPosting> seeded = List.of(
                    job("Senior Backend Engineer", "Northstar Health", "New York, NY", "Engineering",
                            "Build resilient Spring services for care delivery teams.", recruiter),
                    job("Product Designer", "Lumen Finance", "Remote", "Design",
                            "Shape calm, accessible financial workflows used by growing businesses.", recruiter),
                    job("Data Platform Engineer", "Fieldwork Labs", "Boston, MA", "Data",
                            "Develop trusted pipelines and search experiences for research teams.", recruiter));
            jobs.saveAll(seeded);
        };
    }

    private JobPosting job(String title, String company, String location, String department,
                           String description, UserAccount recruiter) {
        JobPosting job = new JobPosting(title, company, location, department, "Full-time", description,
                "Strong communication, thoughtful engineering fundamentals, and a collaborative mindset.",
                new BigDecimal("120000"), new BigDecimal("175000"), recruiter);
        List.of("Applied", "Screen", "Interview", "Offer").forEach(name -> job.addStage(name, job.getStages().size()));
        return job;
    }
}


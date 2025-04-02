package cloud.nckhemanth.ats;

import cloud.nckhemanth.ats.auth.JwtService;
import cloud.nckhemanth.ats.user.Role;
import cloud.nckhemanth.ats.user.UserAccount;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class JwtServiceTest {
    @Test
    void tokenRetainsAuthenticatedEmail() {
        JwtService service = new JwtService("a-test-secret-key-that-is-longer-than-32-bytes", 1);
        UserAccount user = new UserAccount("candidate@example.com", "hash", "Candidate User",
                Role.CANDIDATE, null, null);

        String token = service.issue(user);

        assertThat(service.subject(token)).isEqualTo("candidate@example.com");
    }
}


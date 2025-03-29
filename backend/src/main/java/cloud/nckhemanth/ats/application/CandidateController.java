package cloud.nckhemanth.ats.application;

import cloud.nckhemanth.ats.user.UserAccount;
import cloud.nckhemanth.ats.user.UserDto;
import cloud.nckhemanth.ats.user.UserRepository;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/api/candidate")
@PreAuthorize("hasRole('CANDIDATE')")
public class CandidateController {
    private final ApplicationService applications;
    private final UserRepository users;

    public CandidateController(ApplicationService applications, UserRepository users) {
        this.applications = applications;
        this.users = users;
    }

    @GetMapping("/applications")
    List<ApplicationDtos.View> applications(Authentication auth) {
        return applications.candidateApplications(auth.getName());
    }

    @PostMapping(value = "/jobs/{jobId}/apply", consumes = "multipart/form-data")
    ApplicationDtos.View apply(Authentication auth, @PathVariable Long jobId,
                               @RequestPart(required = false) String coverLetter,
                               @RequestPart(required = false) MultipartFile resume) {
        return applications.apply(auth.getName(), jobId, coverLetter, resume);
    }

    @PutMapping("/profile")
    @Transactional
    UserDto profile(Authentication auth, @RequestBody ProfileRequest request) {
        UserAccount user = users.findByEmailIgnoreCase(auth.getName()).orElseThrow();
        user.updateProfile(request.fullName(), request.phone(), request.location(), request.bio());
        return UserDto.from(user);
    }

    public record ProfileRequest(String fullName, String phone, String location, String bio) {}
}


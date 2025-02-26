package cloud.nckhemanth.ats.auth;

import cloud.nckhemanth.ats.user.*;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {
    private final UserRepository users;
    private final PasswordEncoder passwords;
    private final JwtService tokens;

    public AuthController(UserRepository users, PasswordEncoder passwords, JwtService tokens) {
        this.users = users;
        this.passwords = passwords;
        this.tokens = tokens;
    }

    @PostMapping("/register")
    @ResponseStatus(HttpStatus.CREATED)
    public SessionResponse register(@Valid @RequestBody RegisterRequest request) {
        if (users.existsByEmailIgnoreCase(request.email())) {
            throw new IllegalArgumentException("An account already exists for this email");
        }
        UserAccount user = users.save(new UserAccount(request.email(), passwords.encode(request.password()),
                request.fullName(), request.role(), request.phone(), request.location()));
        return new SessionResponse(tokens.issue(user), UserDto.from(user));
    }

    @PostMapping("/login")
    public SessionResponse login(@Valid @RequestBody LoginRequest request) {
        UserAccount user = users.findByEmailIgnoreCase(request.email())
                .orElseThrow(() -> new BadCredentialsException("Invalid email or password"));
        if (!passwords.matches(request.password(), user.getPasswordHash())) {
            throw new BadCredentialsException("Invalid email or password");
        }
        return new SessionResponse(tokens.issue(user), UserDto.from(user));
    }

    @GetMapping("/me")
    public UserDto me(Authentication authentication) {
        return UserDto.from(users.findByEmailIgnoreCase(authentication.getName()).orElseThrow());
    }

    public record RegisterRequest(@Email @NotBlank String email, @Size(min = 8) String password,
                                  @NotBlank String fullName, Role role, String phone, String location) {}
    public record LoginRequest(@Email @NotBlank String email, @NotBlank String password) {}
    public record SessionResponse(String token, UserDto user) {}
}


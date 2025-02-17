package cloud.nckhemanth.ats.user;

import java.time.Instant;

public record UserDto(Long id, String email, String fullName, Role role, String phone,
                      String location, String bio, Instant createdAt) {
    public static UserDto from(UserAccount user) {
        return new UserDto(user.getId(), user.getEmail(), user.getFullName(), user.getRole(),
                user.getPhone(), user.getLocation(), user.getBio(), user.getCreatedAt());
    }
}


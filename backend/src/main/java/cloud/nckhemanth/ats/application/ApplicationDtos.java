package cloud.nckhemanth.ats.application;

import cloud.nckhemanth.ats.job.JobDtos;
import cloud.nckhemanth.ats.user.UserDto;
import java.time.Instant;
import java.util.List;

public final class ApplicationDtos {
    private ApplicationDtos() {}

    public record Note(Long id, String body, Instant createdAt, String authorName) {
        static Note from(ApplicationNote note) {
            return new Note(note.getId(), note.getBody(), note.getCreatedAt(),
                    note.getAuthor() == null ? "System" : note.getAuthor().getFullName());
        }
    }

    public record View(Long id, ApplicationStatus status, String resumePath, String coverLetter,
                       Instant createdAt, Instant updatedAt, Long jobId, String jobTitle,
                       JobDtos.Stage stage, UserDto candidate, List<Note> notes) {
        public static View from(JobApplication application, boolean includeCandidate) {
            return new View(application.getId(), application.getStatus(), application.getResumePath(),
                    application.getCoverLetter(), application.getCreatedAt(), application.getUpdatedAt(),
                    application.getJob().getId(), application.getJob().getTitle(),
                    application.getStage() == null ? null : JobDtos.Stage.from(application.getStage()),
                    includeCandidate ? UserDto.from(application.getCandidate()) : null,
                    application.getNotes().stream().map(Note::from).toList());
        }
    }
}


package cloud.nckhemanth.ats.application;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;

public interface ApplicationRepository extends JpaRepository<JobApplication, Long> {
    @EntityGraph(attributePaths = {"job", "stage"})
    List<JobApplication> findByCandidateEmailIgnoreCaseOrderByUpdatedAtDesc(String email);
    @EntityGraph(attributePaths = {"candidate", "stage", "notes", "notes.author"})
    List<JobApplication> findByJobIdOrderByUpdatedAtDesc(Long jobId);
    @EntityGraph(attributePaths = {"job", "job.recruiter", "candidate", "stage", "notes", "notes.author"})
    Optional<JobApplication> findDetailedById(Long id);
    boolean existsByJobIdAndCandidateId(Long jobId, Long candidateId);
    long countByJobId(Long jobId);
}


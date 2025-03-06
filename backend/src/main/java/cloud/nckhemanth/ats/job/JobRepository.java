package cloud.nckhemanth.ats.job;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;
import java.util.Optional;

public interface JobRepository extends JpaRepository<JobPosting, Long>, JpaSpecificationExecutor<JobPosting> {
    @EntityGraph(attributePaths = "stages")
    Optional<JobPosting> findDetailedById(Long id);
    Page<JobPosting> findByStatus(JobStatus status, Pageable pageable);
    List<JobPosting> findByRecruiterEmailIgnoreCaseOrderByCreatedAtDesc(String email);
}


package cloud.nckhemanth.ats.job;

import org.springframework.data.jpa.domain.Specification;

public final class JobSpecifications {
    private JobSpecifications() {}

    public static Specification<JobPosting> openJobs(String query, String location) {
        return (root, criteria, builder) -> {
            var predicate = builder.equal(root.get("status"), JobStatus.OPEN);
            if (query != null && !query.isBlank()) {
                String term = "%" + query.toLowerCase() + "%";
                predicate = builder.and(predicate, builder.or(
                        builder.like(builder.lower(root.get("title")), term),
                        builder.like(builder.lower(root.get("company")), term),
                        builder.like(builder.lower(root.get("description")), term)));
            }
            if (location != null && !location.isBlank()) {
                predicate = builder.and(predicate,
                        builder.like(builder.lower(root.get("location")), "%" + location.toLowerCase() + "%"));
            }
            return predicate;
        };
    }
}


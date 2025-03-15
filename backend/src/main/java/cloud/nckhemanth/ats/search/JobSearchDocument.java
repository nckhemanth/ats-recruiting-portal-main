package cloud.nckhemanth.ats.search;

import cloud.nckhemanth.ats.job.JobPosting;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;

@Document(indexName = "ats-jobs", createIndex = false)
public record JobSearchDocument(@Id String id, String title, String company, String location,
                                String department, String description, String requirements) {
    public static JobSearchDocument from(JobPosting job) {
        return new JobSearchDocument(job.getId().toString(), job.getTitle(), job.getCompany(),
                job.getLocation(), job.getDepartment(), job.getDescription(), job.getRequirements());
    }
}


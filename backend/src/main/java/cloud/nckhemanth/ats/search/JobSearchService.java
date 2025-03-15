package cloud.nckhemanth.ats.search;

import cloud.nckhemanth.ats.job.JobPosting;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.client.elc.NativeQuery;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@ConditionalOnProperty(name = "app.elasticsearch-enabled", havingValue = "true")
public class JobSearchService {
    private final ElasticsearchOperations elasticsearch;

    public JobSearchService(ElasticsearchOperations elasticsearch) { this.elasticsearch = elasticsearch; }

    public void index(JobPosting job) { elasticsearch.save(JobSearchDocument.from(job)); }

    public List<Long> search(String text, Pageable pageable) {
        var query = NativeQuery.builder()
                .withQuery(q -> q.multiMatch(m -> m.query(text)
                        .fields("title^3", "company^2", "location", "department", "description", "requirements")))
                .withPageable(pageable)
                .build();
        return elasticsearch.search(query, JobSearchDocument.class).stream()
                .map(hit -> Long.valueOf(hit.getContent().id())).toList();
    }
}


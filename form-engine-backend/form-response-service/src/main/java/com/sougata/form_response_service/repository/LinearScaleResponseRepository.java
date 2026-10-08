package com.sougata.form_response_service.repository;

import com.sougata.form_response_service.model.LinearScaleResponse;
import jakarta.persistence.Tuple;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository("LINEAR_SCALE_RESPONSE_REPOSITORY")
public interface LinearScaleResponseRepository extends AnyTypeQuestionResponseRepository<LinearScaleResponse, Long> {

    @Query(value = """
            select linear_scale_responses_increment_or_create(
                cast(:batchResponses as jsonb)
            )
            """, nativeQuery = true)
    void createOrIncrement(String batchResponses);

    @Query("""
            select
            l.scale scale,
            l.responseCount responseCount
            from LinearScaleResponse l
            where l.questionId = :questionId
            order by l.responseCount desc, l.scale
            """)
    List<Tuple> groupedByScale(Long questionId, Pageable pageable);
}

package com.sougata.form_response_service.repository;

import com.sougata.form_response_service.model.DropdownResponse;
import jakarta.persistence.Tuple;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository("DROPDOWN_RESPONSE_REPOSITORY")
public interface DropdownResponseRepository extends AnyTypeQuestionResponseRepository<DropdownResponse, Long> {

    @Query(value = """
            select dropdown_responses_increment_or_create(
                cast(:batchResponses as jsonb)
            )
            """, nativeQuery = true)
    void createOrIncrement(String batchResponses);

    @Query("""
            select
            dr.optionId optionId,
            dr.responseCount responseCount
            from DropdownResponse dr
            where dr.questionId = :questionId
            order by dr.responseCount desc, dr.optionId
            """)
    List<Tuple> groupedByOptionIds(Long questionId, Pageable pageable);
}

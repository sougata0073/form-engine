package com.sougata.form_service.repository.template;

import com.sougata.form_engine.dto.template.TemplateSummaryDto;
import com.sougata.form_service.model.template.RecentlyUsedTemplate;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Repository
public interface RecentlyUsedTemplateRepository extends JpaRepository<RecentlyUsedTemplate, Long> {

    @Query("""
            select
            new com.sougata.form_engine.dto.template.TemplateSummaryDto(
                t.id,
                t.name,
                'Recently used'
            )
            from RecentlyUsedTemplate rt
            join rt.template t
            join t.category tc
            where rt.userId = :userId
            order by rt.createdAt desc
            """)
    List<TemplateSummaryDto> getByUserId(UUID userId);

    @Modifying
    @Transactional
    @Query(value = """
            DELETE FROM form_template.recently_used_templates
            WHERE id IN (
                SELECT id
                FROM (
                    SELECT
                        id,
                        ROW_NUMBER() OVER (
                            PARTITION BY user_id
                            ORDER BY created_at DESC, id DESC
                        ) rn
                    FROM form_template.recently_used_templates
                    WHERE user_id = :userId
                ) t
                WHERE rn >= :windowSize
            )
            """, nativeQuery = true)
    void deleteOld(UUID userId, int windowSize);

    boolean existsByUserIdAndTemplateId(UUID userId, Long templateId);

    @Modifying
    @Transactional
    @Query("""
            delete from RecentlyUsedTemplate rt
            where rt.userId = :userId and rt.template.id = :templateId
            """)
    void deleteByUserAndTemplate(UUID userId, Long templateId);

}

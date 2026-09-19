package com.sougata.form_service.model.template;

import com.sougata.form_service.model.Auditable;
import io.hypersistence.utils.hibernate.id.Tsid;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.util.UUID;

@Entity
@Table(
        name = "recently_used_templates",
        schema = "form_template",
        uniqueConstraints = {
                @UniqueConstraint(
                        name = "unique_user_id_template_id",
                        columnNames = {"user_id", "template_id"}
                )
        })
@EntityListeners(AuditingEntityListener.class)
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class RecentlyUsedTemplate extends Auditable {

    @Id
    @Tsid
    private Long id;

    @Column(nullable = false)
    private UUID userId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Template template;

}

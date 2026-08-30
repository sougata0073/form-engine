package com.sougata.form_service.model.template;

import com.sougata.form_engine.constant.RatingIcon;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@Entity
@Table(name = "rating_templates", schema = "form_template")
@EntityListeners(AuditingEntityListener.class)
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class RatingTemplate extends AnyTypeQuestionTemplate {

    @Column(nullable = false)
    private Integer maxRatingNumber;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private RatingIcon ratingIcon;

}

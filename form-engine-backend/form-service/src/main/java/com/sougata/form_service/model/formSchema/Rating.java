package com.sougata.form_service.model.formSchema;

import com.sougata.form_engine.constant.RatingIcon;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "ratings", schema = "form_schema")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Rating extends AnyTypeQuestion {

    @Column(nullable = false)
    private Integer maxRatingNumber;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private RatingIcon ratingIcon;
}

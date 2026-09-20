package com.sougata.form_response_service.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "checkbox_responses")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class CheckboxResponse extends AnyTypeQuestionResponse {

    @Column(nullable = false)
    private Long optionId;

}

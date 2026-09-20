package com.sougata.form_response_service.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "paragraph_responses")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ParagraphResponse extends AnyTypeQuestionResponse {

        @Column(nullable = false)
        private String text;


}

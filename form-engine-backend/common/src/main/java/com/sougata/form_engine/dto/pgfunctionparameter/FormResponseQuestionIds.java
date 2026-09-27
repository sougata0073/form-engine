package com.sougata.form_engine.dto.pgfunctionparameter;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Set;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class FormResponseQuestionIds {

    private Set<Long> questionIds;

}

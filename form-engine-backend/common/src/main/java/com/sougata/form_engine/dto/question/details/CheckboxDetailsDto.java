package com.sougata.form_engine.dto.question.details;

import com.sougata.form_engine.dto.validation.config.ValidationConfig;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import tools.jackson.databind.annotation.JsonSerialize;
import tools.jackson.databind.ser.std.ToStringSerializer;

import java.util.List;
import java.util.Objects;
import java.util.function.Function;
import java.util.stream.Collectors;

@NoArgsConstructor
@Getter
@Setter
public class CheckboxDetailsDto extends QuestionDetailsDto {
    private List<Option> options;
    private ValidationConfig validationConfig;

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        CheckboxDetailsDto that = (CheckboxDetailsDto) o;

        var thisOptionsMapById = options.stream().collect(Collectors.toMap(Option::getId, Function.identity()));
        var thatOptionsMapById = that.options.stream().collect(Collectors.toMap(Option::getId, Function.identity()));

        for (var entry : thisOptionsMapById.entrySet()) {
            if (Objects.equals(entry.getValue(), thatOptionsMapById.get(entry.getKey()))) {
                return false;
            }
        }

        return super.equals(that) && Objects.equals(validationConfig, that.validationConfig);
    }

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Option {
        @JsonSerialize(using = ToStringSerializer.class)
        private Long id;
        private String option;
        private Integer orderIndex;

        @Override
        public boolean equals(Object o) {
            if (o == null || getClass() != o.getClass()) return false;
            Option option1 = (Option) o;
            return Objects.equals(id, option1.id) && Objects.equals(option, option1.option) && Objects.equals(orderIndex, option1.orderIndex);
        }
    }
}

package com.sougata.form_service.model.formSchema;

import com.sougata.form_service.model.Auditable;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "forms", schema = "form_schema")
@EntityListeners(AuditingEntityListener.class)
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Form extends Auditable {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false)
    private UUID userId;

    @Column(columnDefinition = "text")
    private String name;

    @Column(columnDefinition = "text")
    private String title;

    @Column(columnDefinition = "text")
    private String description;

    @Column(nullable = false)
    private Boolean published;

    @Column(nullable = false)
    private Boolean acceptingResponse;

    @Column(columnDefinition = "text")
    private String notAcceptingResponseMessage;

    private Instant stopAcceptingResponseOn;

    private Integer stopAcceptingResponseAfterResponse;

    @Column(nullable = false)
    private Instant lastOpenedOn;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true, mappedBy = "form")
    private List<Question> questions = new ArrayList<>();
}

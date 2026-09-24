package com.sougata.form_response_service.repository;

import com.sougata.form_response_service.model.FormResponseIndividual;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface FormResponseIndividualRepository extends JpaRepository<FormResponseIndividual, UUID> {
}

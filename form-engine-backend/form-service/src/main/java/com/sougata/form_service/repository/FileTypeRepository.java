package com.sougata.form_service.repository;

import com.sougata.form_service.constant.cacheNames.CommonCacheNames;
import com.sougata.form_service.model.FileType;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface FileTypeRepository extends JpaRepository<FileType, String> {

    @Cacheable(cacheNames = {CommonCacheNames.FILE_TYPE}, key = "#category")
    Optional<FileType> findByCategory(String category);

}

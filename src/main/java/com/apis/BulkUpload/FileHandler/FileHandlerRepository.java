package com.apis.BulkUpload.FileHandler;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FileHandlerRepository extends JpaRepository<FileHandler, Long> {
}

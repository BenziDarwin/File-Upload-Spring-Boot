package com.apis.BulkUpload.FileHandler;

import java.util.ArrayList;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/files")
@RequiredArgsConstructor
public class FileHandlerController {

    @GetMapping("/get-file/{id}")
    public ResponseEntity<String> getFile(@RequestHeader("Authorization") String token, @PathVariable ("id") Long index ) {
        return ResponseEntity.ok("Sucess!" + index);
    }

    @PostMapping("/add-files")
    public ResponseEntity<ArrayList<String>> addFiles() {
    }
}

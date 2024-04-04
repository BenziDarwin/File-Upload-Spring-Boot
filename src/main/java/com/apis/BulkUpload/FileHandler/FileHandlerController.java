package com.apis.BulkUpload.FileHandler;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.method.annotation.MvcUriComponentsBuilder;

@RestController
@RequestMapping("/api/v1/files")
public class FileHandlerController {

    private final FileHandlerService storageService;

    @Autowired
    public FileHandlerController(FileHandlerService storageService) {
        this.storageService = storageService;
    }

    @GetMapping("/")
    public List<String> listUploadedFiles() throws Exception {
        return storageService.loadAll().map(
                path -> MvcUriComponentsBuilder.fromMethodName(FileHandlerController.class,
                        "serveFile", path.getFileName().toString()).build().toUri().toString())
                .collect(Collectors.toList());
    }

    @GetMapping("/{filename:.+}")
    public ResponseEntity<Resource> serveFile(@PathVariable String filename) throws Exception {
        Resource file = storageService.loadAsResource(filename);
        if (file == null)
            return ResponseEntity.notFound().build();
        return ResponseEntity.ok().header(HttpHeaders.CONTENT_DISPOSITION,
                "attachment; filename=\"" + file.getFilename() + "\"").body(file);
    }

    @PostMapping("/")
    public ResponseEntity<String> handleFileUpload(@RequestParam("file") MultipartFile file) throws Exception {
        storageService.store(file);
        return ResponseEntity.ok().body("You successfully uploaded " + file.getOriginalFilename() + "!");
    }

	@PostMapping("/bulk")
    public ResponseEntity<String> handleBulkFileUpload(@RequestParam("files") MultipartFile[] files) throws Exception {
        storageService.bulkStore(files);
        return ResponseEntity.ok().body("You successfully uploaded " + files.length + "files!");
    }

	

    @ExceptionHandler(Exception.class)
    public ResponseEntity<String> handleStorageFileNotFound(Exception exc) {
        return ResponseEntity.notFound().build();
    }
}

package com.apis.BulkUpload.Storage;

import java.nio.file.Path;
import java.util.stream.Stream;
import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

public interface StorageService {

	void init() throws Exception;

	void store(MultipartFile file) throws Exception;

	void bulkStore(MultipartFile[] files) throws Exception;

	Stream<Path> loadAll() throws Exception;

	Path load(String filename) throws Exception;

	Resource loadAsResource(String filename) throws Exception;

	void deleteAll();

}

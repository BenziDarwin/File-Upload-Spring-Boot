package com.apis.BulkUpload.FileHandler;

import io.github.pixee.security.Filenames;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.util.FileSystemUtils;
import org.springframework.web.multipart.MultipartFile;

import com.apis.BulkUpload.Config.StorageProperties;
import com.apis.BulkUpload.Storage.StorageService;

@Service
public class FileHandlerService implements StorageService  {
    
   private final Path rootLocation;

	@Autowired
	public FileHandlerService(StorageProperties properties) throws Exception {
        
        if(properties.getLocation().trim().length() == 0){
            throw new Exception("File upload location can not be Empty."); 
        }

		this.rootLocation = Paths.get(properties.getLocation());
	}

	@Override
	public void bulkStore(MultipartFile[] files) throws Exception {
        List<String> fileNames = new ArrayList<>();
        try {
            for (MultipartFile file : files) {
                if (file.isEmpty()) {
                    throw new Exception("Failed to store empty file.");
                }
                Path destinationFile = this.rootLocation.resolve(Paths.get(Filenames.toSimpleFileName(file.getOriginalFilename())))
                        .normalize().toAbsolutePath();
                if (!destinationFile.getParent().equals(this.rootLocation.toAbsolutePath())) {
                    // This is a security check
                    throw new Exception("Cannot store file outside current directory.");
                }
                try (InputStream inputStream = file.getInputStream()) {
                    Files.copy(inputStream, destinationFile, StandardCopyOption.REPLACE_EXISTING);
                }
                fileNames.add(Filenames.toSimpleFileName(file.getOriginalFilename()));
            }
        } catch (Exception e) {
            throw new Exception("Failed to store files.", e);
        }
    }

	@Override
	public void store(MultipartFile file) throws Exception {
		try {
			if (file.isEmpty()) {
				throw new Exception("Failed to store empty file.");
			}
			Path destinationFile = this.rootLocation.resolve(
					Paths.get(Filenames.toSimpleFileName(file.getOriginalFilename())))
					.normalize().toAbsolutePath();
			if (!destinationFile.getParent().equals(this.rootLocation.toAbsolutePath())) {
				// This is a security check
				throw new Exception(
						"Cannot store file outside current directory.");
			}
			try (InputStream inputStream = file.getInputStream()) {
				Files.copy(inputStream, destinationFile,
					StandardCopyOption.REPLACE_EXISTING);
			}
		}
		catch (Exception e) {
			throw new Exception("Failed to store file.", e);
		}
	}

	@Override
	public Stream<Path> loadAll() throws Exception {
		try {
			return Files.walk(this.rootLocation, 1)
				.filter(path -> !path.equals(this.rootLocation))
				.map(this.rootLocation::relativize);
		}
		catch (IOException e) {
			throw new Exception("Failed to read stored files", e);
		}

	}

	@Override
	public Path load(String filename) {
		return rootLocation.resolve(filename);
	}

	@Override
	public Resource loadAsResource(String filename) throws Exception {
		try {
			Path file = load(filename);

			@SuppressWarnings("null")
            Resource resource = new UrlResource(file.toUri());
			if (resource.exists() || resource.isReadable()) {
				return resource;
			}
			else {
				throw new Exception(
						"Could not read file: " + filename);

			}
		}
		catch (MalformedURLException e) {
			throw new Exception("Could not read file: " + filename, e);
		}
	}

	@Override
	public void deleteAll() {
		FileSystemUtils.deleteRecursively(rootLocation.toFile());
	}

	@Override
	public void init() throws Exception {
		try {
			Files.createDirectories(rootLocation);
		}
		catch (IOException e) {
			throw new Exception("Could not initialize storage", e);
		}
	}
}

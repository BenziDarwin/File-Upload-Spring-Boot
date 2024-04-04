package com.apis.BulkUpload;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;

import com.apis.BulkUpload.Config.StorageProperties;
import com.apis.BulkUpload.FileHandler.FileHandlerService;

@SpringBootApplication
@EnableConfigurationProperties(StorageProperties.class)
public class BulkUploadApplication {

	public static void main(String[] args) {
		SpringApplication.run(BulkUploadApplication.class, args);
	}

	@Bean
	CommandLineRunner init(FileHandlerService storageService) {
		return (args) -> {
			storageService.deleteAll();
			storageService.init();
		};
	}

}

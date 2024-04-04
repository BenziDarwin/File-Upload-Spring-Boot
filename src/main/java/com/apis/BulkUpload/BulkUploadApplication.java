package com.apis.BulkUpload;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

import com.apis.BulkUpload.Config.StorageProperties;

@SpringBootApplication
@EnableConfigurationProperties(StorageProperties.class)
public class BulkUploadApplication {

	public static void main(String[] args) {
		SpringApplication.run(BulkUploadApplication.class, args);
	}

}

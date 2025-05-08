package ru.alex.project.EbbinghausApp;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

import jakarta.annotation.Resource;
import ru.alex.project.EbbinghausApp.services.FilesStorageService;

@SpringBootApplication
@EnableScheduling
public class EbbinghausAppApplication implements CommandLineRunner {
	
	@Resource
	FilesStorageService storageService;

	public static void main(String[] args) {
		SpringApplication.run(EbbinghausAppApplication.class, args);
	}
	
	@Override
	public void run(String... arg) throws Exception {
		//storageService.deleteAll();
		storageService.init();	
	}
}

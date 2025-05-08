package ru.alex.project.EbbinghausApp.services;

import java.nio.file.Path;
import java.util.stream.Stream;

import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

public interface FilesStorageService {

	public void init();

	public void save(MultipartFile file, String pathStr);
	
	public Resource load(String filename, String pathStr);
	  
	public void deleteAll(String pathStr);

	public Stream<Path> loadAll(String pathStr);

	boolean delete(String filename, String pathStr);
	
	public void deleteDirectory(String pathStr);
	
	Path initDirectory(String pathStr);
}

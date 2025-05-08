package ru.alex.project.EbbinghausApp.services;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.FileAlreadyExistsException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.stream.Stream;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.util.FileSystemUtils;
import org.springframework.web.multipart.MultipartFile;

import ru.alex.project.EbbinghausApp.util.ConstantsClass;

@Service
public class FilesStorageServiceImpl implements FilesStorageService {
	
	private static final Logger logger = LogManager.getLogger(FilesStorageServiceImpl.class);
	
	@Override
	public void init() {
		try {
			Files.createDirectories(Paths.get(ConstantsClass.fileStorageRootPath));
		} catch (IOException e) {
			String msg = "Could not initialize root folder for upload!";
			logger.error(msg);
			throw new RuntimeException(msg);
    	}
	}

	@Override
	public void save(MultipartFile file, String pathStr) {
		Path path = initDirectory(pathStr);
		try {
			Files.copy(file.getInputStream(), path.resolve(file.getOriginalFilename()));
		} catch (Exception e) {
			if (e instanceof FileAlreadyExistsException) {
				String msg = "A file with this name is already exists.";
				logger.error(msg);
				throw new RuntimeException(msg);
			}
			logger.error(e.getMessage());
			throw new RuntimeException(e.getMessage());
		}
	}

	@Override
	public Resource load(String filename, String pathStr) {
		Path path = initDirectory(pathStr);
		try {
			Path file = path.resolve(filename);
			Resource resource = new UrlResource(file.toUri());

			if (resource.exists() || resource.isReadable()) {
				return resource;
			} else {
				String msg = "Could not read the file!";
				logger.error(msg);
				throw new RuntimeException(msg);
			}
		} catch (MalformedURLException e) {
			String msg = "Error: " + e.getMessage();
			logger.error(msg);
			throw new RuntimeException(msg);
		}
	}

	@Override
	public boolean delete(String filename, String pathStr) {
		Path path = initDirectory(pathStr);
		try {
			Path file = path.resolve(filename);
			return Files.deleteIfExists(file);
		} catch (IOException e) {
			String msg = "Error: " + e.getMessage();
			logger.error(msg);
			throw new RuntimeException(msg);
		}
	}

	@Override
	public void deleteAll(String pathStr) {
		Path path = initDirectory(pathStr);
		FileSystemUtils.deleteRecursively(path.toFile());
	}

	@Override
	public Stream<Path> loadAll(String pathStr) {
		Path loadPath = initDirectory(pathStr);
		try {
			return Files.walk(loadPath, 1).filter(path -> !path.equals(loadPath)).map(loadPath::relativize);
		} catch (IOException e) {
			String msg = "Could not load the files!";
			logger.error(msg);
			throw new RuntimeException(msg);
		}
	}
	
	@Override
	public void deleteDirectory(String pathStr) {
		File file = new File(pathStr);
		deleteDir(file);
	}
	
	private void deleteDir(File file) { // 1) delete files 2) delete dir
	    File[] contents = file.listFiles();
	    if (contents != null) {
	        for (File f : contents) {
	            if (! Files.isSymbolicLink(f.toPath())) {
	                deleteDir(f);
	            }
	        }
	    }
	    file.delete();
	}
	
	public Path initDirectory(String pathStr) {
		Path path = getPath(pathStr);
		checkDirectory(path);
		return path;
	}
	
	private Path getPath(String filePath) {
		return Paths.get(ConstantsClass.fileStorageRootPath+filePath);
	}
	
	private void checkDirectory(final Path path) {
		try {
			Files.createDirectories(path);
		} catch (IOException e) {
			String msg = "Could not initialize session folder";
			logger.error(msg);
			throw new RuntimeException(msg);
    	}
	}
}
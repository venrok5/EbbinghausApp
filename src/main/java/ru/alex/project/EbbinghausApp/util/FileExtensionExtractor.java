package ru.alex.project.EbbinghausApp.util;
import java.io.File;

import java.util.Optional;

import org.springframework.http.MediaType;
import org.springframework.http.MediaTypeFactory;

public class FileExtensionExtractor {
	
	public static String getFileMIME(String pathStr) {
	    final File file = new File(pathStr);
	    Optional<MediaType> mimeType = MediaTypeFactory.getMediaType(file.getName());
	    
	    if (mimeType.isPresent()) {
	    	return mimeType.get().toString();
	    } 
	    return "empty";
	}
}

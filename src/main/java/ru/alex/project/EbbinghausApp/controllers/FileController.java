 package ru.alex.project.EbbinghausApp.controllers;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.method.annotation.MvcUriComponentsBuilder;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import ru.alex.project.EbbinghausApp.models.FileInfo;
import ru.alex.project.EbbinghausApp.models.Session;
import ru.alex.project.EbbinghausApp.services.FilesStorageService;
import ru.alex.project.EbbinghausApp.services.SessionService;
import ru.alex.project.EbbinghausApp.util.FileExtensionExtractor;

@Controller
@RequestMapping("/files")
public class FileController {

	@Autowired
	FilesStorageService storageService;
	
	@Autowired
	SessionService sessionService;

	@GetMapping("/{id}/new")
	public String newFile(@PathVariable("id") int id, Model model) {
		model.addAttribute("id", id);
		return "files/upload_form";
	}

	@PostMapping("/{id}/upload")
	public String uploadFile(@PathVariable("id") int id, Model model, @RequestParam("file") MultipartFile file) {
		String message = "";
		try {
			storageService.save(file, getPathStr(id));

			message = "Uploaded the file successfully: " + file.getOriginalFilename();
			model.addAttribute("message", message);
	    } catch (Exception e) {
	    	message = "Could not upload the file: " + file.getOriginalFilename() + ". Error: " + e.getMessage();
	    	model.addAttribute("message", message);
	    }
		model.addAttribute("id", id);
		
	    return "files/upload_form";
	}

	@GetMapping("/show/{id}")
	public String getListFiles(@PathVariable("id") int id, Model model) {
		List<FileInfo> fileInfos = storageService.loadAll(getPathStr(id)).map(path -> {
			String filename = path.getFileName().toString();
			String contentType = FileExtensionExtractor.getFileMIME(filename);
			String url = MvcUriComponentsBuilder
					.fromMethodName(FileController.class, "getFile", path.getFileName().toString(), id, "attachment", contentType).build().toString();
			return new FileInfo(filename, url);
	    }).collect(Collectors.toList());

		model.addAttribute("files", fileInfos);
		model.addAttribute("id", id);
		model.addAttribute("current", sessionService.getSessionsById(id));

	    return "files/filesInfo"; 
	   
	}
	
	@PostMapping("{id}/data")
	public String saveLink(@PathVariable("id") int id,@ModelAttribute("current") Session session, Model model) {
		Session newSession = sessionService.getSessionsById(id);
    	newSession.setData(session.getData());
    	sessionService.update(id, newSession);
	    return "redirect:/files/show/"+id;
	}

	@GetMapping("{id}/{filename:.+}")
	public ResponseEntity<Resource> getFile(@PathVariable String filename, @PathVariable("id") int id, String option, String contentType) {
		Resource file = storageService.load(filename, getPathStr(id));
		
		HttpHeaders headers = new HttpHeaders();
		headers.add(HttpHeaders.CONTENT_DISPOSITION, option+"; filename=\"" + file.getFilename() + "\"");
		headers.add("content-type", contentType);
		
	    return ResponseEntity.ok()
	        .headers(headers).body(file); // for download
	}
	

	@GetMapping("/{id}/open/{filename:.+}")
	public String openFile(@PathVariable("id") int id, @PathVariable String filename, Model model) {
		String url = MvcUriComponentsBuilder
				.fromMethodName(FileController.class, "getFile", filename, id, "inline", FileExtensionExtractor.getFileMIME(filename)).build().toString();
		
		FileInfo info =  new FileInfo(filename, url);
		
		model.addAttribute("file",info);
		model.addAttribute("MIME", FileExtensionExtractor.getFileMIME(filename));
		model.addAttribute("id", id);
		
		return "files/open";
	}
	
	@GetMapping("/{id}/delete/{filename:.+}")
	public String deleteFile(@PathVariable("id") int id, @PathVariable String filename, Model model, RedirectAttributes redirectAttributes) {
		try {
			boolean existed = storageService.delete(filename, getPathStr(id));

			if (existed) {
				redirectAttributes.addFlashAttribute("message", "Delete the file successfully: " + filename);
			} else {
				redirectAttributes.addFlashAttribute("message", "The file does not exist!");
			}
	    } catch (Exception e) {
	    	redirectAttributes.addFlashAttribute("message",
	    			"Could not delete the file: " + filename + ". Error: " + e.getMessage());
	    }

		return "redirect:/files/show/"+id;
	}
	
	private String getPathStr(int sessionId) {
		return sessionService.buildPathStr(sessionId);
	}
}
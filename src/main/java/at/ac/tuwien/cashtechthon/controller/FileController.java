package at.ac.tuwien.cashtechthon.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import at.ac.tuwien.cashtechthon.dtos.FileId;

@Controller
@RequestMapping("/files")
public class FileController extends AbstractController {

	@Override
	protected String getViewDir() {
		return "files";
	}

	@RequestMapping(method=RequestMethod.POST, consumes=MediaType.MULTIPART_FORM_DATA_VALUE, produces=MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> uploadFile(@RequestParam("file") MultipartFile file) {
//		FileUploadResponse response = new FileUploadResponse();
//		response.setFileId(1L);
//		return new ResponseEntity<>(response, HttpStatus.CREATED);
		return new ResponseEntity<>(HttpStatus.NOT_IMPLEMENTED);
	}
}

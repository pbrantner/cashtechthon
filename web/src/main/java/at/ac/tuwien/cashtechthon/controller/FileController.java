package at.ac.tuwien.cashtechthon.controller;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import at.ac.tuwien.cashtechthon.dtos.FileId;
import at.ac.tuwien.cashtechthon.service.IFileService;
import at.ac.tuwien.cashtechthon.service.ITransactionService;
import at.ac.tuwien.cashtechthon.service.exception.TransactionServiceException;

@Controller
@RequestMapping("/files")
public class FileController extends AbstractController {

	private IFileService fileService;
	private ITransactionService transactionService;
	
	@Autowired
	public FileController(IFileService fileService, ITransactionService transactionService) {
		this.fileService = fileService;
		this.transactionService = transactionService;
	}
	
	@Override
	protected String getViewDir() {
		return "files";
	}

	@RequestMapping(method=RequestMethod.POST, consumes=MediaType.MULTIPART_FORM_DATA_VALUE, produces=MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> uploadFile(@RequestParam("file") MultipartFile file) {
		System.out.println(file.getName());
		System.out.println(file.getOriginalFilename());
		
		try {
			FileId fileId = fileService.storeFile(file.getBytes());
			transactionService.importTransactions(fileId.getFileId());
			return new ResponseEntity<>(fileId, HttpStatus.CREATED);
		} catch (IOException e) {
			return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
		} catch (TransactionServiceException e) {
			return new ResponseEntity<>(e.getMessage(), HttpStatus.CONFLICT);
		}
	}
}

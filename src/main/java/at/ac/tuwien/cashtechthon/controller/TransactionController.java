package at.ac.tuwien.cashtechthon.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import at.ac.tuwien.cashtechthon.dtos.FileId;
import at.ac.tuwien.cashtechthon.service.ITransactionService;
import at.ac.tuwien.cashtechthon.service.exception.TransactionServiceException;

@Controller
@RequestMapping("/transactions")
public class TransactionController extends AbstractController {

	private ITransactionService transactionService;
	
	@Autowired
	public TransactionController(ITransactionService transactionService) {
		this.transactionService = transactionService;
	}
	
	@Override
	protected String getViewDir() {
		return "transactions";
	}
	
	@RequestMapping(method=RequestMethod.POST, consumes=MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> importTransactions(@RequestBody FileId fileId) {
		try {
			transactionService.importTransactions(fileId.getFileId());
			return new ResponseEntity<>(HttpStatus.CREATED);
		} catch (TransactionServiceException e) {
			return new ResponseEntity<>(HttpStatus.CONFLICT);
		}
	}
}

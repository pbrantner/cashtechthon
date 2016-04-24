package at.ac.tuwien.cashtechthon.controller;

import at.ac.tuwien.cashtechthon.dao.ITransactionDao;
import at.ac.tuwien.cashtechthon.domain.Transaction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import at.ac.tuwien.cashtechthon.dtos.FileId;
import at.ac.tuwien.cashtechthon.service.ITransactionService;
import at.ac.tuwien.cashtechthon.service.exception.TransactionServiceException;

@RestController
@RequestMapping("/transactions")
public class TransactionController extends AbstractRestController {

	private ITransactionService transactionService;
	private ITransactionDao transactionDao;
	
	@Autowired
	public TransactionController(ITransactionService transactionService,
								 ITransactionDao transactionDao) {
		this.transactionService = transactionService;
		this.transactionDao = transactionDao;
	}

	@RequestMapping(method=RequestMethod.POST, consumes=MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> importTransactions(@RequestBody FileId fileId) {
		try {
			transactionService.importTransactions(fileId.getFileId());
			return new ResponseEntity<>(HttpStatus.CREATED);
		} catch (TransactionServiceException e) {
			return new ResponseEntity<>(e.getMessage(), HttpStatus.CONFLICT);
		}
	}
}

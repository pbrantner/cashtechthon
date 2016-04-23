package at.ac.tuwien.cashtechthon.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import at.ac.tuwien.cashtechthon.dtos.FileId;

@Controller
@RequestMapping("/transactions")
public class TransactionController extends AbstractController {

	@Override
	protected String getViewDir() {
		return "transactions";
	}
	
	@RequestMapping(method=RequestMethod.POST, consumes=MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> importTransactions(@RequestBody FileId fileId) {
		// return status codes:
		// 202 if import has been started
		// 409 if file with fileId cannot be found
		// 409 if import has already been started for specified fileId
		return new ResponseEntity<>(HttpStatus.NOT_IMPLEMENTED);
	}
}

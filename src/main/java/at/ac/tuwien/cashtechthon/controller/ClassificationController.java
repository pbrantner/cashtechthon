package at.ac.tuwien.cashtechthon.controller;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import at.ac.tuwien.cashtechthon.dao.ICustomerDao;
import at.ac.tuwien.cashtechthon.dao.ITransactionDao;
import at.ac.tuwien.cashtechthon.domain.Transaction;
import at.ac.tuwien.cashtechthon.service.IMLService;
import at.ac.tuwien.cashtechthon.util.Constants;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import at.ac.tuwien.cashtechthon.dtos.ClassificationSummary;

@Controller
@RequestMapping("/classifications")
public class ClassificationController extends AbstractController {




	IMLService imlService;

	ITransactionDao transactionDao;


	@Autowired
	public ClassificationController(ITransactionDao transactionDao, IMLService imlService) {
		this.imlService = imlService;
		this.transactionDao = transactionDao;
	}


	@Override
	protected String getViewDir() {
		return "classifications";
	}

	@RequestMapping(method=RequestMethod.GET, produces={MediaType.APPLICATION_JSON_VALUE,MediaType.APPLICATION_OCTET_STREAM_VALUE})
	public ResponseEntity<?> getClassifications(@RequestParam("include") String[] include,
			@RequestParam("exclude") String[] exclude,
			@RequestParam("customers") Long[] customers,
			// not sure if LocalDateTime works here maybe necessary to resort to java.util.Date
			@RequestParam("from") LocalDateTime from,
			@RequestParam("till") LocalDateTime till,
			@RequestParam("format") String format) {




		List<Transaction> transactions = transactionDao.findByCustomerIdAndTransactionDateBetween(customers[0],from,till);

		imlService.setAPIKey(Constants.API_KEY);
		imlService.setAPIURL(Constants.API_URL);
		imlService.setDataSet(transactions);
		JSONObject result = imlService.getResult();

		//result.append()

		return new ResponseEntity<>(HttpStatus.NOT_IMPLEMENTED);
	}
	
	@RequestMapping(method=RequestMethod.GET, produces=MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody List<ClassificationSummary> getClassifiactionSummary(@RequestParam("include") String[] include,
			@RequestParam("exclude") String[] exclude,
			@RequestParam("from") LocalDateTime from,
			@RequestParam("till") LocalDateTime till) {


		List<Transaction> transactions = transactionDao.findByTransactionDateBetween(from,till);

		imlService.setAPIKey(Constants.API_KEY);
		imlService.setAPIURL(Constants.API_URL);
		imlService.setDataSet(transactions);
		imlService.getResult();



		return new ArrayList<>();
	}
}

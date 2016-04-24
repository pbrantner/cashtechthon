package at.ac.tuwien.cashtechthon.controller;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import at.ac.tuwien.cashtechthon.dao.ICustomerDao;
import at.ac.tuwien.cashtechthon.dao.ITransactionDao;
import at.ac.tuwien.cashtechthon.domain.Transaction;
import at.ac.tuwien.cashtechthon.dtos.ClassificationSummaryEntry;
import at.ac.tuwien.cashtechthon.service.IMLService;
import at.ac.tuwien.cashtechthon.util.Constants;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
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
import org.springframework.web.context.request.WebRequest;

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
	public ResponseEntity<?> getClassifications(
			@RequestParam("include") Optional<String[]> include,
			@RequestParam("exclude") Optional<String[]> exclude,
			@RequestParam("customers") Optional<Long[]> customers,
			@DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
			@RequestParam("from") LocalDate from,
			@DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
			@RequestParam("till") LocalDate till,
			@RequestParam("format") Optional<String> format
			) {



		//Use format.orElse("json") for getting format
		return new ResponseEntity<>(HttpStatus.NOT_IMPLEMENTED);
	}

	@RequestMapping(path="/summary",method=RequestMethod.GET, produces=MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody ClassificationSummary getClassifiactionSummary(
			@RequestParam("include") Optional<String[]> include,
			@RequestParam("exclude") Optional<String[]> exclude,
			@DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
			@RequestParam("from") LocalDate from,
			@DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
			@RequestParam("till") LocalDate till) {

		ClassificationSummary cs = new ClassificationSummary();
		cs.setTransactionsTotal(1000);
		cs.setClassificationsTotal(18200);
		cs.setCustomersTotal(150);

		ClassificationSummaryEntry cse1 = new ClassificationSummaryEntry();
		cse1.setName("bauen");
		cse1.setTransactions(3200);
		cse1.setTransactionsPercentage(0.32);
		cse1.setCustomers(54);
		cse1.setCustomersPercentage(0.36);
		cs.getClassifications().add(cse1);

		ClassificationSummaryEntry cse2 = new ClassificationSummaryEntry();
		cse2.setName("mode");
		cse2.setTransactions(5000);
		cse2.setTransactionsPercentage(0.5);
		cse2.setCustomers(109);
		cse2.setCustomersPercentage(0.73);
		cs.getClassifications().add(cse2);

		ClassificationSummaryEntry cse3 = new ClassificationSummaryEntry();
		cse3.setName("sparen");
		cse3.setTransactions(10000);
		cse3.setTransactionsPercentage(1);
		cse3.setCustomers(67);
		cse3.setCustomersPercentage(0.45);
		cs.getClassifications().add(cse3);

		return cs;
	}
}

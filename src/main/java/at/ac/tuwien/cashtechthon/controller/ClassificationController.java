package at.ac.tuwien.cashtechthon.controller;

import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import at.ac.tuwien.cashtechthon.controller.exception.CSVGenerationException;
import at.ac.tuwien.cashtechthon.dtos.Classification;
import at.ac.tuwien.cashtechthon.dtos.ClassificationSummary;
import at.ac.tuwien.cashtechthon.dtos.ClassificationSummaryEntry;
import at.ac.tuwien.cashtechthon.service.IClassificationService;
import at.ac.tuwien.cashtechthon.util.ConverterUtil;

@Controller
@RequestMapping("/classifications")
public class ClassificationController extends AbstractController {

	private IClassificationService classificationService;

	@Autowired
	public ClassificationController(IClassificationService classificationService) {
		this.classificationService = classificationService;
	}

	@Override
	protected String getViewDir() {
		return "classifications";
	}

	@RequestMapping(method=RequestMethod.GET, produces={MediaType.APPLICATION_JSON_VALUE,"text/csv"})
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

		//		imlService.setAPIKey(Constants.API_KEY);
		//imlService.setDataSet();
		//		imlService.getResult();
		List<Classification> classifications;
		if(customers.isPresent()) {
			classifications = classificationService.getClassifications(customers.get(), from, till);
		} else {
			classifications = classificationService.getClassifications(from, till);
		}

		//Use format.orElse("json") for getting format
		String responseFormat = format.orElse("json");		
		ResponseEntity<?> response;
		switch(responseFormat) {
		case "csv":
			try {
				response = new ResponseEntity<>(ConverterUtil.convertClassficiationsToCsv(classifications), HttpStatus.OK);
			} catch (IOException e) {
				throw new CSVGenerationException(e);
			}
			break;
		case "json":
		default:
			response = new ResponseEntity<>(classifications, HttpStatus.OK);
		}
		Classification mockClassification = new Classification();
		mockClassification.setCustomerId(1L);
		mockClassification.setFirstName("Max");
		mockClassification.setLastName("Mustermann");
		mockClassification.setClassifications(new ArrayList<String>(){{add("bauen"); add("mode");add("sparen");}});
		// TODO remove later on
//		classifications = new ArrayList<>();
//		classifications.add(mockClassification);
//		response = new ResponseEntity<>(classifications, HttpStatus.OK); 
		return response;
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
		
//		return cs;
		
		return classificationService.getClassificationSummary(from, till);
	}
}

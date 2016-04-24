package at.ac.tuwien.cashtechthon.controller;

import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpHeaders;
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
import at.ac.tuwien.cashtechthon.service.IClassificationService;
import at.ac.tuwien.cashtechthon.util.ConverterUtil;

@Controller
@RequestMapping("/classifications")
public class ClassificationController extends AbstractController {

	@Autowired
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
		Classification classification;
		if(customers.isPresent()) {
			classification = classificationService.getClassification(customers.get(), from, till);
		} else {
			classification = classificationService.getClassification(from, till);
		}
		//Use format.orElse("json") for getting format
		String responseFormat = format.orElse("json");		
		ResponseEntity<?> response;
		switch(responseFormat) {
		case "csv":
			try {
				response = new ResponseEntity<>(ConverterUtil.convertClassficiationToCsv(classification), HttpStatus.OK);
			} catch (IOException e) {
				throw new CSVGenerationException(e);
			}
			break;
		case "json":
		default:
			response = new ResponseEntity<>(classification, HttpStatus.OK);
		}
		return response;
	}

	@RequestMapping(path="/summary",method=RequestMethod.GET, produces=MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody List<ClassificationSummary> getClassifiactionSummary(
			@RequestParam("include") Optional<String[]> include,
			@RequestParam("exclude") Optional<String[]> exclude,
			@DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
			@RequestParam("from") LocalDate from,
			@DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
			@RequestParam("till") LocalDate till) {
		return new ArrayList<>();
	}
}

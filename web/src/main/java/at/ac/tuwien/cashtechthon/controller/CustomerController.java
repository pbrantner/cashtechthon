package at.ac.tuwien.cashtechthon.controller;

import at.ac.tuwien.cashtechthon.dao.IClassificationDao;
import at.ac.tuwien.cashtechthon.dao.IEventDao;
import at.ac.tuwien.cashtechthon.dao.ITransactionDao;
import at.ac.tuwien.cashtechthon.domain.Classification;
import at.ac.tuwien.cashtechthon.domain.Customer;
import at.ac.tuwien.cashtechthon.domain.Event;
import at.ac.tuwien.cashtechthon.domain.Transaction;
import at.ac.tuwien.cashtechthon.dtos.*;
import at.ac.tuwien.cashtechthon.service.IClassificationService;
import at.ac.tuwien.shared.dtos.Gender;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import at.ac.tuwien.cashtechthon.controller.exception.CustomerNotFoundException;
import at.ac.tuwien.cashtechthon.dao.ICustomerDao;

import java.math.BigDecimal;
import javax.annotation.PostConstruct;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collector;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/customers")
public class CustomerController extends AbstractRestController {

	private Log logger = LogFactory.getLog(CustomerController.class);

	private ICustomerDao customerDao;
	private ITransactionDao transactionDao;
	private IClassificationDao classificationDao;
	private IClassificationService classificationService;
	private IEventDao eventDao;

	@Autowired
	public CustomerController(ICustomerDao customerDao,
							  ITransactionDao transactionDao,
							  IClassificationDao classificationDao,
							  IClassificationService classificationService,
							  IEventDao eventDao) {
		this.customerDao = customerDao;
		this.transactionDao = transactionDao;
		this.classificationDao = classificationDao;
		this.classificationService = classificationService;
		this.eventDao = eventDao;
	}

	@RequestMapping(method=RequestMethod.GET, produces=MediaType.APPLICATION_JSON_VALUE)
	public Page<Customer> getCustomers(Pageable pageable) {
		return customerDao.findAll(pageable);
	}

	@RequestMapping(method=RequestMethod.GET, value="/{customerId}", produces=MediaType.APPLICATION_JSON_VALUE)
	public Customer getCustomer(@PathVariable("customerId") Customer customer) {
		if(customer == null) {
			throw new CustomerNotFoundException();
		}

		List<String> xs = new ArrayList<>();
		List<Integer> ys = new ArrayList<>();

		xs.stream().filter(c -> !ys.stream().map(d -> d.toString()).collect(Collectors.toList()).contains(c));

		return customer;
	}

	@RequestMapping(value="/{customerId}/companies", method=RequestMethod.GET, produces=MediaType.APPLICATION_JSON_VALUE)
	public Page<Transaction> findCompaniesByCustomer(Pageable pageable, @PathVariable("customerId") Long customerId) {
		return transactionDao.findDistinctCompanyByCustomerId(customerId, pageable);
	}

	@RequestMapping(value="/{customerId}/classifications", method=RequestMethod.GET, produces=MediaType.APPLICATION_JSON_VALUE)
	public Page<Classification> findTransactionsByCustomer(Pageable pageable, @PathVariable("customerId") Customer customer) {
		if(customer == null) {
			throw new CustomerNotFoundException();
		}

		Page<Classification> classifications = classificationDao.findByCustomer(customer, pageable);
		return classifications;
	}

	@RequestMapping(value = "/{customerId}/classifications/comparison", method = RequestMethod.GET)
	public ReportResponse getClassifications(@PathVariable("customerId") Long customerId,
										  @RequestParam(value = "sex", required = false) String sex,
										  @RequestParam(value = "age", required = false) Long age,
										  @RequestParam(value = "incomeFrom", required = false) BigDecimal incomeFrom,
										  @RequestParam(value = "incomeTill", required = false) BigDecimal incomeTo,
										  @RequestParam(value = "location", required = false) String location,
										  @RequestParam(value = "ageFrom", required = false) Integer ageFrom,
										  @RequestParam(value = "ageTill", required = false) Integer ageTill) {
		LocalDate firstDayOfYear = LocalDate.now().withDayOfYear(1);
		LocalDate now = LocalDate.now();

		ReportResponse resp = new ReportResponse();

        /* CUSTOMER */
		resp.setCustomer(new CustomerReport());
		resp.getCustomer().getHeaders().add("Class");
		resp.getCustomer().getHeaders().add("Money");

		List<GroupedClassification> customerGroups = classificationDao.findClassificationsByCustomer(customerId);
		customerGroups.forEach(c -> {
			Object[] d = new Object[2];
			d[0] = c.getClassification();
			d[1] = c.getNrClassifications();
			resp.getCustomer().getData().add(d);
		});

        /* GROUP */
		resp.setGroup(new CustomerReport());
		resp.getGroup().getHeaders().add("Class");
		resp.getGroup().getHeaders().add("Money");

		Gender gender = null;
		// check for optional parameters
		if (sex != null) {
			gender = Gender.valueOf(StringUtils.capitalize(sex));
		}

		List<GroupedClassification> groupGroups = classificationDao.findClassificationsByProperties(incomeFrom,
				incomeTo, gender, location, customerId, ageFrom, ageTill);
		groupGroups.forEach(g -> {
			Object[] d = new Object[2];
			d[0] = g.getClassification();
			d[1] = g.getNrClassifications();
			resp.getGroup().getData().add(d);
		});

		return resp;
	}

	@RequestMapping(value="/{customerId}/events", method = RequestMethod.GET)
	public List<Event> getCustomerEvents(@PathVariable("customerId") Customer customer) {
		return eventDao.findByCustomer(customer);
	}

	@RequestMapping(value = "/events/{eventId}", method = RequestMethod.DELETE)
	public ResponseEntity<?> deleteEvent(@PathVariable("eventId") long eventId) {
		eventDao.delete(eventId);
		return new ResponseEntity<>(HttpStatus.OK);
	}

	@RequestMapping(value="/{customerId}/comparison", method=RequestMethod.GET, produces=MediaType.APPLICATION_JSON_VALUE)
	public ComparisonResponse getCustomerComparison(@PathVariable("customerId") Long customerId,
													@RequestParam(value = "sex", required = false) String sex,
													@RequestParam(value = "age", required = false) Long age,
													@RequestParam(value = "incomeFrom", required = false) BigDecimal incomeFrom,
													@RequestParam(value = "incomeTill", required = false) BigDecimal incomeTo,
													@RequestParam(value = "location", required = false) String location,
													@RequestParam(value = "ageFrom", required = false) Integer ageFrom,
													@RequestParam(value = "ageTill", required = false) Integer ageTill){
		ComparisonResponse resp = new ComparisonResponse();
		resp.setStart("2015-11"); // TODO REPLACE THIS
		resp.setEnd("2016-02"); // TODO REPLACE THIS
		resp.setColumns(Arrays.asList("Month", "Customer", "Group"));

		Gender gender = null;
		// check for optional parameters
		if (sex != null) {
			gender = Gender.valueOf(StringUtils.capitalize(sex));
		}

		List<MonthClassification> classificationsCustomer = classificationDao.findComparisonByClassificationAndCustomer("Education", customerId);
		List<MonthClassification> classificationsGroup = classificationDao.findComparisonByClassificationAndGroup("Trash", customerId, incomeFrom,
				incomeTo, gender, location, ageFrom, ageTill);

		List<List<Object>> data = new ArrayList<>();

		for (MonthClassification classification : classificationsCustomer) {
			data.add(Arrays.asList(classification.getDateTime().toInstant(ZoneOffset.UTC).toEpochMilli(), null, classification.getTotal()));
		}
		for (MonthClassification classification : classificationsGroup) {
			data.add(Arrays.asList(classification.getDateTime().toInstant(ZoneOffset.UTC).toEpochMilli(), classification.getTotal(), null));
		}
		resp.setData(data);

		return resp;
	}
}

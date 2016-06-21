package at.ac.tuwien.cashtechthon.controller;

import at.ac.tuwien.cashtechthon.dao.IClassificationDao;
import at.ac.tuwien.cashtechthon.dao.ITransactionDao;
import at.ac.tuwien.cashtechthon.domain.Classification;
import at.ac.tuwien.cashtechthon.domain.Transaction;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import at.ac.tuwien.cashtechthon.dtos.ComparisonResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import at.ac.tuwien.cashtechthon.controller.exception.CustomerNotFoundException;
import at.ac.tuwien.cashtechthon.dao.ICustomerDao;
import at.ac.tuwien.cashtechthon.domain.Customer;

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

	@Autowired
	public CustomerController(ICustomerDao customerDao,
							  ITransactionDao transactionDao,
							  IClassificationDao classificationDao) {
		this.customerDao = customerDao;
		this.transactionDao = transactionDao;
		this.classificationDao = classificationDao;
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

		Page<Classification> classifications = classificationDao.findByCustomerId(customer.getId(), pageable);
		return classifications;
	}

	@RequestMapping(value="/{customerId}/comparison", method=RequestMethod.GET, produces=MediaType.APPLICATION_JSON_VALUE)
	public ComparisonResponse getCustomerComparison(){
		ComparisonResponse resp = new ComparisonResponse();
		resp.setStart("2015-11"); // TODO REPLACE THIS
		resp.setEnd("2016-02"); // TODO REPLACE THIS
		resp.setColumns(Arrays.asList("Month", "Customer", "Group"));

		// TODO REPLACE THIS
		List<List<Object>> data = new ArrayList<>();
		LocalDateTime date = LocalDateTime.now();
		data.add(Arrays.asList(date.toInstant(ZoneOffset.UTC).toEpochMilli(), 630.77d, 722.1d));
		date = date.withMonth(7);
		data.add(Arrays.asList(date.toInstant(ZoneOffset.UTC).toEpochMilli(), 1050.23d, 1802.12d));
		date = date.withMonth(8);
		data.add(Arrays.asList(date.toInstant(ZoneOffset.UTC).toEpochMilli(), 303.55d , 280.78d));
		date = date.withMonth(9);
		data.add(Arrays.asList(date.toInstant(ZoneOffset.UTC).toEpochMilli(), 405.20d, 480.10d));
		resp.setData(data);

		return resp;
	}
}

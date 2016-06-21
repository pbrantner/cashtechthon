package at.ac.tuwien.cashtechthon.controller;

import at.ac.tuwien.cashtechthon.dao.IClassificationDao;
import at.ac.tuwien.cashtechthon.dao.ITransactionDao;
import at.ac.tuwien.cashtechthon.domain.Classification;
import at.ac.tuwien.cashtechthon.domain.Transaction;
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

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collector;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/customers")
public class CustomerController extends AbstractRestController {

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

		Page<Classification> classifications = classificationDao.findByCustomer(customer, pageable);
		return classifications;
	}
}

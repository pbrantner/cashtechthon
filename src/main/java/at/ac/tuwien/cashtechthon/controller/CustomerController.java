package at.ac.tuwien.cashtechthon.controller;

import at.ac.tuwien.cashtechthon.dao.ICustomerDao;
import at.ac.tuwien.cashtechthon.domain.Customer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/customers")
public class CustomerController extends AbstractRestController {

	private ICustomerDao customerDao;

	@Autowired
	public CustomerController(ICustomerDao customerDao) {
		this.customerDao = customerDao;
	}

	@RequestMapping(method=RequestMethod.GET, produces=MediaType.APPLICATION_JSON_VALUE)
	public Page<Customer> getCustomers(Pageable pageable) {
		return customerDao.findAll(pageable);
	}

	@RequestMapping(method=RequestMethod.GET, value="/{customerId}", produces=MediaType.APPLICATION_JSON_VALUE)
	public Customer getCustomer(@PathVariable("customerId") Customer customer) {
		return customer;
	}
}

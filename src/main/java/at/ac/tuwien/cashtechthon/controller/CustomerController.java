package at.ac.tuwien.cashtechthon.controller;

import javax.websocket.server.PathParam;

import at.ac.tuwien.cashtechthon.dao.ICustomerDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import at.ac.tuwien.cashtechthon.dtos.CustomerData;

@Controller
@RequestMapping("/customers")
public class CustomerController extends AbstractController {

	private ICustomerDao customerDao;

	@Autowired
	public CustomerController(ICustomerDao customerDao) {
		this.customerDao = customerDao;
	}

	@Override
	protected String getViewDir() {
		return "customer";
	}

	@RequestMapping(method=RequestMethod.GET, produces=MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> getCustomers(Pageable pageable) {
		//return new ResponseEntity(customerDao.findAll(pageable));

		return new ResponseEntity<>(HttpStatus.NOT_IMPLEMENTED);
	}
	
	@RequestMapping(method=RequestMethod.GET, path="{customerId}", produces=MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody CustomerData getCustomer(@PathParam("customerId") Long customerId) {
		return new CustomerData();
	}
}

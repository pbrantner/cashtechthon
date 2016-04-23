package at.ac.tuwien.cashtechthon.controller;

import javax.websocket.server.PathParam;

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

	@Override
	protected String getViewDir() {
		return "customer";
	}

	@RequestMapping(method=RequestMethod.GET, produces=MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> getCustomers(@RequestParam("offset") Integer offset, @RequestParam("limit") Integer limit) {
		// ?offset=:offset&limit=:limit
		// return new ResponseEntity(new ArrayList<CustomerData>());
		return new ResponseEntity<>(HttpStatus.NOT_IMPLEMENTED);
	}
	
	@RequestMapping(method=RequestMethod.GET, path="customers/:customerId", produces=MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody CustomerData getCustomer(@PathParam("customerId") Long customerId) {
		return new CustomerData();
	}
}

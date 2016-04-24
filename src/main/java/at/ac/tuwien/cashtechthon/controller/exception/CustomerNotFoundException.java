package at.ac.tuwien.cashtechthon.controller.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value=HttpStatus.CONFLICT, reason="No such customer entity")
public class CustomerNotFoundException extends RuntimeException {
	private static final long serialVersionUID = 1L;
	
	public CustomerNotFoundException() {
		super();
	}
}

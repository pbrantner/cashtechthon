package at.ac.tuwien.cashtechthon.service.exception;

public class ServiceException extends Exception {
	private static final long serialVersionUID = 1L;
	
	public ServiceException(String message) {
		super(message);
	}
	
	public ServiceException(String message, Throwable cause) {
		super(message, cause);
	}
}

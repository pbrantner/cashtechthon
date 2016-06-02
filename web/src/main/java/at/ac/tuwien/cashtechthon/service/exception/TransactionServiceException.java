package at.ac.tuwien.cashtechthon.service.exception;

public class TransactionServiceException extends ServiceException {
	private static final long serialVersionUID = 1L;

	public TransactionServiceException(String message) {
		super(message);
	}
	
	public TransactionServiceException(String message, Throwable cause) {
		super(message, cause);
	}
}

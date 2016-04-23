package at.ac.tuwien.cashtechthon.service.importer.exception;

public class TransactionImporterException extends Exception {
	private static final long serialVersionUID = 1L;
	
	public TransactionImporterException(String message) {
		super(message);
	}
	
	public TransactionImporterException(String message, Throwable cause) {
		super(message, cause);
	}
}

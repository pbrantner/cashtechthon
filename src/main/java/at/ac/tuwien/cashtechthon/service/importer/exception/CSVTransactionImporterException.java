package at.ac.tuwien.cashtechthon.service.importer.exception;

public class CSVTransactionImporterException extends TransactionImporterException {
	private static final long serialVersionUID = 1L;
	
	public CSVTransactionImporterException(String message) {
		super(message);
	}
	
	public CSVTransactionImporterException(String message, Throwable cause) {
		super(message, cause);
	}
}

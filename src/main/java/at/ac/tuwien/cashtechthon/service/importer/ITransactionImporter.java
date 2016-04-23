package at.ac.tuwien.cashtechthon.service.importer;

import at.ac.tuwien.cashtechthon.domain.Customer;
import at.ac.tuwien.cashtechthon.service.importer.exception.TransactionImporterException;

public interface ITransactionImporter {
	void importTransactions() throws TransactionImporterException;
	void importTransactionsForCustomer(Customer customer) throws TransactionImporterException;
}

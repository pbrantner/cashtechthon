package at.ac.tuwien.cashtechthon.service.importer;

import java.util.Iterator;

import at.ac.tuwien.cashtechthon.domain.Transaction;

public interface ITransactionImporter {
	void importTransactions(Iterator<Transaction> transactions);
}

package at.ac.tuwien.cashtechthon.service.importer;

import java.util.Iterator;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import at.ac.tuwien.cashtechthon.dao.ITransactionDao;
import at.ac.tuwien.cashtechthon.domain.Transaction;

@Component
public class TransactionImporter implements ITransactionImporter {

	private ITransactionDao transactionDao;

	@Autowired
	public TransactionImporter(ITransactionDao transactionDao) {
		this.transactionDao = transactionDao;
	}
	@Transactional(rollbackOn = Exception.class)
	@Override
	public void importTransactions(Iterator<Transaction> transactions) {
		while(transactions.hasNext()) {
			transactionDao.save(transactions.next());
		} 
	}
}

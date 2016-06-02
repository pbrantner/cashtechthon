package at.ac.tuwien.cashtechthon.service;

import at.ac.tuwien.cashtechthon.service.exception.TransactionServiceException;

public interface ITransactionService {
	void importTransactions(Long fileId) throws TransactionServiceException;
}

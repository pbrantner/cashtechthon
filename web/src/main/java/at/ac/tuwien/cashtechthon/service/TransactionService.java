package at.ac.tuwien.cashtechthon.service;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import at.ac.tuwien.cashtechthon.dao.IFileDao;
import at.ac.tuwien.cashtechthon.domain.File;
import at.ac.tuwien.cashtechthon.service.exception.TransactionServiceException;
import at.ac.tuwien.cashtechthon.service.importer.CSVTransactionResource;
import at.ac.tuwien.cashtechthon.service.importer.ITransactionImporter;

@Component
public class TransactionService implements ITransactionService {

	private IFileDao fileDao;
	private ITransactionImporter transactionImporter;
	
	@Autowired
	public TransactionService(IFileDao fileDao, ITransactionImporter transactionImporter) {
		this.fileDao = fileDao;
		this.transactionImporter = transactionImporter;
	}
	
	@Override
	public void importTransactions(Long fileId) throws TransactionServiceException {
		File file = fileDao.findOne(fileId);
		if(file == null) {
			throw new TransactionServiceException("File with id " + fileId + " not found!");
		}
		CSVTransactionResource csvResource;
		try {
			csvResource = new CSVTransactionResource(file.getContent());
		} catch (IOException e) {
			throw new TransactionServiceException("Error while initializing csv resource!", e);
		}
		transactionImporter.importTransactions(csvResource);
	}
}

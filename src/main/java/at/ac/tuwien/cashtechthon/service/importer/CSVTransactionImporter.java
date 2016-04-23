package at.ac.tuwien.cashtechthon.service.importer;

import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.nio.charset.Charset;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Iterator;

import javax.transaction.Transactional;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import at.ac.tuwien.cashtechthon.dao.ITransactionDao;
import at.ac.tuwien.cashtechthon.domain.Currency;
import at.ac.tuwien.cashtechthon.domain.Customer;
import at.ac.tuwien.cashtechthon.domain.Direction;
import at.ac.tuwien.cashtechthon.domain.Transaction;
import at.ac.tuwien.cashtechthon.service.importer.exception.CSVTransactionImporterException;
import at.ac.tuwien.cashtechthon.service.importer.exception.TransactionImporterException;

@Component
public class CSVTransactionImporter implements ITransactionImporter {

	private String filePath;
	private ITransactionDao transactionDao;
	
	@Autowired
	public CSVTransactionImporter(ITransactionDao transactionDao) {
		this.transactionDao = transactionDao;
	}

	public void setFilePath(String filePath) {
		this.filePath = filePath;
	}
	
	@Override
	public void importTransactions() throws TransactionImporterException {
		
	}

	@Transactional(rollbackOn = Exception.class)
	@Override
	public void importTransactionsForCustomer(Customer customer) throws TransactionImporterException {
		File csvData = new File(filePath);
		try {
			CSVParser parser = CSVParser.parse(csvData, Charset.forName("UTF-8"), CSVFormat.RFC4180);
			Iterator<CSVRecord> iter = parser.iterator();
			//skip header
			if(iter.hasNext()) {
				iter.next();
			}
			while(iter.hasNext()) {
				try {
					CSVRecord record = iter.next();
					Long transactionId = Long.parseLong(record.get(0));
					Long customerId = Long.parseLong(record.get(1));
					if(!customerId.equals(customer.getId())) {
						throw new TransactionImporterException("Record number " + record.getRecordNumber() + " contains an unknown customer id " + customerId);
					}
					LocalDateTime timestamp = LocalDateTime.parse(record.get(4), DateTimeFormatter.ISO_DATE_TIME);
					Direction direction = Direction.valueOf(record.get(5));
					String iban = record.get(6);
					String description = record.get(7);
					BigDecimal amount = new BigDecimal(record.get(8));
					Currency currency = Currency.valueOf(record.get(9));
					
					Transaction transaction = new Transaction();
					transaction.setId(transactionId);
					transaction.setTransactionDate(timestamp);
					transaction.setDirection(direction);
					transaction.setIban(iban);
					transaction.setDescription(description);
					transaction.setAmount(amount);
					transaction.setCurrency(currency);
					transaction.setCustomer(customer);
					transactionDao.save(transaction);
				} catch(DateTimeParseException | IllegalArgumentException e) {
					throw new CSVTransactionImporterException("An error occurred while parsing csv data", e);
				}
			}
		} catch (IOException e) {
			throw new CSVTransactionImporterException("Could not parse csv data", e);
		}
	}
}

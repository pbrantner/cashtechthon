package at.ac.tuwien.cashtechthon.service.importer;

import java.io.IOException;
import java.math.BigDecimal;
import java.nio.charset.Charset;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Iterator;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;

import at.ac.tuwien.cashtechthon.domain.Currency;
import at.ac.tuwien.cashtechthon.domain.Direction;
import at.ac.tuwien.cashtechthon.domain.Transaction;

public class CSVTransactionResource implements Iterator<Transaction>{
	private CSVParser parser;
	private Iterator<CSVRecord> iterator;
	public CSVTransactionResource(byte[] content) throws IOException {
		parser = CSVParser.parse(byteArrayToString(content), CSVFormat.RFC4180);
		iterator = parser.iterator();
		//skip header
		if(iterator.hasNext()) {
			iterator.next();
		}
	}
	
	private String byteArrayToString(byte[] content) {
		return new String(content, Charset.forName("UTF-8"));
	}
	
	@Override
	public boolean hasNext() {
		return iterator.hasNext();
	}

	@Override
	public Transaction next() {
		CSVRecord record = iterator.next();
		Long transactionId = Long.parseLong(record.get(0));
		Long customerId = Long.parseLong(record.get(1));
		LocalDateTime timestamp = LocalDateTime.parse(record.get(4), DateTimeFormatter.ISO_DATE_TIME);
		String company = record.get(5);
		Direction direction = Direction.valueOf(record.get(6));
		String iban = record.get(7);
		String description = record.get(8);
		BigDecimal amount = new BigDecimal(record.get(9));
		Currency currency = Currency.valueOf(record.get(10));
		
		Transaction transaction = new Transaction();
		transaction.setId(transactionId);
		transaction.setCustomerId(customerId);
		transaction.setTransactionDate(timestamp);
		transaction.setCompany(company);
		transaction.setDirection(direction);
		transaction.setIban(iban);
		transaction.setDescription(description);
		transaction.setAmount(amount);
		transaction.setCurrency(currency);
		return transaction;
	}
}

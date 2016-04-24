package at.ac.tuwien.cashtechthon.service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import at.ac.tuwien.cashtechthon.dao.ITransactionDao;
import at.ac.tuwien.cashtechthon.domain.Transaction;
import at.ac.tuwien.cashtechthon.dtos.Classification;
import at.ac.tuwien.cashtechthon.dtos.ClassificationSummary;

@Component
public class ClassificationService implements IClassificationService {

	private IMLService mlService;
	private ITransactionDao transactionDao;
	
	@Autowired
	public ClassificationService(IMLService mlService, ITransactionDao transactionDao) {
		this.mlService = mlService;
		this.transactionDao = transactionDao;
	}
	
	@Override
	public List<Classification> getClassifications(LocalDate from, LocalDate till) {
		LocalDateTime fromTime = LocalDateTime.of(from, LocalTime.MIN);
		LocalDateTime tillTime = LocalDateTime.of(till, LocalTime.MAX);
		return getClassifications(transactionDao.findByTransactionDateBetween(fromTime, tillTime));
	}
	
	@Override
	public List<Classification> getClassifications(Long[] customerIds, LocalDate from, LocalDate till) {
		LocalDateTime fromTime = LocalDateTime.of(from, LocalTime.MIN);
		LocalDateTime tillTime = LocalDateTime.of(till, LocalTime.MAX);
		if(customerIds == null || customerIds.length == 0) {
			return getClassifications(transactionDao.findByTransactionDateBetween(fromTime, tillTime));
		} else {
			return getClassifications(transactionDao.findByCustomerIdInAndTransactionDateBetween(Arrays.asList(customerIds), fromTime, tillTime));
		}
	}
	
	private List<Classification> getClassifications(List<Transaction> transactions) {
		//TODO call mlService with transactions
		return null;
	}

	@Override
	public ClassificationSummary getClassificationSummary(LocalDate from, LocalDate till) {
		LocalDateTime fromTime = LocalDateTime.of(from, LocalTime.MIN);
		LocalDateTime tillTime = LocalDateTime.of(till, LocalTime.MAX);
		List<Classification> classifications = getClassifications(transactionDao.findByTransactionDateBetween(fromTime, tillTime));
		// TODO calculate summary
		return null;
	}
}

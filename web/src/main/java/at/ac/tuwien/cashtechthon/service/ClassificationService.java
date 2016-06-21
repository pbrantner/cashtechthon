package at.ac.tuwien.cashtechthon.service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import at.ac.tuwien.cashtechthon.dao.IClassificationDao;
import at.ac.tuwien.cashtechthon.dao.ICustomerDao;
import at.ac.tuwien.cashtechthon.domain.Customer;
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
	private IClassificationDao classificationDao;
	private ICustomerDao customerDao;
	
	@Autowired
	public ClassificationService(IMLService mlService, ITransactionDao transactionDao,
								 IClassificationDao classificationDao,
								 ICustomerDao customerDao) {
		this.mlService = mlService;
		this.transactionDao = transactionDao;
		this.classificationDao = classificationDao;
		this.customerDao = customerDao;
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
		return mlService.getResultWithCustomers(transactions);

	}

	@Override
	public ClassificationSummary getClassificationSummary(LocalDate from, LocalDate till) {
		LocalDateTime fromTime = LocalDateTime.of(from, LocalTime.MIN);
		LocalDateTime tillTime = LocalDateTime.of(till, LocalTime.MAX);

		List<Transaction> trans = transactionDao.findByTransactionDateBetween(fromTime, tillTime);
		if(trans.size() < 1) {
			return new ClassificationSummary();
		} else {
			return mlService.getResult(trans);
		}
	}

	@Override
	public at.ac.tuwien.cashtechthon.domain.Classification save(at.ac.tuwien.shared.dtos.Classification classification) {
		return classificationDao.save(classificationDtoToClassification(classification));
	}

	private at.ac.tuwien.cashtechthon.domain.Classification classificationDtoToClassification(at.ac.tuwien.shared.dtos.Classification classificationDto) {
		at.ac.tuwien.cashtechthon.domain.Classification classification = new at.ac.tuwien.cashtechthon.domain.Classification();
		Customer customer = customerDao.findOne(classificationDto.getCustomer().getId());
		classification.setAmount(classificationDto.getAmount());
		classification.setClassificationDate(classificationDto.getClassificationDate());
		classification.setCurrency(classificationDto.getCurrency());

		return classification;
	}
}

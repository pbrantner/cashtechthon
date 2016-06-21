package at.ac.tuwien.cashtechthon.service;

import at.ac.tuwien.cashtechthon.cep.*;
import at.ac.tuwien.cashtechthon.cep.event.AccountBalanceEvent;
import at.ac.tuwien.cashtechthon.dao.ICustomerDao;
import at.ac.tuwien.cashtechthon.dao.IEventDao;
import at.ac.tuwien.cashtechthon.dao.IThresholdDao;
import at.ac.tuwien.cashtechthon.domain.Customer;
import at.ac.tuwien.cashtechthon.domain.Threshold;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Service
public class CustomerService implements ICustomerService {

    private ICustomerDao customerDao;
    private IThresholdDao thresholdDao;
    private IEventDao eventDao;
    private EventProcessor eventProcessor;

    @Autowired
    public CustomerService(ICustomerDao customerDao,
                           IThresholdDao thresholdDao, IEventDao eventDao) {
        this.customerDao = customerDao;
        this.thresholdDao = thresholdDao;
        this.eventDao = eventDao;

        eventProcessor = EventProcessor.getInstance();
    }

    @Override
    public Customer save(at.ac.tuwien.shared.dtos.Customer customerDto) {
        Customer customer = customerDao.save(customerDtoToCustomer(customerDto));
        persistThresholds(customer, customerDto.getThresholds());

        return customer;
    }

    /**
     * Persists all thresholds for a customer
     * @param customer
     * @param thresholds the threshold dtos
     */
    private void persistThresholds(Customer customer, List<at.ac.tuwien.shared.dtos.Threshold> thresholds) {
        List<Threshold> thresholdsToPersist = new ArrayList<>();
        for (at.ac.tuwien.shared.dtos.Threshold thresholdDto : thresholds) {
            Threshold threshold = thresholdDtoToThreshold(customer, thresholdDto);
            thresholdsToPersist.add(threshold);

            if (threshold.getWindowSize() == null || threshold.getWindowSize() == 0) {
                addAbsoluteThreshold(threshold, customer);
            } else {
                addRelativeThreshold(threshold, customer);
            }
        }
        thresholdDao.save(thresholdsToPersist);
    }

    private void addAbsoluteThreshold(Threshold threshold, Customer customer) {
        AbsoluteThresholdParameter atp = AbsoluteThresholdParameter.newInstance()
                .accountBalance(new AccountBalanceEvent(customer.getId(), new BigDecimal(0), threshold.getThresholdDate()))
                .thresholdInEur(threshold.getThreshold())
                .type("negative")
                .callback(new EventPersistingCallback(threshold, customer, eventDao))
                .build();
    }

    private void addRelativeThreshold(Threshold threshold, Customer customer) {
        RelativeThresholdParameter rtp = RelativeThresholdParameter.newInstance()
                .customerId(customer.getId())
                .thresholdInEur(threshold.getThreshold())
                .type("negative")
                .direction("unidirectional")
                .callback(new EventPersistingCallback(threshold, customer, eventDao))
                .windowSize(threshold.getWindowSize())
                .classification(threshold.getClassification())
                .build();

        eventProcessor.createRelativeThreshold(rtp);
    }

    private Threshold thresholdDtoToThreshold(Customer customer, at.ac.tuwien.shared.dtos.Threshold thresholdDto) {
        Threshold threshold = new Threshold();
        threshold.setClassification(thresholdDto.getClassification());
        threshold.setCustomer(customer);
        threshold.setThreshold(thresholdDto.getThreshold());
        threshold.setThresholdDate(thresholdDto.getThresholdDate());
        threshold.setWindowSize(thresholdDto.getWindowSize());
        return threshold;
    }

    private Customer customerDtoToCustomer(at.ac.tuwien.shared.dtos.Customer customerDto) {
        Customer customer = new Customer();
        customer.setFirstName(customerDto.getFirstname());
        customer.setLastName(customerDto.getLastname());
        customer.setDateOfBirth(customerDto.getDateOfBirth());
        customer.setGender(customerDto.getGender());
        customer.setLocation(customerDto.getLocation());
        return customer;
    }
}

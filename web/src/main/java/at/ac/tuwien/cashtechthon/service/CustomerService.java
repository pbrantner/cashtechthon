package at.ac.tuwien.cashtechthon.service;

import at.ac.tuwien.cashtechthon.dao.ICustomerDao;
import at.ac.tuwien.cashtechthon.dao.IThresholdDao;
import at.ac.tuwien.cashtechthon.domain.Customer;
import at.ac.tuwien.cashtechthon.domain.Threshold;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class CustomerService implements ICustomerService {

    private ICustomerDao customerDao;
    private IThresholdDao thresholdDao;

    @Autowired
    public CustomerService(ICustomerDao customerDao,
                           IThresholdDao thresholdDao) {
        this.customerDao = customerDao;
        this.thresholdDao = thresholdDao;
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
        }
        thresholdDao.save(thresholdsToPersist);
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

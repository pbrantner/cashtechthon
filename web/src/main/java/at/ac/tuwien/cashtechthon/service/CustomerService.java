package at.ac.tuwien.cashtechthon.service;

import at.ac.tuwien.cashtechthon.dao.ICustomerDao;
import at.ac.tuwien.cashtechthon.domain.Customer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CustomerService implements ICustomerService {

    private ICustomerDao customerDao;

    @Autowired
    public CustomerService(ICustomerDao customerDao) {
        this.customerDao = customerDao;
    }

    @Override
    public Customer save(at.ac.tuwien.shared.dtos.Customer customerDto) {
        Customer customer = customerDao.save(customerDtoToCustomer(customerDto));
        return customer;
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

package at.ac.tuwien.cashtechthon.service;

import at.ac.tuwien.cashtechthon.domain.Customer;

public interface ICustomerService {

    Customer save(at.ac.tuwien.shared.dtos.Customer customerDto);
}

package at.ac.tuwien.cashtechthon.dao;

import org.springframework.data.jpa.repository.JpaRepository;

import at.ac.tuwien.cashtechthon.domain.Customer;

public interface ICustomerDao extends JpaRepository<Customer, Long> {

}

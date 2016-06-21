package at.ac.tuwien.persistence;

import at.ac.tuwien.shared.dtos.Customer;

import java.util.List;

public interface CustomerPersistence {
    void save(List<Customer> c);
}

package at.ac.tuwien.persistence;

import at.ac.tuwien.shared.dtos.ExtendedCustomer;

import java.util.List;

public interface CustomerPersistence {
    void save(List<ExtendedCustomer> c);
}

package at.ac.tuwien.service;

import at.ac.tuwien.shared.dtos.Classification;
import at.ac.tuwien.shared.dtos.ExtendedCustomer;

import java.util.List;

public interface ClassificationGenerator {
    List<Classification> generate();
    List<ExtendedCustomer> getCustomers();
}

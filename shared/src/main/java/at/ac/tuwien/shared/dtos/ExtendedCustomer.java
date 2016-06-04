package at.ac.tuwien.shared.dtos;

import com.fasterxml.jackson.annotation.JsonIgnore;

import java.util.ArrayList;
import java.util.List;

public class ExtendedCustomer extends Customer {
    @JsonIgnore
    private List<Classification> defaultClassifications;

    public ExtendedCustomer() {
        defaultClassifications = new ArrayList<Classification>();
    }

    public List<Classification> getDefaultClassifications() {
        return defaultClassifications;
    }

    public void setDefaultClassifications(List<Classification> defaultClassifications) {
        this.defaultClassifications = defaultClassifications;
    }

    public void addDefaultClassification(Classification classification) {
        classification.setCustomer(this);
        defaultClassifications.add(classification);
    }
}

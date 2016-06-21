package at.ac.tuwien.cashtechthon.domain;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
public class Threshold {
    @Id
    @GeneratedValue
    private Long id;
    @ManyToOne
    private Customer customer;
    private BigDecimal threshold;
    private LocalDateTime thresholdDate;
    private Long windowSize;
    private String classification;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    public BigDecimal getThreshold() {
        return threshold;
    }

    public void setThreshold(BigDecimal threshold) {
        this.threshold = threshold;
    }

    public LocalDateTime getThresholdDate() {
        return thresholdDate;
    }

    public void setThresholdDate(LocalDateTime thresholdDate) {
        this.thresholdDate = thresholdDate;
    }

    public Long getWindowSize() {
        return windowSize;
    }

    public void setWindowSize(Long windowSize) {
        this.windowSize = windowSize;
    }

    public String getClassification() {
        return classification;
    }

    public void setClassification(String classification) {
        this.classification = classification;
    }

    @Override
    public String toString() {
        return String.format("threshold[id=%d,threshold=%f,classification=%s,customerId=%d]",
                id, threshold, classification, customer.getId());
    }
}

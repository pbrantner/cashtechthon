package at.ac.tuwien.cashtechthon.domain;

import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.*;

@Entity
public class Classification {

    @Id
    @GeneratedValue
    private Long id;
    private java.util.Currency currency;
    private BigDecimal amount;
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    private LocalDateTime classificationDate;
    @ManyToOne(cascade = CascadeType.MERGE, optional = false)
    private Customer customer;
    private String classification;

    public Classification(double value, Customer c, String clazz) {
        amount = new BigDecimal(value);
        customer = c;
        classification = clazz;
        currency = java.util.Currency.getInstance("EUR");
        classificationDate = LocalDateTime.now();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public java.util.Currency getCurrency() {
        return currency;
    }

    public void setCurrency(java.util.Currency currency) {
        this.currency = currency;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public LocalDateTime getClassificationDate() {
        return classificationDate;
    }

    public void setClassificationDate(LocalDateTime classificationDate) {
        this.classificationDate = classificationDate;
    }

    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    public String getClassification() {
        return classification;
    }

    public void setClassification(String classification) {
        this.classification = classification;
    }

    @Override
    public String toString() {
        return String.format("classification[id=%d,amount=%f,customerId=%d]", id, amount, customer.getId());
    }
}

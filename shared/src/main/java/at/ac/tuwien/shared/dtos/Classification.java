package at.ac.tuwien.shared.dtos;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Currency;

public class Classification {
    private Customer customer;
    private Currency currency;
    private BigDecimal amount;
    private String classification;
    private LocalDateTime classificationDate;

    public Classification() { this.currency = Currency.getInstance("EUR"); }
    public Classification(Customer customer, BigDecimal amount, String classification) {
        this();
        this.customer = customer;
        this.amount = amount;
        this.classification = classification;
    }

    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    public Currency getCurrency() {
        return currency;
    }

    public void setCurrency(Currency currency) {
        this.currency = currency;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public String getClassification() {
        return classification;
    }

    public void setClassification(String classification) {
        this.classification = classification;
    }

    public LocalDateTime getClassificationDate() {
        return classificationDate;
    }

    public void setClassificationDate(LocalDateTime classificationDate) {
        this.classificationDate = classificationDate;
    }

    @Override
    public String toString() {
        return "Classification{" +
                "customer=" + customer +
                ", currency=" + currency +
                ", amount=" + amount +
                ", classification='" + classification + '\'' +
                ", classificationDate=" + classificationDate +
                '}';
    }
}

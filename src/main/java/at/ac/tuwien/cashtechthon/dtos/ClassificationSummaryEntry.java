package at.ac.tuwien.cashtechthon.dtos;

public class ClassificationSummaryEntry {
    private String name;
    private long transactions;
    private double transactionsPercentage;
    private long customers;
    private double customersPercentage;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public long getTransactions() {
        return transactions;
    }

    public void setTransactions(long transactions) {
        this.transactions = transactions;
    }

    public double getTransactionsPercentage() {
        return transactionsPercentage;
    }

    public void setTransactionsPercentage(double transactionsPercentage) {
        this.transactionsPercentage = transactionsPercentage;
    }

    public long getCustomers() {
        return customers;
    }

    public void setCustomers(long customers) {
        this.customers = customers;
    }

    public double getCustomersPercentage() {
        return customersPercentage;
    }

    public void setCustomersPercentage(double customersPercentage) {
        this.customersPercentage = customersPercentage;
    }
}

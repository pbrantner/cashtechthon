package at.ac.tuwien.dtos;

import java.math.BigDecimal;

public class CustomerData {
    private int transactionCount;
    private BigDecimal balance;

    public CustomerData() {
        transactionCount = 0;
        balance = new BigDecimal(0);
    }

    public BigDecimal getBalance() {
        return balance;
    }

    public void setBalance(BigDecimal balance) {
        this.balance = balance;
    }

    public int getTransactionCount() {
        return transactionCount;
    }

    public void setTransactionCount(int transactionCount) {
        this.transactionCount = transactionCount;
    }
}

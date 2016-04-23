package at.ac.tuwien.cashtechthon;

import at.ac.tuwien.cashtechthon.dao.ICustomerDao;
import at.ac.tuwien.cashtechthon.dao.ITransactionDao;
import at.ac.tuwien.cashtechthon.domain.*;
import at.ac.tuwien.cashtechthon.domain.Currency;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

@Component
public class SampleDataSeeder implements ApplicationListener<ContextRefreshedEvent> {

    private ICustomerDao customerDao;
    private ITransactionDao transactionDao;

    private Customer customerOne;
    private Customer customerTwo;

    @Autowired
    public SampleDataSeeder(ICustomerDao customerDao,
                            ITransactionDao transactionDao) {
        this.customerDao = customerDao;
        this.transactionDao = transactionDao;
    }

    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {
        createCustomers();
        createTransactions();
    }

    public List<Customer> createCustomers() {
        List<Customer> customers = new ArrayList<>();

        customerOne = new Customer();
        customerOne.setFirstName("Max");
        customerOne.setLastName("Mustermann");
        customerOne = customerDao.save(customerOne);
        customers.add(customerOne);

        customerTwo = new Customer();
        customerTwo.setFirstName("Maria");
        customerTwo.setLastName("Mayer");
        customerTwo = customerDao.save(customerTwo);
        customers.add(customerTwo);

        return customers;
    }

    public List<Transaction> createTransactions() {
        List<Transaction> transactions = new ArrayList<>();

        Transaction transactionOne = new Transaction();
        transactionOne.setId(1L);
        transactionOne.setAmount(new BigDecimal("25.0"));
        transactionOne.setCurrency(Currency.EUR);
        transactionOne.setDescription("Billa");
        transactionOne.setDirection(Direction.OUTGOING);
        transactionOne.setIban("AT2342243424");
        transactionOne.setTransactionDate(LocalDateTime.parse("2016-04-23T16:31:31", DateTimeFormatter.ISO_DATE_TIME));
        transactionOne = transactionDao.save(transactionOne);
        transactions.add(transactionOne);

        Transaction transactionTwo = new Transaction();
        transactionTwo.setId(2L);
        transactionTwo.setAmount(new BigDecimal("125.0"));
        transactionTwo.setCurrency(Currency.EUR);
        transactionTwo.setDescription("Obi");
        transactionTwo.setDirection(Direction.OUTGOING);
        transactionTwo.setIban("AT23334222");
        transactionTwo.setTransactionDate(LocalDateTime.parse("2016-04-23T12:31:31", DateTimeFormatter.ISO_DATE_TIME));
        transactionTwo = transactionDao.save(transactionTwo);
        transactions.add(transactionTwo);

        return transactions;
    }
}

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
    private Customer customerThree;
    private Customer customerFour;

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

        customerThree = new Customer();
        customerThree.setFirstName("Anna");
        customerThree.setLastName("Berger");
        customerThree = customerDao.save(customerThree);
        customers.add(customerThree);


        customerFour = new Customer();
        customerFour.setFirstName("Felix");
        customerFour.setLastName("Jung");
        customerFour = customerDao.save(customerFour);
        customers.add(customerFour);

        return customers;
    }

    public List<Transaction> createTransactions() {
        List<Transaction> transactions = new ArrayList<>();

        Transaction transactionOne = new Transaction();
        transactionOne.setCustomerId(customerOne.getId());
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
        transactionTwo.setCustomerId(customerOne.getId());
        transactionTwo.setId(2L);
        transactionTwo.setAmount(new BigDecimal("125.0"));
        transactionTwo.setCurrency(Currency.EUR);
        transactionTwo.setDescription("Obi");
        transactionTwo.setDirection(Direction.OUTGOING);
        transactionTwo.setIban("AT23334222");
        transactionTwo.setTransactionDate(LocalDateTime.parse("2016-04-23T12:31:31", DateTimeFormatter.ISO_DATE_TIME));
        transactionTwo = transactionDao.save(transactionTwo);
        transactions.add(transactionTwo);

        Transaction transactionThree = new Transaction();
        transactionThree.setCustomerId(customerOne.getId());
        transactionThree.setId(3L);
        transactionThree.setAmount(new BigDecimal("34.95"));
        transactionThree.setCurrency(Currency.EUR);
        transactionThree.setDescription("Spar");
        transactionThree.setDirection(Direction.OUTGOING);
        transactionThree.setIban("AT13334222");
        transactionThree.setTransactionDate(LocalDateTime.parse("2016-04-22T09:00:31", DateTimeFormatter.ISO_DATE_TIME));
        transactionThree = transactionDao.save(transactionThree);
        transactions.add(transactionThree);

        Transaction transactionFour = new Transaction();
        transactionFour.setCustomerId(customerTwo.getId());
        transactionFour.setId(3L);
        transactionFour.setAmount(new BigDecimal("23.55"));
        transactionFour.setCurrency(Currency.EUR);
        transactionFour.setDescription("Spar");
        transactionFour.setDirection(Direction.OUTGOING);
        transactionFour.setIban("AT13334222");
        transactionFour.setTransactionDate(LocalDateTime.parse("2016-04-23T09:00:31", DateTimeFormatter.ISO_DATE_TIME));
        transactionFour = transactionDao.save(transactionFour);
        transactions.add(transactionFour);

        return transactions;
    }
}

package at.ac.tuwien.cashtechthon;

import at.ac.tuwien.cashtechthon.dao.IClassificationDao;
import at.ac.tuwien.cashtechthon.dao.ICustomerDao;
import at.ac.tuwien.cashtechthon.dao.ITransactionDao;
import at.ac.tuwien.cashtechthon.domain.*;
import at.ac.tuwien.cashtechthon.domain.Classification;
import at.ac.tuwien.cashtechthon.domain.Currency;
import at.ac.tuwien.cashtechthon.domain.Customer;
import at.ac.tuwien.shared.dtos.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

@Component
public class SampleDataSeeder implements ApplicationListener<ContextRefreshedEvent> {

    private ICustomerDao customerDao;
    private ITransactionDao transactionDao;
    private IClassificationDao classificationDao;

    private Customer customerOne;
    private Customer customerTwo;
    private Customer customerThree;
    private Customer customerFour;

    @Autowired
    public SampleDataSeeder(ICustomerDao customerDao,
                            ITransactionDao transactionDao,
                            IClassificationDao classificationDao) {
        this.customerDao = customerDao;
        this.transactionDao = transactionDao;
        this.classificationDao = classificationDao;
    }

    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {
        createCustomers();
        createClassifications();
        //createTransactions();
    }

    public List<Customer> createCustomers() {
        List<Customer> customers = new ArrayList<>();

        customerOne = new Customer();
        customerOne.setFirstName("Max");
        customerOne.setLastName("Mustermann");
        customerOne.setGender(Gender.Male);
        customerOne.setLocation("Vienna");
        customerOne.setDateOfBirth(LocalDate.of(1990, 6, 25));
        customerOne = customerDao.save(customerOne);
        customers.add(customerOne);

        customerTwo = new Customer();
        customerTwo.setFirstName("Maria");
        customerTwo.setLastName("Mayer");
        customerTwo.setGender(Gender.Female);
        customerTwo.setLocation("Linz");
        customerTwo.setDateOfBirth(LocalDate.of(2000, 6, 6));
        customerTwo = customerDao.save(customerTwo);
        customers.add(customerTwo);

        customerThree = new Customer();
        customerThree.setFirstName("Anna");
        customerThree.setLastName("Berger");
        customerThree.setGender(Gender.Female);
        customerThree.setDateOfBirth(LocalDate.of(2000, 6, 6));
        customerThree = customerDao.save(customerThree);
        customers.add(customerThree);


        customerFour = new Customer();
        customerFour.setFirstName("Felix");
        customerFour.setLastName("Jung");
        customerFour.setGender(Gender.Male);
        customerFour.setLocation("Vienna");
        customerFour.setDateOfBirth(LocalDate.of(1980, 1, 1));
        customerFour = customerDao.save(customerFour);
        customers.add(customerFour);

        return customers;
    }

    public void createClassifications() {
        List<Classification> list = new ArrayList<>();
        list.add(new Classification(-17.322, customerOne, "Miscellaneous"));
        list.add(new Classification(17.322, customerOne, "absoluteThresholdNegative"));
        list.add(new Classification(-17.322, customerOne, "absoluteThresholdPositive"));
        list.add(new Classification(200, customerOne, "absoluteThresholdPositive"));
        list.add(new Classification(-629.58, customerOne, "Travel"));
        list.add(new Classification(-26.016, customerOne, "Bars"));
        list.add(new Classification(-26.045, customerOne, "Health"));
        list.add(new Classification(-5.4043, customerOne, "Education", LocalDateTime.of(2016, 4, 25, 0, 0)));
        list.add(new Classification(-224.8511, customerOne, "Education", LocalDateTime.of(2016, 5, 25, 0, 0)));
        list.add(new Classification(-14.8511, customerOne, "Education", LocalDateTime.of(2016, 6, 25, 0, 0)));
        list.add(new Classification(3500, customerOne, "Income"));

        list.add(new Classification(-8.6855, customerTwo, "Groceries"));
        list.add(new Classification(8.6855, customerTwo, "absoluteThresholdNegative"));
        list.add(new Classification(-8.6855, customerTwo, "absoluteThresholdPositive"));
        list.add(new Classification(-200, customerTwo, "absoluteThresholdNegative"));
        list.add(new Classification(-26.723, customerTwo, "Cash"));
        list.add(new Classification(-13.201, customerTwo, "Restaurants"));
        list.add(new Classification(-17.098, customerTwo, "Miscellaneous"));
        list.add(new Classification(-629.16, customerTwo, "Travel"));
        list.add(new Classification(-23.637, customerTwo, "Cash"));
        list.add(new Classification(-5.4043, customerTwo, "Education"));
        list.add(new Classification(-4.8511, customerTwo, "Education"));
        list.add(new Classification(2500, customerTwo, "Income"));

        list.add(new Classification(-8.6855, customerFour, "Trash"));
        list.add(new Classification(8.6855, customerFour, "Trash"));
        list.add(new Classification(-8.6855, customerFour, "Trash"));
        list.add(new Classification(-200, customerFour, "Trash"));
        list.add(new Classification(-26.723, customerFour, "Trash"));
        list.add(new Classification(-13.201, customerFour, "Trash"));
        list.add(new Classification(-17.098, customerFour, "Trash"));
        list.add(new Classification(-629.16, customerFour, "Trash"));
        list.add(new Classification(-23.637, customerFour, "Trash"));
        list.add(new Classification(-5.4043, customerFour, "Trash", LocalDateTime.of(2016, 5, 23, 0, 0)));
        list.add(new Classification(-4.8511, customerFour, "Trash", LocalDateTime.of(2016, 6, 26, 0, 0)));
        list.add(new Classification(1500, customerFour, "Income"));

        for (Classification c : list) {
            classificationDao.save(c);
        }
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
        transactionOne.setCompany("Billa");
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
        transactionTwo.setCompany("Obi");
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
        transactionThree.setCompany("Spar");
        transactionThree = transactionDao.save(transactionThree);
        transactions.add(transactionThree);

        Transaction transactionFour = new Transaction();
        transactionFour.setCustomerId(customerTwo.getId());
        transactionFour.setId(4L);
        transactionFour.setAmount(new BigDecimal("23.55"));
        transactionFour.setCurrency(Currency.EUR);
        transactionFour.setDescription("Spar");
        transactionFour.setDirection(Direction.OUTGOING);
        transactionFour.setIban("AT13334222");
        transactionFour.setTransactionDate(LocalDateTime.parse("2016-04-23T09:00:31", DateTimeFormatter.ISO_DATE_TIME));
        transactionFour.setCompany("Spar");
        transactionFour = transactionDao.save(transactionFour);
        transactions.add(transactionFour);

        return transactions;
    }
}

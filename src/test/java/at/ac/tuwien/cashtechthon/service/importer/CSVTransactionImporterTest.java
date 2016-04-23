package at.ac.tuwien.cashtechthon.service.importer;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import at.ac.tuwien.cashtechthon.PLAYfulSavingApiApplication;
import at.ac.tuwien.cashtechthon.dao.ICustomerDao;
import at.ac.tuwien.cashtechthon.dao.ITransactionDao;
import at.ac.tuwien.cashtechthon.domain.Customer;
import at.ac.tuwien.cashtechthon.domain.Transaction;
import at.ac.tuwien.cashtechthon.service.importer.exception.TransactionImporterException;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = PLAYfulSavingApiApplication.class)
@WebAppConfiguration
public class CSVTransactionImporterTest {

	@Autowired
	private ITransactionDao transactionDao;
	@Autowired
	private ICustomerDao customerDao;
	
	@Test
	public void testTransactionImportShouldImportOneTransaction() throws TransactionImporterException {
		CSVTransactionImporter transactionImporter = new CSVTransactionImporter(transactionDao);
		Customer customer = new Customer();
		customer.setId(1L);
		customer.setFirstName("Foo");
		customer.setLastName("Bar");
		customerDao.save(customer);
		transactionImporter.setFilePath("src/test/resources/import/transactions.csv");
		transactionImporter.importTransactionsForCustomer(customer);
		List<Transaction> transactions = transactionDao.findAll();
		assertNotNull(transactions);
		assertEquals(1, transactions.size());
		assertEquals(new Long(1), transactions.get(0).getId());
	}
}

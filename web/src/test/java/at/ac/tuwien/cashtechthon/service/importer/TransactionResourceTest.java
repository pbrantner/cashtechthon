package at.ac.tuwien.cashtechthon.service.importer;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
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
import at.ac.tuwien.cashtechthon.domain.Transaction;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = PLAYfulSavingApiApplication.class)
@WebAppConfiguration
public class TransactionResourceTest {

	@Autowired
	private ITransactionImporter transactionImporter;
	@Autowired
	private ITransactionDao transactionDao;
	@Autowired
	private ICustomerDao customerDao;
	
	@Test
	public void testCSVTransactionResourceImportShouldImportOneTransaction() throws IOException {
		byte[] content = Files.readAllBytes(Paths.get("src/test/resources/import/transactions.csv"));
		CSVTransactionResource csvResource = new CSVTransactionResource(content);
		transactionImporter.importTransactions(csvResource);
		List<Transaction> transactions = transactionDao.findAll();
		assertNotNull(transactions);
		assertTrue(transactions.size() > 1);
	}
}

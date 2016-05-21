package at.ac.tuwien.cashtechthon.controller;

import at.ac.tuwien.cashtechthon.dao.ITransactionDao;
import at.ac.tuwien.cashtechthon.domain.Transaction;
import at.ac.tuwien.cashtechthon.dtos.CustomerReport;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

/**
 * Created by syrenio on 21/05/16.
 */
@RestController
@RequestMapping("/reports")
public class ReportController {

    private ITransactionDao transactionDao;

    @Autowired
    public ReportController(ITransactionDao transactionDao) {
        this.transactionDao = transactionDao;
    }

    @RequestMapping(value = "/customers/{customerId}", method = RequestMethod.GET)
    public CustomerReport getTransactions(@PathVariable("customerId") Long customerId,
                                          @RequestParam(value = "age", required = false) Long age,
                                          @RequestParam(value = "income", required = false) BigDecimal income){
        //LocalDateTime from = LocalDateTime.now().withDayOfYear(1);
        //LocalDateTime now = LocalDateTime.now();
        //List<Transaction> list = transactionDao.findByCustomerIdAndTransactionDateBetween(customerId, from, now);

        //return new PageImpl<Transaction>(list);
        CustomerReport report = new CustomerReport();

        report.getHeaders().add("Class");
        report.getHeaders().add("Money");

        Object[] d = new Object[2];
        d[0] = "Work";
        d[1] = 12;
        report.getData().add(d);

        return report;
    }
}

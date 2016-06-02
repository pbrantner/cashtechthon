package at.ac.tuwien.cashtechthon.controller;

import at.ac.tuwien.cashtechthon.dao.ITransactionDao;
import at.ac.tuwien.cashtechthon.domain.Transaction;
import at.ac.tuwien.cashtechthon.dtos.Classification;
import at.ac.tuwien.cashtechthon.dtos.CustomerReport;
import at.ac.tuwien.cashtechthon.dtos.ReportResponse;
import at.ac.tuwien.cashtechthon.service.IClassificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by syrenio on 21/05/16.
 */
@RestController
@RequestMapping("/reports")
public class ReportController {

    private ITransactionDao transactionDao;
    private IClassificationService classificationService;

    @Autowired
    public ReportController(ITransactionDao transactionDao, IClassificationService classificationService) {
        this.transactionDao = transactionDao;
        this.classificationService = classificationService;
    }

    @RequestMapping(value = "/customers/{customerId}", method = RequestMethod.GET)
    public ReportResponse getTransactions(@PathVariable("customerId") Long customerId,
                                          @RequestParam(value = "age", required = false) Long age,
                                          @RequestParam(value = "income", required = false) BigDecimal income){
        LocalDate firstDayOfYear = LocalDate.now().withDayOfYear(1);
        LocalDate now = LocalDate.now();

        List<Classification> classifications = classificationService.getClassifications(new Long[]{customerId}, firstDayOfYear, now);


        ReportResponse resp = new ReportResponse();

        /* CUSTOMER */
        resp.setCustomer(new CustomerReport());

        resp.getCustomer().getHeaders().add("Class");
        resp.getCustomer().getHeaders().add("Money");

        List<String> classList = classifications.stream().map(x -> x.getClassifications()).flatMap(l -> l.stream()).collect(Collectors.toList());

        classList.forEach(c -> {
            Object[] d = new Object[2];
            d[0] = c;
            d[1] = 999;
            resp.getCustomer().getData().add(d);
        });

        /* GROUP */
        resp.setGroup(new CustomerReport());
        resp.getGroup().getHeaders().add("Class");
        resp.getGroup().getHeaders().add("Money");

        Object[] d = new Object[2];
        d[0] = "FAKE";
        d[1] = 123;
        resp.getGroup().getData().add(d);

        return resp;
    }
}

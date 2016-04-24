package at.ac.tuwien.cashtechthon.service;

import at.ac.tuwien.cashtechthon.domain.Transaction;
import at.ac.tuwien.cashtechthon.dtos.Classification;
import at.ac.tuwien.cashtechthon.dtos.ClassificationSummary;
import org.json.JSONObject;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by Chris on 23.04.16.
 */

public interface IMLService {

    public ClassificationSummary getResult(List<Transaction> transactions);
    public List<Classification> getResultWithCustomers(List<Transaction> transactions);
}

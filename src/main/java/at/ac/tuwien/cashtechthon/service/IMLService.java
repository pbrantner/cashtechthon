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

    public void setAPIKey(String key);
    public void setAPIURL(String key);
    public void setData(Transaction transaction);
    public void setDataSet(List<Transaction> transactions);
    public ClassificationSummary getResult();
    public List<Classification> getResultWithCustomers();
}

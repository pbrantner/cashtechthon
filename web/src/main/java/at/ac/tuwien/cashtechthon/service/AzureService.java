package at.ac.tuwien.cashtechthon.service;

import at.ac.tuwien.cashtechthon.dao.ICustomerDao;
import at.ac.tuwien.cashtechthon.domain.Customer;
import at.ac.tuwien.cashtechthon.domain.Transaction;
import at.ac.tuwien.cashtechthon.dtos.Classification;
import at.ac.tuwien.cashtechthon.dtos.ClassificationSummary;
import at.ac.tuwien.cashtechthon.dtos.ClassificationSummaryEntry;
import org.apache.http.HttpEntity;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.protocol.HTTP;



@Service
public class AzureService implements IMLService {

    private static final String apiKey = "Ch3ChwPR9ji5iN8eH9VpRu0KXrH59tzI646cXiaSdZo7o3TWEpasabk6XxgMkrnvzsOKkyECAPA3eRsxvM+yAg==";
    private static final String apiurl = "https://ussouthcentral.services.azureml.net/workspaces/f759a12f91d6476881f3f034f1dc013c/services/b7bd464dd5d24ca095398a844b28776b/execute?api-version=2.0&details=true";


    ICustomerDao customerDao;

    @Autowired
    public AzureService(ICustomerDao customerDao) {
        this.customerDao = customerDao;
    }


    @Override
    public List<Classification> getResultWithCustomers(List<Transaction> transactions) {


        HttpEntity response = queryAzureWS(transactions);

        String retSrc = null;
        try {

            JSONObject output = new JSONObject();

            retSrc = EntityUtils.toString(response);
            // parsing JSON
            JSONObject result = new JSONObject(retSrc);

            JSONArray values = result.getJSONObject("Results").getJSONObject("transaction_out").getJSONObject("value").getJSONArray("Values");


            HashMap<Long, HashMap<String, Integer>> customers = new HashMap<>();

            String kdNr = values.getJSONArray(0).get(0).toString();


            ArrayList<Classification> classifications = new ArrayList<>();
            HashMap<Long, ArrayList<String>> classMapping = new HashMap<>();

            for(int i = 0; i < values.length(); i++) {

                JSONArray val = values.getJSONArray(i);
                Long customerId = Long.parseLong(values.getJSONArray(i).get(0).toString());
                String label = val.get(val.length() - 1).toString();

                if(classMapping.containsKey(customerId)) {
                    if(!classMapping.get(customerId).contains(label)) {
                        classMapping.get(customerId).add(label);
                    }
                }
                else {
                    ArrayList<String> labels = new ArrayList<>();
                    labels.add(label);
                    classMapping.put(customerId, labels);
                }

            }

            for(Map.Entry<Long, ArrayList<String>> iter : classMapping.entrySet()) {
                Classification classy = new Classification();
                classy.setCustomerId(iter.getKey());

                Customer customer = customerDao.findById(iter.getKey());
                classy.setFirstName(customer.getFirstName());
                classy.setLastName(customer.getLastName());
                classy.setClassifications(iter.getValue());
                classifications.add(classy);
            }


            return classifications;


        } catch (IOException e) {
            e.printStackTrace();
        }



        return null;
    }

    @Override
    public ClassificationSummary getResult(List<Transaction> transactions) {


        HttpEntity response = queryAzureWS(transactions);

        String retSrc = null;

        try {

            JSONObject output = new JSONObject();

            retSrc = EntityUtils.toString(response);
            // parsing JSON
            JSONObject result = new JSONObject(retSrc);

            JSONArray values = result.getJSONObject("Results").getJSONObject("transaction_out").getJSONObject("value").getJSONArray("Values");


            long transactionsSum = 0;
            ArrayList<Long> customers = new ArrayList<>();
            long customerCount = 0;
            HashMap<String, Integer> catVals = new HashMap<String, Integer>();
            HashMap<String, ArrayList<Long>> catCustomerCount = new HashMap<String, ArrayList<Long>>();
            for(int i = 0; i < values.length(); i++) {

                Long customerId = Long.parseLong(values.getJSONArray(i).get(0).toString());

                if(!customers.contains(customerId)) {
                    customerCount++;
                }

                transactionsSum += 1;
                JSONArray val = values.getJSONArray(i);

                String label = val.get(val.length() - 1).toString();

                if(catCustomerCount.containsKey(label)) {
                    if(!catCustomerCount.get(label).contains(customerId)) {
                        catCustomerCount.get(label).add(customerId);
                    }
                }
                else {
                    ArrayList<Long> cats = new ArrayList<>();
                    cats.add(customerId);
                    catCustomerCount.put(label, cats);
                }

                if(catVals.containsKey(label)) {
                    catVals.put(label,catVals.get(label) + 1);
                }
                else {
                    catVals.put(label,1);
                }
            }

            ClassificationSummary summary = new ClassificationSummary();

            ArrayList<ClassificationSummaryEntry> entries = new ArrayList<ClassificationSummaryEntry>();

            for(Map.Entry<String, Integer> entry : catVals.entrySet()) {

                ClassificationSummaryEntry summaryEntry = new ClassificationSummaryEntry();
                summaryEntry.setName(entry.getKey());
                summaryEntry.setTransactions(entry.getValue());
                summaryEntry.setTransactionsPercentage((double)entries.size()/(double)transactionsSum);
                summaryEntry.setCustomers(catCustomerCount.get(entry.getKey()).size());
                summaryEntry.setCustomersPercentage((double)catCustomerCount.get(entry.getKey()).size()/(double)customerCount);
                entries.add(summaryEntry);
            }

            summary.setCustomersTotal(customerCount);
            summary.setTransactionsTotal(transactionsSum);
            summary.setClassificationsTotal(entries.size());
            summary.setClassifications(entries);

            return summary;

        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }


    private HttpEntity queryAzureWS(List<Transaction> transactions) {

        HttpPost post;
        HttpClient client;
        StringEntity entity;

        try {
            // create HttpPost and HttpClient object
            post = new HttpPost(apiurl);
            client = HttpClientBuilder.create().build();

            // setup output message by copying JSON body into
            // apache StringEntity object along with content type

            entity = new StringEntity(convertTransactions(transactions).toString(), HTTP.UTF_8);
            entity.setContentEncoding(HTTP.UTF_8);
            entity.setContentType("text/json");

            // add HTTP headers
            post.setHeader("Accept", "text/json");
            post.setHeader("Accept-Charset", "UTF-8");

            // set Authorization header based on the API key
            post.setHeader("Authorization", ("Bearer "+apiKey));
            post.setEntity(entity);

            // Call REST API and retrieve response content
            HttpResponse authResponse = client.execute(post);


           // System.out.println(EntityUtils.toString(authResponse.getEntity()));

            return authResponse.getEntity();
        }
        catch (Exception e) {

        }

        return null;
    }

    private JSONObject convertTransactions(List<Transaction> transactions) {


        if(transactions != null && !transactions.isEmpty()) {

          /*  {
                "Inputs": {
                "transaction_in":{
                    "ColumnNames":[...],
                    "Values": [
                    [],[]]}}, "GlobalParameters": {}
            }*/

            JSONObject wrapper = new JSONObject();
            JSONObject inputs = new JSONObject();
            JSONObject transactionIn = new JSONObject();
            JSONArray columnNames  = new JSONArray();
            JSONArray values  = new JSONArray();
            JSONObject globalParameters = new JSONObject();

            columnNames.put("KdNr");
            columnNames.put("Vorname");
            columnNames.put("Nachname");
            columnNames.put("Type");
            columnNames.put("Wert");
            columnNames.put("Description");
            columnNames.put("Kategorie");

            for(Transaction t : transactions) {
                JSONArray valuesData = new JSONArray();

                valuesData.put(t.getCustomerId());
                valuesData.put("");
                valuesData.put("");
                valuesData.put("");
                valuesData.put(0);
                valuesData.put(t.getDescription());
                valuesData.put("");
                values.put(valuesData);
            }

            transactionIn.put("ColumnNames", columnNames);
            transactionIn.put("Values", values);

            inputs.put("transaction_in", transactionIn);
            wrapper.put("Inputs", inputs);
            wrapper.put("GlobalParameters",globalParameters);

            return wrapper;
        }

        return null;
    }

    /*
    private JSONObject parseResponse(HttpEntity response) {


        String retSrc = null;
        try {

            JSONObject output = new JSONObject();

            retSrc = EntityUtils.toString(response);
            // parsing JSON
            JSONObject result = new JSONObject(retSrc);

            JSONArray values = result.getJSONArray("Values");

            String kdNr = values.getJSONArray(0).get(0).toString();
            HashMap<String, Integer> categories = new HashMap<>();
            for(int i = 0; i < values.length(); i++) {

                JSONArray val = values.getJSONArray(i);

                String label = val.get(val.length() - 1).toString();

                if(categories.containsKey(label)) {
                    categories.put(label,categories.get(label) + 1);
                }
                else {
                    categories.put(label,1);
                }

            }


            JSONObject cat = new JSONObject();

            for (Map.Entry<String, Integer> entry : categories.entrySet()) {
                    cat.put(entry.getKey(),entry.getValue());
            }


            output.put("KdNr", kdNr);
            output.put("Kategorien", cat);


        } catch (IOException e) {
            e.printStackTrace();
        }


        Classification c = new Classification();

        return null;
    }
        */

}

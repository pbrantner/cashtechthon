package at.ac.tuwien.cashtechthon.service;

import at.ac.tuwien.cashtechthon.domain.Transaction;
import org.apache.http.HttpEntity;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.protocol.HTTP;


/**
 * Created by Chris on 23.04.16.
 */
@Service
public class AzureService implements IMLService {

    private List<Transaction> transactions = new ArrayList<Transaction>();
    private String apiKey;
    public String apiurl;
    public String jsonBody;

    @Override
    public void setAPIKey(String key) {
        this.apiKey = key;
    }

    @Override
    public void setData(Transaction transaction) {
        this.transactions.clear();
        this.transactions.add(transaction);
    }

    @Override
    public void setDataSet(List<Transaction> transactions) {
        this.transactions = transactions;
    }


    @Override
    public JSONObject getResult() {

        HttpPost post;
        HttpClient client;
        StringEntity entity;

        try {
            // create HttpPost and HttpClient object
            post = new HttpPost(apiurl);
            client = HttpClientBuilder.create().build();

            // setup output message by copying JSON body into
            // apache StringEntity object along with content type

            entity = new StringEntity(jsonBody, HTTP.UTF_8);
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



            //parseResponse(authResponse.getEntity());


            System.out.println(EntityUtils.toString(authResponse.getEntity()));

            return null;


        }
        catch (Exception e) {

        }

        return null;
    }


    private JSONObject convertTransactions() {


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

                valuesData.put(t.getIban());
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

    private JSONObject parseResponse(HttpEntity response) {


        String retSrc = null;
        try {

            //JSONArray =
            retSrc = EntityUtils.toString(response);
            // parsing JSON
            JSONObject result = new JSONObject(retSrc);

            JSONArray values = result.getJSONArray("Values");
            for(int i = 0; i < values.length(); i++) {
                JSONArray val = values.getJSONArray(i);

                String labels = val.get(val.length() - 1).toString();

            }


        } catch (IOException e) {
            e.printStackTrace();
        }



        return null;
    }

}

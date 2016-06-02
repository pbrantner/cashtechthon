package at.ac.tuwien.cashtechthon.dtos;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by syrenio on 21/05/16.
 */
public class CustomerReport {

    private List<String> headers;
    private List<Object[]> data;

    public CustomerReport() {
        this.headers = new ArrayList<>();
        this.data = new ArrayList<>();
    }

    public List<String> getHeaders() {
        return headers;
    }

    public void setHeaders(List<String> headers) {
        this.headers = headers;
    }

    public List<Object[]> getData() {
        return data;
    }

    public void setData(List<Object[]> data) {
        this.data = data;
    }
}

package at.ac.tuwien.cashtechthon.dtos;


import java.util.List;

/*
    {
        "start":"2015-11"
        ,"end":"2016-02"
        ,"columns":['Month', 'Customer', 'Group']
        ,"data": [
          ['2015-11', 630.77 , 722.10 ]
         ,['2015-12', 1050.23, 1802.12]
         ,['2016-01', 303.55 , 280.78 ]
         ,['2016-02', 405.20 , 480.10 ]
         ]
    }
 */
public class ComparisonResponse {

    private String start;
    private String end;
    private List<String> columns;
    private List<List<Object>> data;

    public String getStart() {
        return start;
    }

    public void setStart(String start) {
        this.start = start;
    }

    public String getEnd() {
        return end;
    }

    public void setEnd(String end) {
        this.end = end;
    }

    public List<String> getColumns() {
        return columns;
    }

    public void setColumns(List<String> columns) {
        this.columns = columns;
    }

    public List<List<Object>> getData() {
        return data;
    }

    public void setData(List<List<Object>> data) {
        this.data = data;
    }
}

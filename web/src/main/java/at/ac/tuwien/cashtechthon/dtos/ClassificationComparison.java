package at.ac.tuwien.cashtechthon.dtos;

import java.util.List;

public class ClassificationComparison {
	private String start;
	private String end;
	private List<String> columns;
	private Object[] data;
	
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
	
	public Object[] getData() {
		return data;
	}
	
	public void setData(Object[] data) {
		this.data = data;
	}
	
	
	

}

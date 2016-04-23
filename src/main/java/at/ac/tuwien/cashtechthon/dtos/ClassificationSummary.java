package at.ac.tuwien.cashtechthon.dtos;

public class ClassificationSummary {
	private String name;
	private Double percentage;
	private Long total;
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	
	public Double getPercentage() {
		return percentage;
	}
	
	public void setPercentage(Double percentage) {
		this.percentage = percentage;
	}
	
	public Long getTotal() {
		return total;
	}
	
	public void setTotal(Long total) {
		this.total = total;
	}
}

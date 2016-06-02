package at.ac.tuwien.cashtechthon.cep;

import java.util.List;

public class ClassificationFilter {
	private Integer pastMonths;
	private Integer ageFrom;
	private Integer ageTill;
	private Boolean sex;
	private Double incomeFrom;
	private Double incomeTo;
	private List<String> locations;
	private List<String> classifications;
	
	public Integer getPastMonths() {
		return pastMonths;
	}
	
	public void setPastMonths(Integer pastMonths) {
		this.pastMonths = pastMonths;
	}

	public Integer getAgeFrom() {
		return ageFrom;
	}

	public void setAgeFrom(Integer ageFrom) {
		this.ageFrom = ageFrom;
	}

	public Integer getAgeTill() {
		return ageTill;
	}

	public void setAgeTill(Integer ageTill) {
		this.ageTill = ageTill;
	}

	public Boolean getSex() {
		return sex;
	}

	public void setSex(Boolean sex) {
		this.sex = sex;
	}

	public Double getIncomeFrom() {
		return incomeFrom;
	}

	public void setIncomeFrom(Double incomeFrom) {
		this.incomeFrom = incomeFrom;
	}

	public Double getIncomeTo() {
		return incomeTo;
	}

	public void setIncomeTo(Double incomeTo) {
		this.incomeTo = incomeTo;
	}

	public List<String> getLocations() {
		return locations;
	}

	public void setLocations(List<String> locations) {
		this.locations = locations;
	}

	public List<String> getClassifications() {
		return classifications;
	}

	public void setClassifications(List<String> classifications) {
		this.classifications = classifications;
	}
}

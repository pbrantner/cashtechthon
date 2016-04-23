package at.ac.tuwien.cashtechthon.dtos;

import java.util.List;

public class Classification {
	private Long customerId;
	private String firstName;
	private String lastName;
	private List<String> classifications;
	
	public Long getCustomerId() {
		return customerId;
	}
	
	public void setCustomerId(Long customerId) {
		this.customerId = customerId;
	}
	
	public String getFirstName() {
		return firstName;
	}
	
	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}
	
	public String getLastName() {
		return lastName;
	}
	
	public void setLastName(String lastName) {
		this.lastName = lastName;
	}
	
	public List<String> getClassifications() {
		return classifications;
	}
	
	public void setClassifications(List<String> classifications) {
		this.classifications = classifications;
	}
}

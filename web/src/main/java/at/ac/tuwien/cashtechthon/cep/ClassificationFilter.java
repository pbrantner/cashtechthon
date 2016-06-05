package at.ac.tuwien.cashtechthon.cep;

import java.util.List;

public class ClassificationFilter {
	private final Long customerId;
	private final Integer pastMonths;
	private final Integer ageFrom;
	private final Integer ageTill;
	private final Boolean sex;
	private final Double incomeFrom;
	private final Double incomeTo;
	private final List<String> locations;
	private final List<String> classifications;
	
	public ClassificationFilter(Builder builder) {
		this.customerId = builder.customerId;
		this.pastMonths = builder.pastMonths;
		this.ageFrom = builder.ageFrom;
		this.ageTill = builder.ageTill;
		this.sex = builder.sex;
		this.incomeFrom = builder.incomeFrom;
		this.incomeTo = builder.incomeTo;
		this.locations = builder.locations;
		this.classifications = builder.classifications;
	}
	
	public Long getCustomerId() {
		return customerId;
	}


	
	public Integer getPastMonths() {
		return pastMonths;
	}
	


	public Integer getAgeFrom() {
		return ageFrom;
	}



	public Integer getAgeTill() {
		return ageTill;
	}



	public Boolean getSex() {
		return sex;
	}



	public Double getIncomeFrom() {
		return incomeFrom;
	}



	public Double getIncomeTo() {
		return incomeTo;
	}



	public List<String> getLocations() {
		return locations;
	}



	public List<String> getClassifications() {
		return classifications;
	}


	
	public static class Builder {
		private Long customerId;
		private Integer pastMonths;
		private Integer ageFrom;
		private Integer ageTill;
		private Boolean sex;
		private Double incomeFrom;
		private Double incomeTo;
		private List<String> locations;
		private List<String> classifications;
		
		public Builder customerId(Long customerId) {
			this.customerId = customerId;
			return this;
		}
		
		public Builder pastMonths(Integer pastMonths) {
			this.pastMonths = pastMonths;
			return this;
		}
		
		public Builder ageFrom(Integer ageFrom) {
			this.ageFrom = ageFrom;
			return this;
		}
		
		public Builder ageTill(Integer ageTill) {
			this.ageTill = ageTill;
			return this;
		}
		
		public Builder sex(Boolean sex) {
			this.sex = sex;
			return this;
		}
		
		public Builder incomeFrom(Double incomeFrom) {
			this.incomeFrom = incomeFrom;
			return this;
		}
		
		public Builder incomeTo(Double incomeTo) {
			this.incomeTo = incomeTo;
			return this;
		}
		
		public Builder locations(List<String> locations) {
			this.locations = locations;
			return this;
		}
		
		public Builder classifications(List<String> classifications) {
			this.classifications = classifications;
			return this;
		}
		
		public ClassificationFilter build() {
			return new ClassificationFilter(this);
		}
	}
}

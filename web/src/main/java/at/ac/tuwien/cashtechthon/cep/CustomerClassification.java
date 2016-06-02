package at.ac.tuwien.cashtechthon.cep;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Currency;

public class CustomerClassification {
	private Long customerId;
	private LocalDateTime birthday;
	private String location;
	private Boolean sex;
	private String classification;
	private LocalDateTime classificationDate;
	private Currency currency;
	private BigDecimal amount;
	private BigDecimal amountInEur;

	public CustomerClassification(Long customerId, LocalDateTime birthday, String location, Boolean sex, String classification,
			LocalDateTime classificationDate, Currency currency, BigDecimal amount, BigDecimal amountInEur) {
		this.customerId = customerId;
		this.birthday = birthday;
		this.location = location;
		this.sex = sex;
		this.classificationDate = classificationDate;
		this.currency = currency;
		this.classification = classification;
		this.amount = amount;
		this.amountInEur = amountInEur;
	}
	
	public Long getCustomerId() {
		return customerId;
	}

	public void setCustomerId(Long customerId) {
		this.customerId = customerId;
	}

	public BigDecimal getAmount() {
		return amount;
	}

	public void setAmount(BigDecimal amount) {
		this.amount = amount;
	}

	public String getClassification() {
		return classification;
	}

	public void setClassification(String classification) {
		this.classification = classification;
	}

	public LocalDateTime getClassificationDate() {
		return classificationDate;
	}

	public void setClassificationDate(LocalDateTime classificationDate) {
		this.classificationDate = classificationDate;
	}

	public LocalDateTime getBirthday() {
		return birthday;
	}

	public void setBirthday(LocalDateTime birthday) {
		this.birthday = birthday;
	}

	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		this.location = location;
	}

	public Boolean getSex() {
		return sex;
	}

	public void setSex(Boolean sex) {
		this.sex = sex;
	}

	public Currency getCurrency() {
		return currency;
	}

	public void setCurrency(Currency currency) {
		this.currency = currency;
	}

	public BigDecimal getAmountInEur() {
		return amountInEur;
	}

	public void setAmountInEur(BigDecimal amountInEur) {
		this.amountInEur = amountInEur;
	}	
}

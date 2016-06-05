package at.ac.tuwien.cashtechthon.cep.event;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class AccountBalanceEvent {
	private Long customerId;
	private BigDecimal balanceInEur;
	private LocalDateTime determinedAt;
	
	public Long getCustomerId() {
		return customerId;
	}
	
	public void setCustomerId(Long customerId) {
		this.customerId = customerId;
	}
	
	public BigDecimal getBalanceInEur() {
		return balanceInEur;
	}
	
	public void setBalanceInEur(BigDecimal balanceInEur) {
		this.balanceInEur = balanceInEur;
	}
	
	public LocalDateTime getDeterminedAt() {
		return determinedAt;
	}
	
	public void setDeterminedAt(LocalDateTime determinedAt) {
		this.determinedAt = determinedAt;
	}
}

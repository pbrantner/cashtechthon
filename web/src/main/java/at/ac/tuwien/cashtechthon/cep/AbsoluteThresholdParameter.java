package at.ac.tuwien.cashtechthon.cep;

import java.math.BigDecimal;

import at.ac.tuwien.cashtechthon.cep.event.AccountBalanceEvent;

public class AbsoluteThresholdParameter {
	private final AccountBalanceEvent accountBalance;
	private final BigDecimal thresholdInEur;
	private final String type;
	private final AlertCallback callback;
	
	private AbsoluteThresholdParameter(Builder builder) {
		this.accountBalance = builder.accountBalance;
		this.thresholdInEur = builder.thresholdInEur;
		this.type = builder.type;
		this.callback = builder.callback;
	}
	
	public AccountBalanceEvent getAccountBalance() {
		return accountBalance;
	}
	
	public static AccountBalanceStep newInstance() {
		return new Builder();
	}

	public BigDecimal getThresholdInEur() {
		return thresholdInEur;
	}

	public String getType() {
		return type;
	}

	public AlertCallback getCallback() {
		return callback;
	}
	

	public static class Builder implements AccountBalanceStep, ThresholdStep, TypeStep, CallbackStep, BuildStep {
		private AccountBalanceEvent accountBalance;
		private BigDecimal thresholdInEur;
		private String type;
		private AlertCallback callback;
		
		private Builder() {
		}
		
		@Override
		public ThresholdStep accountBalance(AccountBalanceEvent accountBalance) {
			this.accountBalance = accountBalance;
			return this;
		}
		
		@Override
		public TypeStep thresholdInEur(BigDecimal thresholdInEur) {
			this.thresholdInEur = thresholdInEur;
			return this;
		}

		@Override
		public CallbackStep type(String type) {
			this.type = type;
			return this;
		}
		
		@Override
		public BuildStep callback(AlertCallback callback) {
			this.callback = callback;
			return this;
		}
		@Override
		public AbsoluteThresholdParameter build() {
			return new AbsoluteThresholdParameter(this);
		}
	}
	
	public static interface AccountBalanceStep {
		ThresholdStep accountBalance(AccountBalanceEvent accountBalance);
	}
	
	public static interface ThresholdStep {
		TypeStep thresholdInEur(BigDecimal thresholdInEur);
	}
	
	public static interface TypeStep {
		CallbackStep type(String type);
	}
	
	public static interface CallbackStep {
		BuildStep callback(AlertCallback callback);
	}
	
	public static interface BuildStep {
		AbsoluteThresholdParameter build();
	}
}

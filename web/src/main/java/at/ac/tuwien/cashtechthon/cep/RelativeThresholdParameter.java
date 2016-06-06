package at.ac.tuwien.cashtechthon.cep;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class RelativeThresholdParameter {
	private final Long customerId;
	private final BigDecimal thresholdInEur;
	private final String classification;
	private final String type;
	private final String direction;
	private final AlertCallback callback;
	private final LocalDateTime windowStart;
	private final Long windowSize;
	
	private RelativeThresholdParameter(Builder builder) {
		this.customerId = builder.customerId;
		this.thresholdInEur = builder.thresholdInEur;
		this.classification = builder.classification;
		this.type = builder.type;
		this.direction = builder.direction;
		this.callback = builder.callback;
		this.windowStart = builder.windowStart;
		this.windowSize = builder.windowSize;
	}
	
	public static UserIdStep newInstance() {
		return new Builder();
	}
	
	public Long getCustomerId() {
		return customerId;
	}

	public BigDecimal getThresholdInEur() {
		return thresholdInEur;
	}

	public String getClassification() {
		return classification;
	}

	public String getType() {
		return type;
	}

	public String getDirection() {
		return direction;
	}

	public AlertCallback getCallback() {
		return callback;
	}

	public LocalDateTime getWindowStart() {
		return windowStart;
	}

	public Long getWindowSize() {
		return windowSize;
	}

	
	public static class Builder implements UserIdStep, ThresholdStep, TypeStep, DirectionStep, CallbackStep, OptionalParametersStep {
		private Long customerId;
		private BigDecimal thresholdInEur;
		private String classification;
		private String type;
		private String direction;
		private AlertCallback callback;
		private LocalDateTime windowStart;
		private Long windowSize;
		
		private Builder() {
		}
		
		@Override
		public OptionalParametersStep classification(String classification) {
			this.classification = classification;
			return this;
		}

		@Override
		public OptionalParametersStep windowStart(LocalDateTime windowStart) {
			this.windowStart = windowStart;
			return this;
		}

		@Override
		public OptionalParametersStep windowSize(Long windowSize) {
			this.windowSize = windowSize;
			return this;
		}

		@Override
		public OptionalParametersStep callback(AlertCallback callback) {
			this.callback = callback;
			return this;
		}

		@Override
		public CallbackStep direction(String direction) {
			this.direction = direction;
			return this;
		}

		@Override
		public DirectionStep type(String type) {
			this.type = type;
			return this;
		}

		@Override
		public TypeStep thresholdInEur(BigDecimal thresholdInEur) {
			this.thresholdInEur = thresholdInEur;
			return this;
		}

		@Override
		public ThresholdStep customerId(Long customerId) {
			this.customerId = customerId;
			return this;
		}
		
		@Override
		public RelativeThresholdParameter build() {
			return new RelativeThresholdParameter(this);
		}

	}
	
	public static interface UserIdStep {
		ThresholdStep customerId(Long customerId);
	}
	
	public static interface ThresholdStep {
		TypeStep thresholdInEur(BigDecimal thresholdInEur);
	}
	
	public static interface TypeStep {
		DirectionStep type(String type);
	}
	
	public static interface DirectionStep {
		CallbackStep direction(String direction);
	}
	
	public static interface CallbackStep {
		OptionalParametersStep callback(AlertCallback callback);
	}
	
	public static interface OptionalParametersStep {
		OptionalParametersStep classification(String classification);
		OptionalParametersStep windowStart(LocalDateTime windowStart);
		OptionalParametersStep windowSize(Long windowSize);
		RelativeThresholdParameter build();
	}
}

package at.ac.tuwien.cashtechthon.dtos;

import java.util.ArrayList;
import java.util.List;

public class ClassificationSummary {
	private long transactionsTotal;
	private long classificationsTotal;
	private long customersTotal;
	private List<ClassificationSummaryEntry> classifications;

	public ClassificationSummary() {
		classifications = new ArrayList<>();
	}

	public long getTransactionsTotal() {
		return transactionsTotal;
	}

	public void setTransactionsTotal(long transactionsTotal) {
		this.transactionsTotal = transactionsTotal;
	}

	public long getClassificationsTotal() {
		return classificationsTotal;
	}

	public void setClassificationsTotal(long classificationsTotal) {
		this.classificationsTotal = classificationsTotal;
	}

	public long getCustomersTotal() {
		return customersTotal;
	}

	public void setCustomersTotal(long customersTotal) {
		this.customersTotal = customersTotal;
	}

	public List<ClassificationSummaryEntry> getClassifications() {
		return classifications;
	}

	public void setClassifications(List<ClassificationSummaryEntry> classifications) {
		this.classifications = classifications;
	}
}

package at.ac.tuwien.cashtechthon.dtos;

/**
 * Created by syrenio on 22/06/16.
 */
public class ReportExpEarn {

    private ReportData expenses;

    private ReportData earnings;

    public ReportData getExpenses() {
        return expenses;
    }

    public void setExpenses(ReportData expenses) {
        this.expenses = expenses;
    }

    public ReportData getEarnings() {
        return earnings;
    }

    public void setEarnings(ReportData earnings) {
        this.earnings = earnings;
    }
}

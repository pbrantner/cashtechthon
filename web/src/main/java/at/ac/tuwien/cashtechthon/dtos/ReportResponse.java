package at.ac.tuwien.cashtechthon.dtos;

/**
 * Created by syrenio on 21/05/16.
 */
public class ReportResponse {
    /* requested customer report*/
    private ReportExpEarn customer;
    /* age/income group to compare to*/
    private ReportExpEarn group;

    public ReportExpEarn getCustomer() {
        return customer;
    }

    public void setCustomer(ReportExpEarn customer) {
        this.customer = customer;
    }

    public ReportExpEarn getGroup() {
        return group;
    }

    public void setGroup(ReportExpEarn group) {
        this.group = group;
    }
}

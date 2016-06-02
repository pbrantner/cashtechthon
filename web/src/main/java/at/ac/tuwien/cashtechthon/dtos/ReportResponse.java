package at.ac.tuwien.cashtechthon.dtos;

/**
 * Created by syrenio on 21/05/16.
 */
public class ReportResponse {
    /* requested customer report*/
    private CustomerReport customer;
    /* age/income group to compare to*/
    private CustomerReport group;

    public CustomerReport getCustomer() {
        return customer;
    }

    public void setCustomer(CustomerReport customer) {
        this.customer = customer;
    }

    public CustomerReport getGroup() {
        return group;
    }

    public void setGroup(CustomerReport group) {
        this.group = group;
    }
}

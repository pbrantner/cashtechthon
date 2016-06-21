package at.ac.tuwien.cashtechthon.cep;

import at.ac.tuwien.cashtechthon.dao.IEventDao;
import at.ac.tuwien.cashtechthon.domain.Classification;
import at.ac.tuwien.cashtechthon.domain.Customer;
import at.ac.tuwien.cashtechthon.domain.Event;
import at.ac.tuwien.cashtechthon.domain.Threshold;
import org.apache.log4j.Logger;

public class EventPersistingCallback implements AlertCallback {
    private final static Logger logger = Logger.getLogger(EventPersistingCallback.class);

    private IEventDao eventDao;
    private Threshold threshold;
    private Customer customer;

    public EventPersistingCallback(Threshold threshold, Customer customer, IEventDao eventDao) {
        this.threshold = threshold;
        this.customer = customer;
        this.eventDao = eventDao;
    }

    @Override
    public void onAlert(Long alertId) {
        Event event = new Event();
        event.setThreshold(threshold);
        event.setCustomer(customer);

        eventDao.save(event);

        logger.debug("New event: " + event.toString());
    }
}

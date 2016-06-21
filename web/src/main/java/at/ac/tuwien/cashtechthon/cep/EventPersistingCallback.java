package at.ac.tuwien.cashtechthon.cep;

import at.ac.tuwien.cashtechthon.dao.IEventDao;
import at.ac.tuwien.cashtechthon.domain.Threshold;

public class EventPersistingCallback implements AlertCallback {
    public EventPersistingCallback(Threshold threshold, IEventDao eventDao) {

    }

    @Override
    public void onAlert(Long alertId) {

    }
}

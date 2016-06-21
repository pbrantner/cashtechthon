package at.ac.tuwien.cashtechthon.domain;

import javax.persistence.Entity;
import java.math.BigDecimal;

@Entity
public class Classification {

    private Currency currency;
    private BigDecimal amount;

}

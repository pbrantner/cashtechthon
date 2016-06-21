package at.ac.tuwien.cashtechthon.dtos;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * Aggregation DTO of classification data for frontend Line-Chart
 */
public class MonthClassification {
    private LocalDateTime dateTime;
    private BigDecimal total;
    private Integer dayOfYear;

    public MonthClassification(Integer dayOfYear, BigDecimal total) {
        //this.dateTime = dateTime;
        this.total = total;
        this.dayOfYear = dayOfYear;
    }

    public LocalDateTime getDateTime() {
        return dateTime;
    }

    public void setDateTime(LocalDateTime dateTime) {
        this.dateTime = dateTime;
    }

    public BigDecimal getTotal() {
        return total;
    }

    public void setTotal(BigDecimal total) {
        this.total = total;
    }

    public Integer getDayOfYear() {
        return dayOfYear;
    }

    public void setDayOfYear(Integer dayOfYear) {
        this.dayOfYear = dayOfYear;
    }
}

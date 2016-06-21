package at.ac.tuwien.shared.dtos;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class Threshold {
    private String classification;
    private BigDecimal threshold;
    private LocalDateTime thresholdDate;
    private Long windowSize;

    public String getClassification() {
        return classification;
    }

    public void setClassification(String classification) {
        this.classification = classification;
    }

    public BigDecimal getThreshold() {
        return threshold;
    }

    public void setThreshold(BigDecimal value) {
        this.threshold = value;
    }

    public LocalDateTime getThresholdDate() {
        return thresholdDate;
    }

    public void setThresholdDate(LocalDateTime thresholdDate) {
        this.thresholdDate = thresholdDate;
    }

    public Long getWindowSize() {
        return windowSize;
    }

    public void setWindowSize(Long windowSize) {
        this.windowSize = windowSize;
    }
}

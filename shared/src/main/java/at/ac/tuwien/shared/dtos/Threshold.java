package at.ac.tuwien.shared.dtos;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class Threshold {
    private String classification;
    private BigDecimal threshold;
    private LocalDateTime creationDate;

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

    public LocalDateTime getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(LocalDateTime creationDate) {
        this.creationDate = creationDate;
    }
}

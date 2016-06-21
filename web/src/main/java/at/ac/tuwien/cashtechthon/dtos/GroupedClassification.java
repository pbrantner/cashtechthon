package at.ac.tuwien.cashtechthon.dtos;

import java.math.BigDecimal;

/**
 * Aggregation DTO of classification data for frontend Pie-Chart
 */
public class GroupedClassification {
    private String classification;
    private BigDecimal nrClassifications;

    public GroupedClassification(String classification, BigDecimal nrClassifications) {
        this.classification = classification;
        this.nrClassifications = nrClassifications;
    }

    public String getClassification() {
        return classification;
    }

    public void setClassification(String classification) {
        this.classification = classification;
    }

    public BigDecimal getNrClassifications() {
        return nrClassifications;
    }

    public void setNrClassifications(BigDecimal nrClassifications) {
        this.nrClassifications = nrClassifications;
    }
}

package at.ac.tuwien.shared.dtos;

import java.math.BigDecimal;

public class ExtendedClassification extends Classification {
    private double lowerPercentage;
    private double upperPercentage;

    public ExtendedClassification() {
        super();
        lowerPercentage = 0.9;
        upperPercentage = 1.1;
    }
    public ExtendedClassification(Customer customer, BigDecimal amount, String classification) {
        super(customer, amount, classification);
        lowerPercentage = 0.9;
        upperPercentage = 1.1;
    }
    public ExtendedClassification(Customer customer, BigDecimal amount, String classification,
                                  double lowerPercentage, double upperPercentage) {
        super(customer, amount, classification);
        this.lowerPercentage = lowerPercentage;
        this.upperPercentage = upperPercentage;
    }

    public double getLowerPercentage() {
        return lowerPercentage;
    }

    public void setLowerPercentage(double lowerPercentage) {
        this.lowerPercentage = lowerPercentage;
    }

    public double getUpperPercentage() {
        return upperPercentage;
    }

    public void setUpperPercentage(double upperPercentage) {
        this.upperPercentage = upperPercentage;
    }
}

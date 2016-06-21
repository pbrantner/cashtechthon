package at.ac.tuwien.cashtechthon.dtos;

/**
 * Created by Tom on 21.06.16.
 */
public class GroupedClassification {
    private String classification;
    private Long nrClassifications;

    public GroupedClassification(String classification, Long nrClassifications) {
        this.classification = classification;
        this.nrClassifications = nrClassifications;
    }

    public String getClassification() {
        return classification;
    }

    public void setClassification(String classification) {
        this.classification = classification;
    }

    public Long getNrClassifications() {
        return nrClassifications;
    }

    public void setNrClassifications(Long nrClassifications) {
        this.nrClassifications = nrClassifications;
    }
}

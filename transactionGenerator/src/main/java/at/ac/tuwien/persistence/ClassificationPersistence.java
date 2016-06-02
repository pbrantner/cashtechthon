package at.ac.tuwien.persistence;

import at.ac.tuwien.domain.Classification;

import java.util.List;

public interface ClassificationPersistence {
    void save(List<Classification> c);
}

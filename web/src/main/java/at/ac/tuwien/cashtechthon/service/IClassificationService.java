package at.ac.tuwien.cashtechthon.service;

import java.time.LocalDate;
import java.util.List;

import at.ac.tuwien.cashtechthon.dtos.Classification;
import at.ac.tuwien.cashtechthon.dtos.ClassificationSummary;

public interface IClassificationService {
	List<Classification> getClassifications(LocalDate from, LocalDate till);
	List<Classification> getClassifications(Long[] customerIds, LocalDate from, LocalDate till);
	ClassificationSummary getClassificationSummary(LocalDate from, LocalDate till);

	/**
	 * Persists a classification
	 * @param classification classification DTO to persist
	 * @return persisted classification
     */
	at.ac.tuwien.cashtechthon.domain.Classification save(at.ac.tuwien.shared.dtos.Classification classification);
}

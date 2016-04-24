package at.ac.tuwien.cashtechthon.service;

import java.time.LocalDate;

import at.ac.tuwien.cashtechthon.dtos.Classification;
import at.ac.tuwien.cashtechthon.dtos.ClassificationSummary;

public interface IClassificationService {
	Classification getClassification(LocalDate from, LocalDate till);
	Classification getClassification(Long[] customerIds, LocalDate from, LocalDate till);
	ClassificationSummary getClassificationSummary(LocalDate from, LocalDate till);
}

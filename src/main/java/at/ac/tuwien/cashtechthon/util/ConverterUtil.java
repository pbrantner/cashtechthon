package at.ac.tuwien.cashtechthon.util;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;

import at.ac.tuwien.cashtechthon.dtos.Classification;

public class ConverterUtil {
	private static final Object[] CLASSIFICATION_HEADER = {"customerid", "firstname", "lastname", "classifications"};

	public static String convertClassficiationToCsv(Classification classification) throws IOException {
		return convertClassficiationsToCsv(new ArrayList<Classification>(){{add(classification);}});
	}

	public static String convertClassficiationsToCsv(List<Classification> classifications) throws IOException {
		StringBuffer buffer = new StringBuffer();
		try(CSVPrinter csvFilePrinter = new CSVPrinter(buffer, CSVFormat.RFC4180)) {
			csvFilePrinter.printRecord(CLASSIFICATION_HEADER);
			for (Classification classification : classifications) {
				String customerId = classification.getCustomerId() != null ? classification.getCustomerId().toString() : "";
				String firstName = classification.getFirstName() != null ? classification.getFirstName() : "";
				String lastName = classification.getLastName() != null ? classification.getLastName() : "";

				List<String> record = new ArrayList<>(4);
				record.add(customerId);
				record.add(firstName);
				record.add(lastName);
				List<String> classficiations = classification.getClassifications();
				if(classficiations == null || classficiations.isEmpty()) {
					record.add("");
				} else {
					record.add(String.join(",", classficiations));
				}
				csvFilePrinter.printRecord(record);
			}
		}
		return buffer.toString();
	}
}

package at.ac.tuwien.cashtechthon.util;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;

import at.ac.tuwien.cashtechthon.dtos.Classification;

public class ConverterUtil {
	private static final String CLASSIFICATION_HEADER = "customerid,firstname,lastname,classifications";

	public static String convertClassficiationToCsv(Classification classification) throws IOException {
		StringBuffer buffer = new StringBuffer();
		String customerId = classification.getCustomerId() != null ? classification.getCustomerId().toString() : "";
		String firstName = classification.getFirstName() != null ? classification.getFirstName() : "";
		String lastName = classification.getLastName() != null ? classification.getLastName() : "";
		try(CSVPrinter csvFilePrinter = new CSVPrinter(buffer, CSVFormat.RFC4180)) {
			csvFilePrinter.printRecord(CLASSIFICATION_HEADER);
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
		}
		return buffer.toString();
	}
}

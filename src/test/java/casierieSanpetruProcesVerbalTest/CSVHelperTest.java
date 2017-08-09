package casierieSanpetruProcesVerbalTest;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;
import org.mockito.Mock;

import casierieSanpetruProcesVerbal.CSVHelper;

public class CSVHelperTest {
	
	@Mock
	CSVHelper csvHelperMock = new CSVHelper();
	
	@Test
	public void parseLineTest() {
		final String csvLine = "\"aa\",\"bb\",\"cc\"";
		final String separator = ",";
		final List<String> expectedCsvLineResult = new ArrayList();
		expectedCsvLineResult.add("aa");
		expectedCsvLineResult.add("bb");
		expectedCsvLineResult.add("cc");
		final List<String> csvLineResult = csvHelperMock.parseLine(csvLine, separator.charAt(0));
		assertEquals(csvLineResult, expectedCsvLineResult);
	}
}

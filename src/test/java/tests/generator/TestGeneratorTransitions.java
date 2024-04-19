package tests.generator;


import org.junit.jupiter.api.Test;
import xml.spreadsheet.XMLSpreadsheetException;
import xml.spreadsheet.XMLSpreadsheetGenerator;

import java.util.Date;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.fail;
import static tests.XmlTestUtils.executeWithTempFile;

public class TestGeneratorTransitions {
	
	@Test
	public void testInvalidTransitionSheetToCell() {
		executeWithTempFile( baos -> {
			try {
				// Don't mind here to have a warning that the resource is never closed
				@SuppressWarnings("resource")
				XMLSpreadsheetGenerator generator = new XMLSpreadsheetGenerator(baos);
				generator.startDocument();
				generator.startSheet("this will fail");
				generator.writeCell("adasf"); // not the proper place
				fail(); // Should not get here!
			} catch (XMLSpreadsheetException e) {
				assertNotNull(e);
			} catch (Throwable t) {
				fail();
			}
		});
	}
	
	@Test 
	public void testInvalidTransitionDocumentToCell() {
		executeWithTempFile( baos -> {
			try {
				// Don't mind here to have a warning that the resource is never closed
				@SuppressWarnings("resource")
				XMLSpreadsheetGenerator generator = new XMLSpreadsheetGenerator(baos);
				generator.startDocument();
				generator.writeCell("adasf"); // not the proper place
				fail(); // Should not get here!
			} catch (XMLSpreadsheetException e) {
				assertNotNull(e);
			} catch (Throwable t) {
				fail();
			}
		});
	}
	
	@Test 
	public void testInvalidTransitionSheetToDocument() {
		executeWithTempFile( baos -> {
			try {
				XMLSpreadsheetGenerator generator = new XMLSpreadsheetGenerator(baos);
				generator.startDocument();
				generator.startSheet("this will fail");
				generator.close(); // Should jump here with an XMLSpreadsheetException
				fail(); // Should not get here!
			} catch (XMLSpreadsheetException e) {
				assertNotNull(e);
			} catch (Throwable t) {
				fail();
			}
		});
	}
	
	@Test 
	public void testInvalidTransitionRowToDocument() {
		executeWithTempFile( baos -> {
			try {
				XMLSpreadsheetGenerator generator = new XMLSpreadsheetGenerator(baos);
				generator.startDocument();
				generator.startSheet("this will fail");
				generator.startRow();
				generator.close();
				fail(); // Should not get here!
			} catch (XMLSpreadsheetException e) {
				assertNotNull(e);
			} catch (Throwable t) {
				fail();
			}
		});
	}
	
	@Test 
	public void testInvalidTransitionCleanToCell() {
		executeWithTempFile( baos -> {
			try {
				// Don't mind here to have a warning that the resource is never closed
				@SuppressWarnings("resource")
				XMLSpreadsheetGenerator generator = new XMLSpreadsheetGenerator(baos);
				generator.startDocument();
				generator.writeCell(new Date());
				fail(); // Should not get here!
			} catch (XMLSpreadsheetException e) {
				assertNotNull(e);
			} catch (Throwable t) {
				fail();
			}
		});
	}
	
	@Test 
	public void testInvalidTransitionNotInitializedToCell() {
		executeWithTempFile(baos -> {
			try {
				// Don't mind here to have a warning that the resource is never closed
				@SuppressWarnings("resource")
				XMLSpreadsheetGenerator generator = new XMLSpreadsheetGenerator(baos);
				generator.writeCell(new Date());
				fail(); // Should not get here!
			} catch (XMLSpreadsheetException e) {
				assertNotNull(e);
			} catch (Throwable t) {
				fail();
			}
		});
	}
}
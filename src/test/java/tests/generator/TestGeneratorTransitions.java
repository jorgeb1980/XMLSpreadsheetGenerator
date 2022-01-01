package tests.generator;

import org.junit.Test;
import xml.spreadsheet.XMLSpreadsheetException;
import xml.spreadsheet.XMLSpreadsheetGenerator;

import java.util.Date;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.fail;
import static tests.XmlTestUtils.executeWithTempFile;

public class TestGeneratorTransitions {
	
	@Test 
	public void testInvalidTransitionSheetToCell() {
		executeWithTempFile( os -> {
			try {
				// Don't mind here to have a warning that the resource is never closed
				@SuppressWarnings("resource")
				XMLSpreadsheetGenerator generator = new XMLSpreadsheetGenerator(os);
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
		executeWithTempFile( os -> {
			try {
				// Don't mind here to have a warning that the resource is never closed
				@SuppressWarnings("resource")
				XMLSpreadsheetGenerator generator = new XMLSpreadsheetGenerator(os);
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
		executeWithTempFile( os -> {
			try {
				XMLSpreadsheetGenerator generator = new XMLSpreadsheetGenerator(os);
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
		executeWithTempFile( os -> {
			try {
				XMLSpreadsheetGenerator generator = new XMLSpreadsheetGenerator(os);
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
		executeWithTempFile( os -> {
			try {
				// Don't mind here to have a warning that the resource is never closed
				@SuppressWarnings("resource")
				XMLSpreadsheetGenerator generator = new XMLSpreadsheetGenerator(os);
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
		executeWithTempFile(os -> {
			try {
				// Don't mind here to have a warning that the resource is never closed
				@SuppressWarnings("resource")
				XMLSpreadsheetGenerator generator = new XMLSpreadsheetGenerator(os);
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
package tests.generator;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.fail;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.util.Date;

import org.junit.Test;

import xml.spreadsheet.XMLSpreadsheetException;
import xml.spreadsheet.XMLSpreadsheetGenerator;

public class TestGeneratorTransitions {
	
	@Test 
	public void testInvalidTransitionSheetToCell() {
		try {
			File file = File.createTempFile("xmlspreadsheet", ".xml");
			OutputStream os = new FileOutputStream(file);
			// Don't mind here to have a warning that the resource is never closed
			@SuppressWarnings("resource")
			XMLSpreadsheetGenerator generator = new XMLSpreadsheetGenerator(os);
			generator.startDocument();
			generator.startSheet("this will fail");
			generator.writeCell("adasf"); // not the proper place
			fail(); // Should not get here!
		}
		catch (XMLSpreadsheetException e) {
			assertNotNull(e);
		}
		catch (Throwable t) {
			fail();
		}
	}
	
	@Test 
	public void testInvalidTransitionDocumentToCell() {
		try {
			File file = File.createTempFile("xmlspreadsheet", ".xml");
			OutputStream os = new FileOutputStream(file);
			// Don't mind here to have a warning that the resource is never closed
			@SuppressWarnings("resource")
			XMLSpreadsheetGenerator generator = new XMLSpreadsheetGenerator(os);
			generator.startDocument();
			generator.writeCell("adasf"); // not the proper place
			fail(); // Should not get here!
		}
		catch (XMLSpreadsheetException e) {
			assertNotNull(e);
		}
		catch (Throwable t) {
			fail();
		}
	}
	
	@Test 
	public void testInvalidTransitionSheetToDocument() {
		try {
			File file = File.createTempFile("xmlspreadsheet", ".xml");
			OutputStream os = new FileOutputStream(file);
			XMLSpreadsheetGenerator generator = new XMLSpreadsheetGenerator(os);
			generator.startDocument();
			generator.startSheet("this will fail");
			generator.close(); // Should jump here with an XMLSpreadsheetException
			fail(); // Should not get here!
		}
		catch (XMLSpreadsheetException e) {
			assertNotNull(e);
		}
		catch (Throwable t) {
			fail();
		}
	}
	
	@Test 
	public void testInvalidTransitionRowToDocument() {
		try {
			File file = File.createTempFile("xmlspreadsheet", ".xml");
			OutputStream os = new FileOutputStream(file);
			XMLSpreadsheetGenerator generator = new XMLSpreadsheetGenerator(os);
			generator.startDocument();
			generator.startSheet("this will fail");
			generator.startRow();
			generator.close();
			fail(); // Should not get here!
		}
		catch (XMLSpreadsheetException e) {
			assertNotNull(e);
		}
		catch (Throwable t) {
			fail(); 
		}
	}
	
	@Test 
	public void testInvalidTransitionCleanToCell() {
		try {
			File file = File.createTempFile("xmlspreadsheet", ".xml");
			OutputStream os = new FileOutputStream(file);
			// Don't mind here to have a warning that the resource is never closed
			@SuppressWarnings("resource")
			XMLSpreadsheetGenerator generator = new XMLSpreadsheetGenerator(os);
			generator.startDocument();
			generator.writeCell(new Date());
			fail(); // Should not get here!
		}
		catch (XMLSpreadsheetException e) {
			assertNotNull(e);
		}
		catch (Throwable t) {
			fail();
		}
	}
	
	@Test 
	public void testInvalidTransitionNotInitializedToCell() {
		try {
			File file = File.createTempFile("xmlspreadsheet", ".xml");
			OutputStream os = new FileOutputStream(file);
			// Don't mind here to have a warning that the resource is never closed
			@SuppressWarnings("resource")
			XMLSpreadsheetGenerator generator = new XMLSpreadsheetGenerator(os);
			generator.writeCell(new Date());
			fail(); // Should not get here!
		}
		catch (XMLSpreadsheetException e) {
			assertNotNull(e);
		}
		catch (Throwable t) {
			fail();
		}
	}
	
}
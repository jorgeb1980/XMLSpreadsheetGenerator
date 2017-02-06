/**
 * 
 */
package xml.spreadsheet;

import java.util.LinkedList;
import java.util.List;

/**
 * The generator works as a state machine, as documented in the diagram state_machine.jpg							
 */
enum GeneratorState {
	INITIALIZATION, CLEAN_DOCUMENT, WRITING_SHEET, WRITING_COLUMNS, WRITING_SHEET_ROWS, WRITING_ROW, WRITING_CELL, DONE;
	
	// Possible states to go for every state
	private List<GeneratorState> states = new LinkedList<GeneratorState>();
	
	// State machine
	static {
		INITIALIZATION.states.add(CLEAN_DOCUMENT);
		
		CLEAN_DOCUMENT.states.add(WRITING_SHEET);
		CLEAN_DOCUMENT.states.add(DONE);
		
		WRITING_SHEET.states.add(WRITING_COLUMNS);
		WRITING_SHEET.states.add(WRITING_ROW);
		WRITING_SHEET.states.add(CLEAN_DOCUMENT);
		
		WRITING_COLUMNS.states.add(WRITING_SHEET);
		
		WRITING_ROW.states.add(WRITING_SHEET_ROWS);
		WRITING_ROW.states.add(WRITING_CELL);
		
		WRITING_SHEET_ROWS.states.add(WRITING_ROW);
		WRITING_SHEET_ROWS.states.add(CLEAN_DOCUMENT);
		
		
		WRITING_CELL.states.add(WRITING_ROW);
	}
	
	/**
	 * This method tells if it is possible to make a transition in the state machine 
	 * from <code>previous</code> to <code>next</code> state. 
	 * @param previous Original state
	 * @param next Next state
	 * @return The <code>next</code> state if the transition is possible
	 * @throws XMLSpreadsheetException If the transition is not possible
	 */
	public static GeneratorState validateTransition(GeneratorState previous, GeneratorState next) 
			throws XMLSpreadsheetException {
		boolean valid = previous.states.contains(next);
		// There is no valid transition from DONE state
		if (!valid) {
			// Inform valid states from the current state			
			StringBuilder sb = new StringBuilder(200);
			sb.append("Invalid transition: "); 
			sb.append(previous);
			sb.append(" -> ");
			sb.append(next);
			sb.append(System.getProperty("line.separator"));
			sb.append("Valid transitions from ");
			sb.append(previous);
			sb.append(": ");
			for (GeneratorState state: previous.states) {
				sb.append(System.getProperty("line.separator"));
				sb.append(state);
			}
			throw new XMLSpreadsheetException(sb.toString());
		}
		else {
			return next;
		}
	}
}

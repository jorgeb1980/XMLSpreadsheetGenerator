/**
 * 
 */
package xml.spreadsheet;

/**
 * The generator works as a state machine.  <br/>
 * <code>
 * INITIALIZATION -> CLEAN_DOCUMENT 
 * [ -> WRITING_SHEET 
 * 		[ -> WRITING_ROW 
 * 			[ -> WRITING_CELL -> WRITING_ROW 
 * 			]* -> WRITING_SHEET  
 * 		]* -> CLEAN_DOCUMENT
 * ]* -> DONE
 * </code>
 */
enum GeneratorState {
	INITIALIZATION, CLEAN_DOCUMENT, WRITING_SHEET, WRITING_ROW, WRITING_CELL, DONE;
	
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
		boolean valid = false;
		if (previous == INITIALIZATION) {
			valid = (next == CLEAN_DOCUMENT);
		}
		else if (previous == CLEAN_DOCUMENT) {
			valid = (next == WRITING_SHEET) || (next == DONE);
		}
		else if (previous == WRITING_SHEET) {
			valid = (next == CLEAN_DOCUMENT) || (next == WRITING_ROW);
		}
		else if (previous == WRITING_ROW) {
			valid = (next == WRITING_SHEET) || (next == WRITING_CELL);
		}
		else if (previous == WRITING_CELL) {
			valid = (next == WRITING_ROW);
		}
		// There is no valid transition from DONE state
		if (!valid) {
			throw new XMLSpreadsheetException("Invalid transition: " 
					+ previous + " -> " + next);
		}
		else {
			return next;
		}
	}
}

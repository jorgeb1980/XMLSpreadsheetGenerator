/**
 * 
 */
package xml.spreadsheet;

/**
 * The generator works as a state machine.  
 * BASE -> INITIALIZATION -> WRITING_SHEET -> SHEET_DONE [... WRITING_SHEET -> SHEET_DONE ] -> DONE
 */
public enum GeneratorState {
	BASE, INITIALIZATION, WRITING_SHEET, SHEET_DONE, DONE;
	
	/**
	 * This method controls the possible states in the machine state
	 * @param previous Original state
	 * @param next Next state
	 * @return True if the transition is possible; false in another case
	 */
	public static GeneratorState validateTransition(GeneratorState previous, GeneratorState next) 
			throws XMLSpreadsheetException {
		boolean valid = false;
		if (previous == BASE) {
			valid = next == INITIALIZATION;
		}
		else if (previous == INITIALIZATION) {
			valid = next == WRITING_SHEET;
		}
		else if (previous == WRITING_SHEET) {
			valid = next == SHEET_DONE;
		}
		else if (previous == SHEET_DONE) {
			valid = (next == WRITING_SHEET) || (next == DONE);
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

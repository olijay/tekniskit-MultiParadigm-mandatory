package dk.iha.plpa.multiparadigm;

import javax.swing.JOptionPane;

/**
 * Class for running the program and displaying error messages.
 * 
 * @author Tommy, Mikkel and Ólafur
 * 
 */
public class Main {

	/**
	 * Pops up a message with an error message if wrong input is used in scheme
	 * text fields.
	 * 
	 * @param functionExpression
	 *            The function typed in the text field
	 */
	public static void showFunctionErrorMessage(String functionExpression) {

		if (functionExpression != null && functionExpression == "") {
			JOptionPane
					.showMessageDialog(
							null,
							"Invalid Scheme function input. \nPlease write only valid Scheme functions in the function textboxes.",
							"Attention!", JOptionPane.ERROR_MESSAGE);
		} else {
			JOptionPane
					.showMessageDialog(
							null,
							"Invalid Scheme function input for function expression:\n "
									+ functionExpression
									+ " \nPlease write only valid Scheme functions in the function textboxes.",
							"Attention!", JOptionPane.ERROR_MESSAGE);
		}
	}

	/**
	 * Tests if a String can be parsed to a double.
	 * 
	 * @param value
	 *            String variable to be cast to double
	 * @return true if it can be parsed else false.
	 */
	public static boolean tryParseDouble(String value) {
		try {
			Double.parseDouble(value);
			return true;
		} catch (NumberFormatException nfe) {
			return false;
		}
	}

	/**
	 * Tests if a String can be parsed to a integer.
	 * 
	 * @param value
	 *            String variable to be cast to integer
	 * @return true if it can be parsed else false.
	 */
	public static boolean tryParseInt(String value) {
		try {
			Integer.parseInt(value);
			return true;
		} catch (NumberFormatException nfe) {
			return false;
		}
	}

	/**
	 * Main method - starts the program.
	 * 
	 * @param args
	 *            None.
	 */
	public static void main(String[] args) {
		@SuppressWarnings("unused")
		MainFrame obj = new MainFrame();
	}
}

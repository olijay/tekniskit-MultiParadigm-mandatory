package dk.iha.plpa.multiparadigm;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.border.TitledBorder;

/**
 * Panel which holds functions panels and the button to add more of them.
 * 
 * @author Tommy, Mikkel and Ólafur
 * 
 */
public class FunctionsFramePanel extends JPanel {

	private static final long serialVersionUID = 1L;
	public static final int MAX_NO_TEXTFIELDS = 5;
	public static final int MAX_NO_FUNCTIONS = 10;

	private FunctionPanel[] functionsArray = new FunctionPanel[MAX_NO_TEXTFIELDS];

	private int textFieldCounter = 0;
	private JButton addEquationTextFieldButton = new JButton(
			"Add Equation field");

	/**
	 * Constructor
	 */
	FunctionsFramePanel() {
		setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));
		setBorder(new TitledBorder("Functions"));

		// Adding text field
		for (int i = 0; i < functionsArray.length; i++) {
			functionsArray[i] = new FunctionPanel();
		}

		addFuntionPanel();

		addEquationTextFieldButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				addNewTextfields();
			}
		});
		add(addEquationTextFieldButton);
	}

	/**
	 * Add a FuntionsPanel to the panel
	 */
	private void addFuntionPanel() {
		add(functionsArray[textFieldCounter]);
		textFieldCounter++;

	}

	/**
	 * Adds FunctionPanel to the panel above the "add function" button.
	 */
	protected void addNewTextfields() {

		if (textFieldCounter < MAX_NO_TEXTFIELDS) {
			remove(addEquationTextFieldButton);
			addFuntionPanel();
			if (textFieldCounter < MAX_NO_TEXTFIELDS) {
				add(addEquationTextFieldButton);
			}
			updateUI();
		}
	}

	/**
	 * Removes all the added FuntionsPanels and clears it.
	 */
	public void clearFunctionsArray() {

		for (int i = 0; i < functionsArray.length; i++) {
			remove(functionsArray[i]);
			functionsArray[i] = new FunctionPanel();
		}
		textFieldCounter = 0;
		addNewTextfields();
	}

	public FunctionPanel[] getFunctionsArray() {
		return functionsArray;
	}

	public void setFunctionsArray(FunctionPanel[] functionsArray) {
		this.functionsArray = functionsArray;
	}
}

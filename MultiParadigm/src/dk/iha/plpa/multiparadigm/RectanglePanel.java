package dk.iha.plpa.multiparadigm;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.JCheckBox;
import javax.swing.border.TitledBorder;

import java.awt.GridLayout;

/**
 * Panel to specify the input for calculating the area below the graph using the
 * rectangle method.
 * 
 * @author Tommy, Mikkel and Ólafur
 * 
 */
public class RectanglePanel extends JPanel {

	private static final long serialVersionUID = 1L;
	private int defaultRectangleCount = 10;
	private int initialABValue = 0;
	private JTextField rectangleInputField = new JTextField(""
			+ defaultRectangleCount);
	private JTextField txtA = new JTextField("" + initialABValue);
	private JTextField txtB = new JTextField("" + initialABValue);
	private JCheckBox derivativeCheckBox = new JCheckBox("Derivative");
	private JCheckBox functionCheckBox = new JCheckBox("Function");

	/**
	 * Constructor for setting up the panel.
	 */
	RectanglePanel() {

		JLabel rectangleInputLabel = new JLabel("No. of rectangles:");
		rectangleInputField.setColumns(3);

		setBorder(new TitledBorder("Calculate area below graph"));
		setLayout(new GridLayout(4, 2, 0, 0));

		add(functionCheckBox);
		add(derivativeCheckBox);
		add(rectangleInputLabel);
		add(rectangleInputField);

		JLabel lblFrom = new JLabel("From:");
		add(lblFrom);

		add(txtA);
		txtA.setColumns(10);

		JLabel lblTo = new JLabel("To: ");
		add(lblTo);

		add(txtB);
		txtB.setColumns(10);
	}

	/**
	 * Method for setting all the values in the textfields to be the default
	 * values and uncheck all checkboxes.
	 */
	public void clearAllFields() {
		rectangleInputField.setText("" + defaultRectangleCount);
		txtA.setText("" + initialABValue);
		txtB.setText("" + initialABValue);
		derivativeCheckBox.setSelected(false);
		functionCheckBox.setSelected(false);

	}

	public JTextField getRectangleInputField() {
		return rectangleInputField;
	}

	public void setRectangleInputField(JTextField rectangleInputField) {
		this.rectangleInputField = rectangleInputField;
	}

	public JTextField getTxtA() {
		return txtA;
	}

	public void setTxtA(JTextField txtA) {
		this.txtA = txtA;
	}

	public JTextField getTxtB() {
		return txtB;
	}

	public void setTxtB(JTextField txtB) {
		this.txtB = txtB;
	}

	public JCheckBox getDerivativeCheckBox() {
		return derivativeCheckBox;
	}

	public void setDerivativeCheckBox(JCheckBox derivativeCheckBox) {
		this.derivativeCheckBox = derivativeCheckBox;
	}

	public JCheckBox getFunctionCheckBox() {
		return functionCheckBox;
	}

	public void setFunctionCheckBox(JCheckBox functionCheckBox) {
		this.functionCheckBox = functionCheckBox;
	}

}

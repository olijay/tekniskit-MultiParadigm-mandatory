package dk.iha.plpa.multiparadigm;

import javax.swing.JCheckBox;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.JLabel;
import javax.swing.border.TitledBorder;

import java.awt.Dimension;
import java.awt.GridLayout;

/**
 * Creates a panel for typing in the scheme function.
 * 
 * @author Tommy, Mikkel, Ólafur
 * 
 */
public class FunctionPanel extends JPanel {

	private static final long serialVersionUID = -6644312503995335868L;
	private JTextField functionTextField = new JTextField();
	private JCheckBox functionCheckBox = new JCheckBox("Show derivative");

	private JLabel functionCalculateareaLabel = new JLabel(
			Plotter.DEFAULT_NUMBER_STRING);
	private JLabel derivativeCalculateareaLabel = new JLabel(
			Plotter.DEFAULT_NUMBER_STRING);

	private final JPanel dataBelowPanel = new JPanel();
	private final JPanel fillerpanel = new JPanel();

	/**
	 * Constructor.
	 */
	FunctionPanel() {
		setLayout(new GridLayout(3, 2, 0, 0));
		setBorder(new TitledBorder(""));
		functionTextField.setColumns(40);
		JLabel functionTextLabel = new JLabel("Scheme functions:");
		add(functionTextLabel);
		add(functionTextField);
		add(functionCheckBox);

		add(fillerpanel);

		JLabel rectangleCalculateLabel = new JLabel("Area below graph: ");

		add(rectangleCalculateLabel);
		add(dataBelowPanel);
		dataBelowPanel.setLayout(new GridLayout(2, 2, 0, 0));
		JLabel functionLabel = new JLabel("Function:");
		JLabel derivativeLabel = new JLabel("Derivative:");
		dataBelowPanel.add(functionLabel);
		dataBelowPanel.add(functionCalculateareaLabel);
		dataBelowPanel.add(derivativeLabel);
		dataBelowPanel.add(derivativeCalculateareaLabel);

		Dimension size = new Dimension(250, 70);
		setPreferredSize(size);
		setMaximumSize(size);

	}

	public JTextField getFunctionTextField() {
		return functionTextField;
	}

	public void setFunctionTextField(JTextField functionTextField) {
		this.functionTextField = functionTextField;
	}

	public JCheckBox getFunctionCheckBox() {
		return functionCheckBox;
	}

	public void setFunctionCheckBox(JCheckBox functionCheckBox) {
		this.functionCheckBox = functionCheckBox;
	}

	public JLabel getFunctionCalculateareaLabel() {
		return functionCalculateareaLabel;
	}

	public void setFunctionCalculateareaLabel(JLabel funtionCalculateareaLabel) {
		this.functionCalculateareaLabel = funtionCalculateareaLabel;
	}

	public JLabel getDerivativeCalculateareaLabel() {
		return derivativeCalculateareaLabel;
	}

	public void setDerivativeCalculateareaLabel(
			JLabel derivativeCalculateareaLabel) {
		this.derivativeCalculateareaLabel = derivativeCalculateareaLabel;
	}
}

package dk.iha.plpa.multiparadigm;

import java.awt.*;
import java.awt.event.*;
import java.text.DecimalFormat;

import javax.swing.*;

/**
 * The Plotter class is the main Panel of the Schemer graphing tool.
 * MainFrame.java has an instance of Plotter and passes the sub-panels to it.
 * 
 * @author Tommy, Mikkel, Olafur
 */
public class Plotter extends JPanel {

	private static final long serialVersionUID = 1L;
	private PlotPanel plotPanel;
	private FunctionsFramePanel funcFramePanel;
	private SettingsPanel settingsPanel;
	public final static double DEFAULT_XY_MIN = -5;
	public final static double DEFAULT_XY_MAX = 5;
	public final static int DEFAULT_DATAPOINTS = 10;
	private RectanglePanel rectanglePanel;
	public final static String DEFAULT_NUMBER_STRING = "N/A";
	private Schemer scm = new dk.iha.plpa.multiparadigm.Schemer();

	/**
	 * The constructor of the Plotter class. Takes in the sub-panels in the
	 * application. Initializes action listeners for controls. Sets the layout.
	 * 
	 * @param plotPanelObj
	 *            The PlotPanel used.
	 * @param functionPanelObj
	 *            The FunctionPanel used.
	 * @param settingsPanelObj
	 *            The SettinsPanel used.
	 * @param rectanglePanelObj
	 *            THe RectanglePanel used.
	 * 
	 */
	Plotter(PlotPanel plotPanelObj, FunctionsFramePanel functionPanelObj,
			SettingsPanel settingsPanelObj, RectanglePanel rectanglePanelObj) {

		funcFramePanel = functionPanelObj;
		plotPanel = plotPanelObj;
		settingsPanel = settingsPanelObj;
		rectanglePanel = rectanglePanelObj;

		// add drawing action listeners
		addDrawKeyListenerToJTextField(settingsPanel.getDatapoints());
		addDrawKeyListenerToJTextField(settingsPanel.getXmax());
		addDrawKeyListenerToJTextField(settingsPanel.getXmin());
		addDrawKeyListenerToJTextField(settingsPanel.getYmax());
		addDrawKeyListenerToJTextField(settingsPanel.getYmin());

		settingsPanel.getLogCheckBox().addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				drawFunction();
				calculateWithRectangles();
			}
		});

		addActionListenersToFunctionPanels();

		// add calculating action listener
		addCalculateActionListenerToRectangleCheckbox(rectanglePanel
				.getFunctionCheckBox());
		addCalculateActionListenerToRectangleCheckbox(rectanglePanel
				.getDerivativeCheckBox());
		addCalculateKeyListenerToJTextField(rectanglePanel
				.getRectangleInputField());
		addCalculateKeyListenerToJTextField(rectanglePanel.getTxtA());
		addCalculateKeyListenerToJTextField(rectanglePanel.getTxtB());

		setLayout(new GridBagLayout());
		GridBagConstraints gbc = new GridBagConstraints();
		setBorder(BorderFactory.createEtchedBorder());
		gbc.weightx = 1.0;

		JButton clearAllButton = new JButton("Clear");
		int buttonDimension = 100;
		clearAllButton.setPreferredSize(new Dimension(buttonDimension,
				buttonDimension));
		clearAllButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				clearAll();
			}
		});

		GridBagConstraints gbc_clearButton = new GridBagConstraints();
		gbc_clearButton.insets = new Insets(0, 0, 5, 5);
		gbc_clearButton.gridx = 0;
		gbc_clearButton.gridy = 0;
		add(clearAllButton, gbc_clearButton);

		JPanel panel = new JPanel();
		GridBagConstraints gbc_panel = new GridBagConstraints();
		gbc_panel.insets = new Insets(0, 0, 5, 5);
		gbc_panel.gridx = 1;
		gbc_panel.gridy = 0;
		add(panel, gbc_panel);
		panel.setLayout(new BoxLayout(panel, BoxLayout.X_AXIS));

		panel.add(settingsPanel);
		panel.add(rectanglePanel);

		JButton drawButton = new JButton("Draw");
		drawButton.setPreferredSize(new Dimension(buttonDimension,
				buttonDimension));
		drawButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				drawFunction();
				calculateWithRectangles();
			}
		});
		GridBagConstraints gbc_drawButton = new GridBagConstraints();
		gbc_drawButton.insets = new Insets(0, 0, 5, 0);
		gbc_drawButton.gridx = 2;
		gbc_drawButton.gridy = 0;
		add(drawButton, gbc_drawButton);

	}

	/**
	 * Adds action listeners to all FunctionPanel instances found in
	 * funcFramePanel
	 */
	private void addActionListenersToFunctionPanels() {
		for (FunctionPanel e : funcFramePanel.getFunctionsArray()) {
			addDrawKeyListenerToJTextField(e.getFunctionTextField());
			e.getFunctionCheckBox().addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					drawFunction();
				}
			});
		}

	}

	/**
	 * Clears all function textboxes, resets the number of function textboxes to
	 * one. Sets default values to all fields in all panels. Clears all
	 * calculations. Clears the graphing plot.
	 */
	private void clearAll() {
		funcFramePanel.clearFunctionsArray();
		addActionListenersToFunctionPanels();
		settingsPanel.setDefaultValuesToAllFields();
		rectanglePanel.clearAllFields();
		drawFunction();

	}

	/**
	 * Adds an action listener to given JCheckbox used for rectangle checkboxes
	 * so that calculations are cleared when the checkbox is unselected and
	 * calculations are performed when it is selected.
	 * 
	 * @param element
	 *            The JCheckBox to have an action listener added to it.
	 * 
	 */
	private void addCalculateActionListenerToRectangleCheckbox(JCheckBox element) {
		element.addItemListener(new ItemListener() {

			@Override
			public void itemStateChanged(ItemEvent arg0) {
				if (arg0.getStateChange() == ItemEvent.DESELECTED
						&& !rectanglePanel.getFunctionCheckBox().isSelected()) {

					for (FunctionPanel e : funcFramePanel.getFunctionsArray()) {
						e.getFunctionCalculateareaLabel().setText(
								DEFAULT_NUMBER_STRING);
					}
				} else if (arg0.getStateChange() == ItemEvent.SELECTED
						&& rectanglePanel.getFunctionCheckBox().isSelected()) {

					calculateWithRectangles();
				}

				if (arg0.getStateChange() == ItemEvent.DESELECTED
						&& !rectanglePanel.getDerivativeCheckBox().isSelected()) {

					for (FunctionPanel e : funcFramePanel.getFunctionsArray()) {
						e.getDerivativeCalculateareaLabel().setText(
								DEFAULT_NUMBER_STRING);
					}
				} else if (arg0.getStateChange() == ItemEvent.SELECTED
						&& rectanglePanel.getDerivativeCheckBox().isSelected()) {
					calculateWithRectangles();
				}
			}
		});
	}

	/**
	 * Adds a listener to a JTextField so that the function is drawn when enter
	 * is pressed in the textbox.
	 * 
	 * @param element
	 *            JTextField to have an action listener added to it.
	 * 
	 */
	private void addDrawKeyListenerToJTextField(JTextField element) {
		element.addKeyListener(new KeyListener() {
			@Override
			public void keyPressed(KeyEvent e) {
				if (e.getKeyChar() == KeyEvent.VK_ENTER) {
					drawFunction();
					calculateWithRectangles();
				}
			}

			@Override
			public void keyReleased(KeyEvent e) {
			}

			@Override
			public void keyTyped(KeyEvent e) {
			}

		});
	}

	/**
	 * Adds action listeners to given JTextField to recalculate rectangle values
	 * 
	 * @param element
	 *            JTextField to have an action listener added to it.
	 * 
	 */
	private void addCalculateKeyListenerToJTextField(JTextField element) {
		element.addKeyListener(new KeyListener() {
			@Override
			public void keyPressed(KeyEvent e) {
				if (e.getKeyChar() == KeyEvent.VK_ENTER)
					calculateWithRectangles();
			}

			@Override
			public void keyReleased(KeyEvent e) {
			}

			@Override
			public void keyTyped(KeyEvent e) {
			}

		});
	}

	/**
	 * Plots all valid functions specified in the function textboxes in
	 * funcFramePanel
	 */
	protected void drawFunction() {

		// NOTE: MAX_NO_FUNCTIONS is twice what MAX_NO_TEXTFIELDS is since we
		// want to be able to plot the derivative of each function
		double[][] xValuesCalculated = new double[FunctionsFramePanel.MAX_NO_FUNCTIONS][];
		double[][] yValuesCalculated = new double[FunctionsFramePanel.MAX_NO_FUNCTIONS][];

		double[] x = null;
		try {
			x = scm.generateXPoints(settingsPanel.getNoOfDataPoints(),
					settingsPanel.getXMax(), settingsPanel.getXMin());
		} catch (Exception e1) {
			e1.printStackTrace();
		}

		for (int i = 0; i < funcFramePanel.getFunctionsArray().length; i++) {

			FunctionPanel e = funcFramePanel.getFunctionsArray()[i];

			if (e != null && !e.getFunctionTextField().getText().isEmpty()
					&& e.getFunctionTextField().getText().startsWith("(")
					&& e.getFunctionTextField().getText().endsWith(")")) {

				// NOTE functions must be registered in the scheme environment
				// before run

				if (scm.registerFunction(e.getFunctionTextField().getText(),
						"f" + i) && x != null) {

					double[] y = scm.evaluateFunction("f" + i, e
							.getFunctionTextField().getText(), x, settingsPanel
							.getLogCheckBox().isSelected());

					xValuesCalculated[i] = x;
					yValuesCalculated[i] = y;

					// evaluate derivative
					if (e.getFunctionCheckBox().isSelected()) {
						// NOTE functions must be registered in the scheme
						// environment before run
						scm.registerFunction(
								e.getFunctionTextField().getText(), "fd" + i);

						double[] yder = scm.evaluateFunctionDerivative(
								"fd" + i, x, settingsPanel.getLogCheckBox()
										.isSelected());

						// put coordinates for derivative into calculated values
						// we put it MAX_NO_TEXTFIELDS after the original
						// function
						// so for function at index 0 it is at index 5
						xValuesCalculated[i
								+ FunctionsFramePanel.MAX_NO_TEXTFIELDS] = x;
						yValuesCalculated[i
								+ FunctionsFramePanel.MAX_NO_TEXTFIELDS] = yder;
					}
				}
			} else if (e == null
					|| e.getFunctionTextField().getText().isEmpty()) {
				// do nothing
			} else {
				Main.showFunctionErrorMessage(e.getFunctionTextField()
						.getText());
			}

		}

		if (xValuesCalculated != null && yValuesCalculated != null) {

			// manipulate the labels if we want logarithmic scale
			if (settingsPanel.getLogCheckBox().isSelected()) {

				double[] extremes = scm.getLogExtremes(settingsPanel.getYMax(),
						settingsPanel.getYMin());
				plotPanel.setData(xValuesCalculated, yValuesCalculated,
						settingsPanel.getXMin(), settingsPanel.getXMax(),
						settingsPanel.getYMin(), settingsPanel.getYMax(),
						extremes[1], extremes[0]);

			} else {
				plotPanel.setData(xValuesCalculated, yValuesCalculated,
						settingsPanel.getXMin(), settingsPanel.getXMax(),
						settingsPanel.getYMin(), settingsPanel.getYMax());
			}

		}
	}

	/**
	 * Calculates the integral using the rectangle method for all valid
	 * functions in the textboxes in funcFramePanel
	 */
	protected void calculateWithRectangles() {

		for (int i = 0; i < funcFramePanel.getFunctionsArray().length; i++) {

			FunctionPanel e = funcFramePanel.getFunctionsArray()[i];

			if (e != null
					&& !e.getFunctionTextField().getText().isEmpty()
					&& e.getFunctionTextField().getText().startsWith("(")
					&& e.getFunctionTextField().getText().endsWith(")")
					&& Main.tryParseInt(rectanglePanel.getTxtA().getText())
					&& Main.tryParseInt(rectanglePanel.getTxtB().getText())
					&& Main.tryParseInt(rectanglePanel.getRectangleInputField()
							.getText())) {

				scm.registerFunction(e.getFunctionTextField().getText(), "f"
						+ i);

				// Get the calculated rectangle values for the function
				DecimalFormat df = new DecimalFormat("0.0000");
				boolean isDerivative;
				if (rectanglePanel.getFunctionCheckBox().isSelected()) {
					isDerivative = false;
					e.getFunctionCalculateareaLabel()
							.setText(
									""
											+ df.format(fetchRectangleData(i,
													isDerivative)));
				}

				if (rectanglePanel.getDerivativeCheckBox().isSelected()) {
					isDerivative = true;
					e.getDerivativeCalculateareaLabel()
							.setText(
									""
											+ df.format(fetchRectangleData(i,
													isDerivative)));

				}
			} else {
				// Do nothing - empty functionPanel
			}
		}
	}

	/**
	 * Calls Schemer to calculate the rectangle method approximation for the
	 * given function number (functionId)
	 * 
	 * @param functionNo
	 *            ID of the function in the current scheme environment.
	 * @param isDerivative
	 *            Boolean used to determine if we are using a derivative
	 *            function
	 * @return The area below the graph.
	 */
	private double fetchRectangleData(int functionNo, boolean isDerivative) {

		return scm.getRectangleValues("f" + functionNo, Integer
				.parseInt(rectanglePanel.getTxtA().getText()), Integer
				.parseInt(rectanglePanel.getTxtB().getText()), Integer
				.parseInt(rectanglePanel.getRectangleInputField().getText()),
				isDerivative, settingsPanel.getLogCheckBox().isSelected());
	}

}
package dk.iha.plpa.multiparadigm;

import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.border.TitledBorder;
import java.awt.GridLayout;

/**
 * Settings panel shown in the bottom of the program screen.
 * 
 * @author Tommy, Mikkel and Ólafur
 * 
 */
public class SettingsPanel extends JPanel {

	private static final long serialVersionUID = -2085190070682865743L;

	private JTextField xmin = new JTextField();
	private JTextField xmax = new JTextField();
	private JTextField ymin = new JTextField();
	private JTextField ymax = new JTextField();
	private JCheckBox logCheckBox = new JCheckBox("On/Off");
	private JTextField datapoints = new JTextField();
	private int lowScaleValue = -5;
	private int highScaleValue = 5;
	private int defaultDatapointsValue = 10;

	/**
	 * Constructor.
	 */
	public SettingsPanel() {
		JLabel lblNoOfDatapoints = new JLabel("DataPoints: ");
		JLabel lblXMin = new JLabel("X min: ", SwingConstants.RIGHT);
		JLabel lblYMin = new JLabel("Y min: ", SwingConstants.RIGHT);
		JLabel lblXMax = new JLabel("X max: ", SwingConstants.RIGHT);
		JLabel lblYMax = new JLabel("Y max: ", SwingConstants.RIGHT);
		JLabel lblToggleScale = new JLabel("Logarithmic");
		setBorder(new TitledBorder("Settings"));
		setLayout(new GridLayout(3, 4, 0, 0));
		datapoints.setText("10");
		add(lblNoOfDatapoints);
		add(datapoints);

		add(lblToggleScale);
		add(logCheckBox);

		add(lblXMax);
		setDefaultValuesToAllFields();
		add(xmax);
		add(lblYMax);
		add(ymax);

		add(lblXMin);

		add(xmin);
		add(lblYMin);
		add(ymin);

	}

	/**
	 * Sets default values to all the text fields in this panel and unchecks the
	 * log-checkbox.
	 */
	public void setDefaultValuesToAllFields() {
		xmax.setText("" + highScaleValue);
		ymax.setText("" + highScaleValue);
		xmin.setText("" + lowScaleValue);
		ymin.setText("" + lowScaleValue);
		datapoints.setText("" + defaultDatapointsValue);
		logCheckBox.setSelected(false);
	}

	/**
	 * Method for retrieving the number of datapoints specified by the user.
	 * 
	 * @return Amount of datapoints parsed to integer - or the default number of
	 *         datapoints, if the given number is invalid.
	 */
	public int getNoOfDataPoints() {
		if (Main.tryParseInt(datapoints.getText())) {
			return Integer.parseInt(datapoints.getText());
		} else {
			return Plotter.DEFAULT_DATAPOINTS;
		}
	}

	/**
	 * Method for retrieving the minimum Y value specified by the user.
	 * 
	 * @return Minimum Y value parsed to double - or the default number for
	 *         minimum Y value, if the given number is invalid.
	 */
	public Double getYMin() {
		if (Main.tryParseDouble(getYmin().getText())) {
			return Double.parseDouble(getYmin().getText());
		} else {
			return Plotter.DEFAULT_XY_MIN;
		}
	}

	/**
	 * Method for retrieving the minimum X value specified by the user.
	 * 
	 * @return Minimum X value parsed to double - or the default number for
	 *         minimum X value, if the given number is invalid.
	 */
	public Double getXMin() {
		if (Main.tryParseDouble(getXmin().getText())) {
			return Double.parseDouble(getXmin().getText());
		} else {
			return Plotter.DEFAULT_XY_MIN;
		}
	}

	/**
	 * Method for retrieving the maximum X value specified by the user.
	 * 
	 * @return Maximum X value parsed to double - or the default number for
	 *         maximum X value, if the given number is invalid.
	 */
	public Double getXMax() {
		if (Main.tryParseDouble(getXmax().getText())) {
			return Double.parseDouble(getXmax().getText());
		} else {
			return Plotter.DEFAULT_XY_MAX;
		}
	}

	/**
	 * Method for retrieving the maximum Y value specified by the user.
	 * 
	 * @return Maximum Y value parsed to double - or the default number for
	 *         maximum Y value, if the given number is invalid.
	 */
	public Double getYMax() {
		if (Main.tryParseDouble(getXmax().getText())) {
			return Double.parseDouble(getYmax().getText());
		} else {
			return Plotter.DEFAULT_XY_MAX;
		}
	}

	public JTextField getXmin() {
		return xmin;
	}

	public void setXmin(JTextField xmin) {
		this.xmin = xmin;
	}

	public JTextField getXmax() {
		return xmax;
	}

	public void setXmax(JTextField xmax) {
		this.xmax = xmax;
	}

	public JTextField getYmin() {
		return ymin;
	}

	public void setYmin(JTextField ymin) {
		this.ymin = ymin;
	}

	public JTextField getYmax() {
		return ymax;
	}

	public void setYmax(JTextField ymax) {
		this.ymax = ymax;
	}

	public JCheckBox getLogCheckBox() {
		return logCheckBox;
	}

	public void setLogCheckBox(JCheckBox logCheckBox) {
		this.logCheckBox = logCheckBox;
	}

	public JTextField getDatapoints() {
		return datapoints;
	}

	public void setDatapoints(JTextField datapoints) {
		this.datapoints = datapoints;
	}

}

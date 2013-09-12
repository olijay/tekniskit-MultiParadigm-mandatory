package dk.iha.plpa.multiparadigm;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.font.FontRenderContext;
import java.awt.font.LineMetrics;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Line2D;
import java.awt.geom.Point2D;

import javax.swing.JPanel;

/**
 * Panel which sets up the area where the graphs are drawn.
 * 
 * @author Tommy, Mikkel and Ólafur
 * 
 */
class PlotPanel extends JPanel {

	private static final long serialVersionUID = 1L;
	private double[][] x;
	private double[][] y;
	private double xMin = 0.0;
	private double xMax = 0.0;
	private double yMin = 0.0;
	private double yMax = 0.0;

	private double yMinLabel = 0.0;
	private double yMaxLabel = 0.0;

	private final int PAD = 20;
	private int h;

	/**
	 * Constructor. Sets the values of X and Y.
	 * 
	 * @param x
	 *            X values in the coordinate system.
	 * @param y
	 *            Y values in the coordinate system.
	 */
	public PlotPanel(double[][] x, double[][] y) {
		setData(x, y);
	}

	/**
	 * Default constructor.
	 */
	public PlotPanel() {
		this.xMin = Plotter.DEFAULT_XY_MIN;
		this.xMax = Plotter.DEFAULT_XY_MAX;
		this.yMax = Plotter.DEFAULT_XY_MAX;
		this.yMin = Plotter.DEFAULT_XY_MIN;

		this.yMaxLabel = yMax;
		this.yMinLabel = yMin;

		setSize(1100, 500);
		Dimension dimension = new Dimension(100, 100);
		setMaximumSize(dimension);
		setMinimumSize(dimension);
		setPreferredSize(dimension);
	}

	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		Graphics2D g2 = (Graphics2D) g;
		g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
				RenderingHints.VALUE_ANTIALIAS_ON);
		int w = getWidth();
		h = getHeight();
		double xScale = (w - 2 * PAD) / (xMax - xMin);
		double yScale = (h - 2 * PAD) / (yMax - yMin);

		Point2D.Double origin = new Point2D.Double(); // Axes origin.
		Point2D.Double offset = new Point2D.Double(); // Locate data.
		if (xMax < 0) {
			origin.x = w - PAD;
			offset.x = origin.x - xScale * xMax;
		} else if (xMin < 0) {
			origin.x = PAD - xScale * xMin;
			offset.x = origin.x;
		} else {
			origin.x = PAD;
			offset.x = PAD - xScale * xMin;
		}
		if (yMax < 0) {
			origin.y = h - PAD;
			offset.y = origin.y - yScale * yMax;
		} else if (yMin < 0) {
			origin.y = PAD - yScale * yMin;
			offset.y = origin.y;
		} else {
			origin.y = PAD;
			offset.y = PAD - yScale * yMin;
		}

		// Draw abscissa (X-axis).
		g2.draw(new Line2D.Double(PAD, h - origin.y, w - PAD, h - origin.y));
		// Draw ordinate (Y-axis).
		g2.draw(new Line2D.Double(origin.x, PAD, origin.x, h - PAD));

		// Draw axis scales
		double decimalXDistance = (xMin - Math.floor(xMin));
		double decimalYDistance = (yMin - Math.floor(yMin));
		for (int k = 0; k < xMax - xMin + 1; k++) {
			// x axis scale
			g2.fill(new Ellipse2D.Double(decimalXDistance + xScale * k + PAD, h
					- origin.y - 1, 3, 3));
		}
		for (int j = 0; j < yMax - yMin + 1; j++) {
			// y axis scale
			g2.fill(new Ellipse2D.Double(origin.x - 1, decimalYDistance + h
					- (yScale * j + PAD), 3, 3));
		}

		// Plot data, only if data is available
		if (x != null && y != null && x.length > 0 && y.length > 0) {

			double[][] yPixelPosition = new double[y.length][], xPixelPosition = new double[x.length][];

			for (int i = 0; i < x.length; i++) {
				// Change colour for the graph
				Color[] colourArray = { Color.blue, Color.green, Color.cyan,
						Color.red, Color.yellow, Color.black, Color.magenta,
						Color.orange, Color.pink, Color.white };

				// Why do we need this if check? oli 14.5.2013
				// In case an error happens and the array of colours are too
				// short
				if (x.length > colourArray.length) {
					g2.setPaint(Color.blue);
				} else {
					g2.setPaint(colourArray[i]);
				}

				if (x[i] != null && y[i] != null) {
					xPixelPosition[i] = new double[x[i].length];
					yPixelPosition[i] = new double[y[i].length];

					if (x[i] != null) {
						for (int j = 0; j < x[i].length; j++) {
							// set actual position of points

							double temp = offset.x + xScale * x[i][j];
							xPixelPosition[i][j] = temp;
							yPixelPosition[i][j] = h - offset.y + yScale
									* (-y[i][j]);

							g2.fill(new Ellipse2D.Double(
									xPixelPosition[i][j] - 2,
									yPixelPosition[i][j] - 2, 4, 4));
						}

					}

				}
				// Draw lines between points
				if (yPixelPosition[i] != null) {
					for (int k = 1; k < yPixelPosition[i].length; k++) {
						g2.drawLine((int) xPixelPosition[i][k - 1],
								(int) yPixelPosition[i][k - 1],
								(int) xPixelPosition[i][k],
								(int) yPixelPosition[i][k]);
					}
				}
			}

		}

		// Draw extreme data values.
		g2.setPaint(Color.black);
		Font font = g2.getFont();
		FontRenderContext frc = g2.getFontRenderContext();
		LineMetrics lm = font.getLineMetrics("0", frc);

		// Draw labels for the X-axis
		String s = String.format("%.1f", xMin);
		float width = (float) font.getStringBounds(s, frc).getWidth();
		double xpos = offset.x + xScale * xMin;
		g2.drawString(s, (float) xpos, (float) (h - origin.y) + lm.getAscent()); // reversed
																					// height

		s = String.format("%.1f", xMax);
		width = (float) font.getStringBounds(s, frc).getWidth();
		xpos = offset.x + xScale * xMax;
		g2.drawString(s, (float) xpos - width,
				(float) (h - origin.y) + lm.getAscent()); // reversed height

		// Draw labels for the Y-axis, but reversing the position of them,
		// because of reversed axis in JPanel
		s = String.format("%.1f", yMaxLabel);
		width = (float) font.getStringBounds(s, frc).getWidth();
		double ypos = offset.y + yScale * yMin;

		g2.drawString(s, (float) origin.x + 1, (float) ypos + lm.getAscent());
		s = String.format("%.1f", yMinLabel);
		width = (float) font.getStringBounds(s, frc).getWidth();
		ypos = offset.y + yScale * yMax;
		g2.drawString(s, (float) origin.x + 1, (float) ypos);

	}

	public void setxMin(double xMin) {
		this.xMin = xMin;
	}

	public void setxMax(double xMax) {
		this.xMax = xMax;
	}

	public void setyMin(double yMin) {
		this.yMin = yMin;
	}

	public void setyMax(double yMax) {
		this.yMax = yMax;
	}

	/**
	 * Sets the data for a plotPanel
	 * 
	 * @param x
	 *            X values for the coordinate system
	 * @param y
	 *            Y values for the coordinate system
	 * @param xmin
	 *            Minimum x value
	 * @param xmax
	 *            Maximum X value
	 * @param ymin
	 *            Minimum Y value
	 * @param ymax
	 *            Maximum Y value
	 * @param yminLabel
	 *            Label text for the Y axis
	 * @param ymaxLabel
	 *            Label text for the X axis
	 */
	public void setData(double[][] x, double[][] y, double xmin, double xmax,
			double ymin, double ymax, double yminLabel, double ymaxLabel) {
		this.xMin = xmin;
		this.xMax = xmax;
		this.yMax = ymax;
		this.yMin = ymin;
		this.yMaxLabel = ymaxLabel;
		this.yMinLabel = yminLabel;
		this.setData(x, y);
	}

	/**
	 * Sets the data for a plotPanel
	 * 
	 * @param x
	 *            X values for the coordinate system
	 * @param y
	 *            Y values for the coordinate system
	 * @param xmin
	 *            Minimum x value
	 * @param xmax
	 *            Maximum X value
	 * @param ymin
	 *            Minimum Y value
	 * @param ymax
	 *            Maximum Y value
	 */
	public void setData(double[][] x, double[][] y, double xmin, double xmax,
			double ymin, double ymax) {
		this.xMin = xmin;
		this.xMax = xmax;
		this.yMax = ymax;
		this.yMin = ymin;
		this.yMaxLabel = yMax;
		this.yMinLabel = yMin;
		this.setData(x, y);
	}

	/**
	 * Sets the data for a plotPanel
	 * 
	 * @param x
	 *            X values for the coordinate system
	 * @param y
	 *            Y values for the coordinate system
	 */
	public void setData(double[][] x, double[][] y) {
		if (x.length != y.length) {
			throw new IllegalArgumentException("x and y data arrays "
					+ "must be same length.");
		}
		this.x = x;
		this.y = y;

		if (xMin == 0.0 && xMax == 0.0 && yMin == 0.0 && yMax == 0.0) {

			double[] xVals = getExtremeValues(x);
			xMin = xVals[0];
			xMax = xVals[1];

			double[] yVals = getExtremeValues(y);
			yMin = yVals[0];
			yMax = yVals[1];

		}
		updateUI();
	}

	/**
	 * Finds the minimum and maximum values of an array
	 * 
	 * @param d
	 *            Array of arrays, where we find the extreme values
	 * @return Array of extreme values.
	 */
	private double[] getExtremeValues(double[][] d) {
		double min = Double.MAX_VALUE;
		double max = -min;
		for (int i = 0; i < d.length; i++) {
			for (int j = 0; j < d[i].length; j++) {
				if (d[i][j] < min) {
					min = d[i][j];
				}
				if (d[i][j] > max) {
					max = d[i][j];
				}
			}
		}
		return new double[] { min, max };
	}
}
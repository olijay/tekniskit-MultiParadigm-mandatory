package dk.iha.plpa.multiparadigm;

import java.text.DecimalFormat;

/**
 * A suite of automatic tests for the graphing tool Schemer.
 * 
 * @author Tommy, Mikkel, Olafur
 * 
 */
public class Tester {

	/**
	 * Main method for running the tests.
	 * 
	 * @param args
	 *            None.
	 */
	public static void main(String[] args) {
		Tester run = new Tester();
		run.oneEnvironmentTest(10000);	
		run.manyEnvironmentsTest(10000);
				
		run.generateXPointsTest(10, -5.0, 5.0);
		run.generateXPointsTest(1000, -100, 10);
	}

	/**
	 * Tests evaluating a Scheme function many times in succession, for many
	 * environments
	 * 
	 * @param count
	 *            No. of times to evaluate
	 * 
	 */
	private void manyEnvironmentsTest(int count) {
		System.out.println("Running manyEnvironmentsTest for " + count
				+ " environments and as many executions of generateXPoints");
		String functionExpression = "(* x x)";
		try {
			for (int i = 0; i <= count; i++) {
				System.out.println("Running: " + i);
				Schemer scm = new Schemer();
				scm.registerFunction(functionExpression, "f");
				scm.evaluateFunction("f", functionExpression,
						scm.generateXPoints(10000, 0.0, 10.0), false);
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		System.out.println("ManyEnvironmentsTest complete");

	}

	/**
	 * Tests evaluating a Scheme function many times in succession, using one
	 * environment
	 * 
	 * @param count
	 *            No. of times to evaluate
	 */
	private void oneEnvironmentTest(int count) {
		System.out.println("Running oneEnvironmentTest for " + count
				+ " executions of generateXPoints");
		String functionExpression = "(* x x)";
		try {
			Schemer scm = new Schemer();
			for (int i = 0; i <= count; i++) {
				System.out.println("Running: " + i);

				scm.registerFunction(functionExpression, "f");
				scm.evaluateFunction("f", functionExpression,
						scm.generateXPoints(10000, 0.0, 10.0), false);
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		System.out.println("OneEnvironmentTest complete");

	}

	/**
	 * Tests if the Scheme and Java versions of generateXPoints are equivalent
	 * 
	 * @param datapoints
	 *            No. of datapoints used.
	 * @param xmax
	 *            Maximum X value
	 * @param xmin
	 *            Minimum X values
	 * 
	 */
	private void generateXPointsTest(int datapoints, double xmin, double xmax) {
		System.out.println("Running generateXPointsTest for " + datapoints
				+ " datapoints and with xmin = " + xmin + ", xmax = " + xmax);
		Boolean equalLength = true;
		Schemer scm = new Schemer();
		double[] scmArray = scm.generateXPoints(datapoints, xmax, xmin);
		double[] javaArray = this.generateXPoints(datapoints, xmax, xmin);
		DecimalFormat myFormat = new DecimalFormat("0.000");

		try {

			// test the length
			if (scmArray.length != javaArray.length) {
				equalLength = false;
				System.out
						.println("Length is not equivalent.\nScheme array is: "
								+ scmArray.length + ", Java array: "
								+ javaArray.length);
			} else {
				System.out.println("Java and Scheme arrays are equally long.");
			}

			// test 2 most extreme values (if we have at least 4 values in both
			// arrays)
			if (javaArray.length >= 4 && scmArray.length >= 4) {
				String[] javaValues = new String[4];
				String[] scmValues = new String[4];

				javaValues[0] = myFormat.format(javaArray[0]);
				javaValues[1] = myFormat.format(javaArray[1]);
				javaValues[2] = myFormat
						.format(javaArray[javaArray.length - 2]);
				javaValues[3] = myFormat
						.format(javaArray[javaArray.length - 1]);

				scmValues[0] = myFormat.format(scmArray[0]);
				scmValues[1] = myFormat.format(scmArray[1]);
				scmValues[2] = myFormat.format(scmArray[scmArray.length - 2]);
				scmValues[3] = myFormat.format(scmArray[scmArray.length - 1]);

				for (int i = 0; i < javaValues.length; i++) {
					if (scmValues[i].compareTo(javaValues[i]) != 0) {
						System.out.println("Extreme values at index " + i
								+ " not equivalent.\nScheme value is: "
								+ scmValues[i] + ", Java value: "
								+ javaValues[i]);
					}
				}

			}

			// lets not continue if they are not of equal length, since we get
			// out of range errors otherwise
			if (equalLength) {

				for (int i = 0; i < scmArray.length; i++) {
					// round doubles down to 4 significant figures to compare
					// them

					String javaString = myFormat.format(javaArray[i]);
					String schemeString = myFormat.format(scmArray[i]);

					if (javaString.compareTo(schemeString) != 0) {
						equalLength = false;
						System.out.println("Java and Scheme X point at index "
								+ i + " not equivalent.\n" + "Scheme: "
								+ schemeString + " Java: " + javaString);
					}

				}

				if (equalLength) {
					System.out.println("All x point values are equivalent");
				}

			}
		} catch (Exception e) {

			e.printStackTrace();
		}

	}

	/**
	 * Java version of Schemer.generateXPoints()
	 * 
	 * @param datapoints
	 *            No. of datapoints used.
	 * @param xmax
	 *            Maximum X value
	 * @param xmin
	 *            Minimum X values
	 * @return The generated X values.
	 */
	private double[] generateXPoints(int datapoints, double xmax, double xmin) {

		double size = xmax - xmin;

		double interval = size / datapoints;
		double[] xvalues = new double[datapoints + 1];
		int j = datapoints;
		for (int i = 0; i < datapoints + 1; i++) {
			double newx = xmin + interval * i;
			// arrays are reversed due to scheme peculiarities, lets reverse the
			// javaArray
			xvalues[j--] = newx;
		}
		//

		return xvalues;
	}
}

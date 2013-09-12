package dk.iha.plpa.multiparadigm;

import gnu.lists.Pair;
import gnu.mapping.Environment;
import kawa.standard.Scheme;

/**
 * Contains all methods for calculations done in the Schemer graphing tool.
 * 
 * @author Tommy, Mikkel and Olafur
 * 
 */
public class Schemer {

	private Environment environment;

	/**
	 * Constructor for Schemer class. Initializes the Scheme environment and
	 * registers functions used in various calculations. The functions are:
	 * (logB x B) - B is the base of the logarithm to apply to value x
	 * (log10func f) - returns a procedure that takes 10-base logarithm of
	 * function f (derivative f) - returns a procedure that is the approximation
	 * of the derivative of function f (rectangle a b N f) - returns an
	 * approximation of the integral of function f, using the rectangle method
	 * with N rectangles on the span from a to b
	 * 
	 */
	public Schemer() {

		Scheme.registerEnvironment();
		environment = Environment.getCurrent();

		// Define the derivative function
		String derivativeExpression = "(define dx 0.001) "
				+ "(define derivative (lambda (f)(lambda (x) "
				+ "(/ (- (f (+ x dx)) (f x)) dx))))";
		Scheme.eval(derivativeExpression, environment);

		// define the logarithmic function
		String logBExpression = "(define logB " + "(lambda (x B) "
				+ "(/ (log x) (log B))))";
		Scheme.eval(logBExpression, environment);

		// define the function that logs another function's value
		String log10func = "(define log10func (lambda (f)(lambda (x) "
				+ "(/ (log x) (log 10)))))";
		Scheme.eval(log10func, environment);

		// define rectangle method functions
		String hlen = "(define hlen (lambda (a b N) (/ (- b a) N)))";
		String xn = "(define xn (lambda (a b N k) (+ a (* k (hlen a b N)))))";
		String recIter = "(define rec-iter (lambda (h a b k N fu sum) (if (= k N) sum (rec-iter h a b (+ k 1) N fu (+ sum (fu (xn a b N k)))))))";
		String rectangle = "(define rectangle (lambda (a b N fu) (letrec ((k 0)) (* (hlen a b N) (rec-iter (hlen a b N) a b k N fu 0)))))";
		Scheme.eval(hlen, environment);
		Scheme.eval(xn, environment);
		Scheme.eval(recIter, environment);
		Scheme.eval(rectangle, environment);
	}

	/**
	 * Registers a Scheme function in the current scheme environment for this
	 * instance of Schemer. Takes in a function id which is the name given to
	 * the function in the scheme environment.
	 * 
	 * @param functionExpression
	 *            String representation of the scheme function
	 * @param functionId
	 *            The id used for this function in the current Scheme
	 *            environment.
	 * @return true if registration was successful else false.
	 */
	public boolean registerFunction(String functionExpression, String functionId) {
		try {
			Scheme.eval("(define " + functionId + "  (lambda (x) "
					+ functionExpression + "))", environment);
			return true;
		} catch (Exception e) {
			Main.showFunctionErrorMessage(functionExpression);
			return false;
		}
	}

	/**
	 * Approximates an integral of functionId registered in the Scheme
	 * environment. Can return for the derivative using parameter derivative.
	 * Can return for logarithmic scales using parameter logarithmic.
	 * 
	 * @param functionId
	 * @param a
	 *            The value we are calculating from
	 * @param b
	 *            The value we are calculating to.
	 * @param rectangles
	 *            No. of rectangles used in the calculation.
	 * @param derivative
	 *            Tells if we are calculating the area for a derivative
	 *            function.
	 * @param logarithmic
	 *            Tells if we are using a logarithmic scale
	 * @return The result of the evaluation of the rectangle method for given
	 *         function id.
	 */
	public double getRectangleValues(String functionId, double a, double b,
			int rectangles, boolean derivative, boolean logarithmic) {
		double result = 0.0;

		try {
			Object functionCalcObj;

			if (logarithmic) {
				if (a <= 0) {
					a = 0.001;
				}

				if (derivative) {
					functionCalcObj = Scheme.eval("(abs (rectangle " + a + " "
							+ b + " " + rectangles + " (log10func (derivative "
							+ functionId + "))))", environment);
				} else {
					functionCalcObj = Scheme.eval("(abs (rectangle " + a + " "
							+ b + " " + rectangles + " (log10func "
							+ functionId + ")))", environment);
				}

			} else if (derivative) {
				functionCalcObj = Scheme.eval("(rectangle " + a + " " + b + " "
						+ rectangles + " (derivative " + functionId + "))",
						environment);
			} else {
				functionCalcObj = Scheme.eval("(rectangle " + a + " " + b + " "
						+ rectangles + " " + functionId + ")", environment);
			}

			if (functionCalcObj instanceof Double
					|| functionCalcObj instanceof Integer) {
				result = (Double) functionCalcObj;
			} else if (functionCalcObj instanceof gnu.math.DFloNum) {
				result = ((gnu.math.DFloNum) functionCalcObj).doubleValue();
			} else {
				System.out.println("Weird format: " + result + " - Type: "
						+ functionCalcObj.getClass());
				result = 0.0;
			}

		} catch (Exception e) {
			System.out.println("Cannot calculate rectangles");
			e.printStackTrace();
		}

		return result;
	}

	/**
	 * Generates the extremes of the Y scale when using logarithmic scales.
	 * 
	 * @param ymin
	 *            Minimum values of Y
	 * @param ymax
	 *            Maximum values of Y
	 * @return Array of extremes of Y for logarithmic scale
	 */
	public double[] getLogExtremes(Double ymin, Double ymax) {
		double[] result = new double[2];

		try {

			Object newymin = Scheme
					.eval("(expt 10 " + ymin + " )", environment);
			if (newymin instanceof Double) {
				result[0] = (double) newymin;
			} else {
				result[0] = ((gnu.math.DFloNum) newymin).doubleValue();
			}

			Object newymax = Scheme
					.eval("(expt 10 " + ymax + " )", environment);
			if (newymax instanceof Double) {
				result[1] = (double) newymax;
			} else {
				result[1] = ((gnu.math.DFloNum) newymax).doubleValue();
			}

		} catch (Throwable e) {
			System.out.println("Get log extremes fail");
			e.printStackTrace();
		}

		return result;
	}

	/**
	 * Evaluates a registered function with functionId in the current scheme
	 * environment, over the array x-values. Function expression is passed in
	 * for correct error reporting only.
	 * 
	 * @param functionId
	 *            The id of the registered scheme function
	 * @param functionExpression
	 *            String representation of the function.
	 * @param xvalues
	 *            Array of X values
	 * @param logarithmicScale
	 *            Boolean - if we are using the logarithmic scale or not.
	 * @return An array of evaluated values.
	 * 
	 */
	public double[] evaluateFunction(String functionId,
			String functionExpression, double[] xvalues,
			boolean logarithmicScale) {

		double[] yvalues = new double[xvalues.length];

		// Calculate expression
		try {
			for (int i = 0; i < xvalues.length; i++) {
				Object result;
				Object evaluatedFunctionValue = Scheme.eval("(" + functionId
						+ " " + xvalues[i] + ")", environment);

				double evaluatedFunctionValueDouble;
				if (evaluatedFunctionValue instanceof Double) {
					evaluatedFunctionValueDouble = (double) evaluatedFunctionValue;
				} else {
					evaluatedFunctionValueDouble = ((gnu.math.DFloNum) evaluatedFunctionValue)
							.doubleValue();
				}

				if (logarithmicScale) {

					Object obj = Scheme.eval("(abs "
							+ evaluatedFunctionValueDouble + ")", environment);
					if (obj instanceof Double) {
						evaluatedFunctionValueDouble = (double) obj;
					} else {
						evaluatedFunctionValueDouble = ((gnu.math.DFloNum) obj)
								.doubleValue();
					}

					result = Scheme.eval("(logB "
							+ evaluatedFunctionValueDouble + " 10)",
							environment);

				} else {
					result = evaluatedFunctionValueDouble;
				}

				if (result instanceof Double) {
					yvalues[i] = (double) result;
				} else {
					yvalues[i] = ((gnu.math.DFloNum) result).doubleValue();
				}
			}

		} catch (Throwable e) {
			Main.showFunctionErrorMessage(functionExpression);
		}
		return yvalues;
	}

	/**
	 * Evaluates the derivative of the registered function with functionId in
	 * the current scheme environment, over the array of xvalues.
	 * 
	 * @param functionId
	 *            The id of the registered scheme function
	 * @param xvalues
	 *            Array of X values
	 * @param logarithmicScale
	 *            Boolean - if we are using the logarithmic scale or not.
	 * @return an array of evaluated values.
	 * 
	 */
	public double[] evaluateFunctionDerivative(String functionId,
			double[] xvalues, boolean logarithmicScale) {

		double[] yvalues = new double[xvalues.length];

		// Calculate expression
		try {

			for (int i = 0; i < xvalues.length; i++) {
				Object result;
				if (logarithmicScale) {
					String derivEval = "((derivative " + functionId + ") "
							+ xvalues[i] + ")";
					Object deriveObj = Scheme.eval(derivEval, environment);
					double deriveVal;
					if (deriveObj instanceof Double) {
						deriveVal = (double) deriveObj;
					} else {
						deriveVal = ((gnu.math.DFloNum) deriveObj)
								.doubleValue();
					}

					// Make sure we do not take log of a negative number
					deriveObj = Scheme.eval("(abs " + deriveVal + ")",
							environment);
					if (deriveObj instanceof Double) {
						deriveVal = (double) deriveObj;
					} else {
						deriveVal = ((gnu.math.DFloNum) deriveObj)
								.doubleValue();
					}

					// calculate the log of the number
					result = Scheme.eval("(logB " + deriveVal + " 10)",
							environment);
				} else {
					result = Scheme.eval("((derivative " + functionId + ") "
							+ xvalues[i] + ")", environment);

				}

				if (result instanceof Double) {
					yvalues[i] = (double) result;
				} else {
					yvalues[i] = ((gnu.math.DFloNum) result).doubleValue();
				}
			}

		} catch (Throwable e) {
			System.out.println("evaluateFunctionDerivative fail");
			e.printStackTrace();
		}

		return yvalues;
	}

	/**
	 * Generates an array of values ranging from xmin to xmax with the number of
	 * datapoints used to find the length of the interval between the points.
	 * 
	 * @param datapoints
	 *            Number of datapoints to calculate from
	 * @param xmax
	 *            Maximum X value
	 * @param xmin
	 *            Minimum X value
	 * @return An array of double values on the x number line.
	 * 
	 */
	public double[] generateXPoints(int datapoints, Double xmax, Double xmin) {

		double[] xvalues = null;

		// (define interval (/ (- xmax xmin) datapoints))
		String intervalExpression = "(define interval (/ (- " + xmax.toString()
				+ " " + xmin.toString() + ") " + datapoints + "))";

		String makelistExpression = "(define makelist-iter "
				+ "(lambda (lst n i max) " + "(if (>= n max)  " + "lst "
				+ "(makelist-iter (cons (+ n i) lst) (+ n i) i max)))) "
				+ "(define makelist " + "(lambda (min max i) "
				+ "(makelist-iter (list min) min i max)))";

		try {
			Scheme.eval(intervalExpression, environment);
			Scheme.eval(makelistExpression, environment);
			// (makelist xmin xmax interval)
			String makelistEval = "(makelist " + xmin.toString() + " "
					+ xmax.toString() + " interval)";
			Pair result = (Pair) Scheme.eval(makelistEval, environment);
			Object[] arr = result.toArray();

			xvalues = new double[arr.length];

			for (int i = 0; i < arr.length; i++) {
				if (arr[i] instanceof Double) {
					xvalues[i] = (double) arr[i];
				} else {
					xvalues[i] = ((gnu.math.DFloNum) arr[i]).doubleValue();
				}
			}

		} catch (Throwable e) {

			// System.out.println("generateXPoints fails");
			e.printStackTrace();
			xvalues = null;
		}
		return xvalues;
	}

}

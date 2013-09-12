package dk.iha.plpa.multiparadigm;

import java.awt.BorderLayout;
import java.awt.Dimension;

import javax.swing.JFrame;

/**
 * Sets up the window used for this program.
 * 
 * @author Tommy, Mikkel and Ólafur
 * 
 */
public class MainFrame extends JFrame {

	private static final long serialVersionUID = 1L;

	/**
	 * Constructor. Sets up the window.
	 */
	MainFrame() {
		PlotPanel plotAreaPanel = new PlotPanel();
		FunctionsFramePanel ffPanel = new FunctionsFramePanel();
		SettingsPanel ssPanel = new SettingsPanel();
		RectanglePanel rrpanel = new RectanglePanel();
		Plotter plotFrame = new Plotter(plotAreaPanel, ffPanel, ssPanel,
				rrpanel);

		Dimension size = new Dimension(300, 10);
		ffPanel.setPreferredSize(size);
		ffPanel.setMaximumSize(size);

		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		setLayout(new BorderLayout());
		add(plotFrame, BorderLayout.SOUTH);
		add(plotAreaPanel, BorderLayout.CENTER);
		add(ffPanel, BorderLayout.WEST);

		setTitle("Multi-Paradigm Program - By Ólafur, Tommy & Mikkel");
		setSize(1300, 700);
		setLocation(50, 50);
		setVisible(true);
	}

}

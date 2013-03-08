package camrto.wa.fm.bash;

import javax.swing.JPanel;
import javax.swing.JFrame;

import org.math.plot.Plot2DPanel;

import java.awt.Graphics;
import java.util.Random;

class PolynomialPlot extends JPanel {

	public static void main(String[] args) throws Exception {
		int n=10;
		double[] x=new double[n];//{0,1,2,3,4,5};
		double[] y=new double[n];//{0,1,2,3,4,5};
		for(int i=0;i<n;i++){
			x[i]=i;
			y[i]=new Random().nextDouble()*500;
		}
		PolRegresion pl=new PolRegresion(x,y,100);
		
		Plot2DPanel plot = new Plot2DPanel();

		// add a line plot to the PlotPanel
		plot.addLinePlot("my plot", x, y);

		// put the PlotPanel in a JFrame, as a JPanel
		JFrame frame = new JFrame("a plot panel");
		frame.setContentPane(plot);
		frame.setVisible(true);
	}
}
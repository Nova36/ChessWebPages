package Graphs;
import org.jfree.data.xy.XYDataset;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;

public class DataSheet {

	public XYDataset createDataset() {

		final XYSeries series1 = new XYSeries("Wins");
		series1.add(1.0, 15.0);
		series1.add(2.0, 45.0);
		series1.add(3.0, 100.0);
		series1.add(4.0, 210.0);
		series1.add(5.0, 300.0);
		series1.add(6.0, 500.0);
		series1.add(7.0, 746.0);
		series1.add(8.0, 1050.0);
		series1.add(9.0, 1350.0);
		series1.add(10.0, 1560.0);
		series1.add(11.0, 1790.0);
		series1.add(12.0, 2550.0);

		final XYSeriesCollection dataset = new XYSeriesCollection();
		dataset.addSeries(series1);

		return dataset;

	}
}

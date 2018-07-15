package au.com.rsutton.rollingaverage;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import com.vaadin.event.UIEvents.PollEvent;
import com.vaadin.event.UIEvents.PollListener;
import com.vaadin.server.Sizeable.Unit;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;

import at.downdrown.vaadinaddons.highchartsapi.Colors;
import at.downdrown.vaadinaddons.highchartsapi.HighChart;
import at.downdrown.vaadinaddons.highchartsapi.HighChartFactory;
import at.downdrown.vaadinaddons.highchartsapi.exceptions.HighChartsException;
import at.downdrown.vaadinaddons.highchartsapi.exceptions.NoChartTypeException;
import at.downdrown.vaadinaddons.highchartsapi.model.ChartConfiguration;
import at.downdrown.vaadinaddons.highchartsapi.model.ChartType;
import at.downdrown.vaadinaddons.highchartsapi.model.series.HighChartsSeries;
import at.downdrown.vaadinaddons.highchartsapi.model.series.LineChartSeries;

public enum PowerMonitor implements Runnable
{
	SELF;

	RollingAverage ra30days = new RollingAverage("30 Days", 30);
	RollingAverage raday = new RollingAverage("1 Day", 1440);
	RollingAverage rahour = new RollingAverage("1 Hour", 60);
	RollingAverage ra5minute = new RollingAverage("5 Minute", 5);
	RollingAverage ra15minute = new RollingAverage("15 Minute", 15);
	RollingAverage ra30minute = new RollingAverage("30 Minute", 30);

	RollingAverage ra60hour = new RollingAverage("60 Hour", 60);

	RollingAverage raminute = new RollingAverage("1 Minute", 60);

	Object sync = new Object();

	AtomicInteger lastValue = new AtomicInteger(0);

	private PowerMonitor()
	{
		raminute.addCascadeListener(ra5minute);
		raminute.addCascadeListener(ra15minute);
		raminute.addCascadeListener(ra30minute);
		raminute.addCascadeListener(rahour);
		raminute.addCascadeListener(raday);
		rahour.addCascadeListener(ra60hour);
		raday.addCascadeListener(ra30days);
		new Thread(this).start();
	}

	List<RollingAverage> getAverages()
	{
		List<RollingAverage> averages = new LinkedList<>();
		averages.add(raminute);
		averages.add(ra5minute);
		averages.add(ra15minute);
		averages.add(ra30minute);
		averages.add(rahour);
		averages.add(raday);
		averages.add(ra30days);
		return averages;
	}

	public HorizontalLayout getVaadinLayout()
	{
		final HorizontalLayout layout = new HorizontalLayout();

		final HighChart lineChart = buildGraph();

		layout.addComponent(lineChart);
		layout.setExpandRatio(lineChart, 1);
		final VerticalLayout vlayout = new VerticalLayout();
		vlayout.setWidth("200");
		layout.addComponent(vlayout);

		final AtomicInteger counter = new AtomicInteger();

		layout.setSizeFull();
		UI.getCurrent().setPollInterval(1000);
		UI.getCurrent().addPollListener(new PollListener()
		{

			/**
			 * 
			 */
			private static final long serialVersionUID = 4774483203404680969L;

			@Override
			public void poll(PollEvent event)
			{

				vlayout.removeAllComponents();

				if (counter.get() % 5 == 0)
				{
					HighChartsSeries secondLine = lineChart.getChartConfiguration().getSeriesList().get(0);
					HighChartsSeries minuteLine = lineChart.getChartConfiguration().getSeriesList().get(1);
					HighChartsSeries hourLine = lineChart.getChartConfiguration().getSeriesList().get(2);

					secondLine.clearData();
					minuteLine.clearData();
					hourLine.clearData();
					for (double v : raminute.getData())
					{
						secondLine.addData((int) v);
					}
					for (double v : rahour.getData())
					{
						minuteLine.addData((int) v);
					}
					for (double v : ra60hour.getData())
					{
						hourLine.addData((int) v);
					}
					try
					{
						lineChart.redraw(lineChart.getChartConfiguration());
					} catch (HighChartsException e)
					{
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
				counter.incrementAndGet();

				vlayout.addComponent(new Label("Now: " + lastValue.get() + " Wh"));
				for (RollingAverage ra : getAverages())
				{
					synchronized (sync)
					{

						vlayout.addComponent(new Label(ra.getDescription() + " " + ra.getAverageInt() + " Wh"));

					}
				}

			}

		});

		return layout;
	}

	private HighChart buildGraph()
	{
		VerticalLayout chartLayout = new VerticalLayout();
		chartLayout.setSizeFull();
		// *** LINE ***
		ChartConfiguration lineConfiguration = new ChartConfiguration();
		lineConfiguration.setTitle("TestLine");
		lineConfiguration.setChartType(ChartType.LINE);
		lineConfiguration.setBackgroundColor(Colors.WHITE);

		LineChartSeries bananaLine = new LineChartSeries("1 Second");
		LineChartSeries minuteLine = new LineChartSeries("1 Minute");
		LineChartSeries hourLine = new LineChartSeries("1 Hour");

		lineConfiguration.getSeriesList().add(bananaLine);
		lineConfiguration.getSeriesList().add(minuteLine);
		lineConfiguration.getSeriesList().add(hourLine);

		HighChart lineChart = null;
		try
		{
			lineChart = HighChartFactory.renderChart(lineConfiguration);
			lineChart.setHeight(80, Unit.PERCENTAGE);
			lineChart.setWidth(80, Unit.PERCENTAGE);
			System.out.println("LineChart Script : " + lineConfiguration.getHighChartValue());
			chartLayout.addComponent(lineChart);
			chartLayout.setComponentAlignment(lineChart, Alignment.MIDDLE_CENTER);

		} catch (NoChartTypeException e)
		{
			e.printStackTrace();
		} catch (HighChartsException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return lineChart;

	}

	@Override
	public void run()
	{
		InputStream is = null;
		BufferedReader br = null;
		long lastValueReceived = System.currentTimeMillis() - 1000;

		try
		{

			is = System.in;

			br = new BufferedReader(new InputStreamReader(is), 1);

			String line = null;

			while ((line = br.readLine()) != null)
			{
				if (line.equalsIgnoreCase("quit"))
				{
					break;
				}
				System.out.println("Line entered : " + line);
				try
				{
					double value = Double.parseDouble(line);
					double elapsed = System.currentTimeMillis() - lastValueReceived;
					lastValueReceived = System.currentTimeMillis();
					int count = Math.max((int) (elapsed / 1000), 1);

					lastValue.set((int) value);
					// value = value * (1000d / elapsed);
					synchronized (sync)
					{
						for (int i = 0; i < count; i++)
						{
							raminute.addValue(value);
						}

					}
				} catch (NumberFormatException e)
				{

				}
			}

		} catch (IOException ioe)
		{
			System.out.println("Exception while reading input " + ioe);
		} finally
		{
			// close the streams using close method
			try
			{
				if (br != null)
				{
					br.close();
				}
			} catch (IOException ioe)
			{
				System.out.println("Error while closing stream: " + ioe);
			}

		}

	}

	public double getOneMinuteAverage()
	{
		return raminute.getAverage();
	}
}

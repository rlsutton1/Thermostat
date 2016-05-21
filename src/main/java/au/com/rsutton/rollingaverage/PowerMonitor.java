package au.com.rsutton.rollingaverage;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.LinkedList;
import java.util.List;

import com.vaadin.event.UIEvents.PollEvent;
import com.vaadin.event.UIEvents.PollListener;
import com.vaadin.ui.Label;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;

public enum PowerMonitor implements Runnable
{
	SELF;

	final static RollingAverage ra30days = new RollingAverage("30 Days", 30, null);
	final static RollingAverage raday = new RollingAverage("1 Day", 24, null);
	final static RollingAverage rahour = new RollingAverage("1 Hour", 60, raday);

	final static RollingAverage raminute = new RollingAverage("1 Minute", 60, rahour);

	final static Object sync = new Object();

	private PowerMonitor()
	{
		new Thread(this).start();
	}

	List<RollingAverage> getAverages()
	{
		List<RollingAverage> averages = new LinkedList<>();
		averages.add(raminute);
		averages.add(rahour);
		averages.add(raday);
		averages.add(ra30days);
		return averages;
	}

	public VerticalLayout getVaadinLayout()
	{
		final VerticalLayout layout = new VerticalLayout();
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
				layout.removeAllComponents();
				for (RollingAverage ra : getAverages())
				{
					synchronized (sync)
					{

						layout.addComponent(new Label(ra.getDescription() + " " + ra.getAverageInt()));

					}
				}

			}
		});

		return layout;
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
			br = new BufferedReader(new InputStreamReader(is));

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
					if (elapsed == 0)
					{
						elapsed = 1000;
					}

					//value = value * (1000d / elapsed);
					synchronized (sync)
					{
						raminute.addValue(value);

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
}

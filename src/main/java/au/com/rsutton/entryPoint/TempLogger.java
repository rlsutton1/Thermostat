package au.com.rsutton.entryPoint;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.Date;

import au.com.rsutton.rollingaverage.PowerMonitor;

public class TempLogger implements Runnable
{

	@Override
	public void run()
	{

		SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd HH:mm");
		try (PrintWriter out = new PrintWriter(new BufferedWriter(new FileWriter("temperatureLog.csv", true))))
		{
			out.println(sdf.format(new Date()) + "," + Monitor.getCurrentTemp().intValue() + ","
					+ Trigger.getSetTemperature() + "," + ForecastReader.getTemperature() + ","
					+ PowerMonitor.SELF.getOneMinuteAverage());
		} catch (IOException e)
		{
			e.printStackTrace();
		}

	}

}

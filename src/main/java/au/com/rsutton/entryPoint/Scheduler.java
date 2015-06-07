package au.com.rsutton.entryPoint;

import java.util.Date;

public class Scheduler implements Runnable
{

	static int startHour;
	static int endHour;

	boolean offState = false;

	public static void setStart(int start)
	{
		startHour = start;
	}

	public static void setEnd(int end)
	{
		endHour = end;
	}

	public static int getEndHour()
	{
		return endHour;
	}
	
	public static int getStartHour()
	{
		return startHour;
	}
	
	@Override
	public void run()
	{
		int hour = new Date().getHours();
		if (startHour > endHour)
		{
			if (hour >= startHour || hour < endHour)
			{
				offState = true;
				Trigger.setTemperature(14);
			} else
			{
				if (offState)
				{
					offState = false;
					Trigger.setTemperature(23);
				}
			}
		} else
		{
			if (hour >= startHour && hour < endHour)
			{
				offState = true;
				Trigger.setTemperature(14);
			} else
			{
				if (offState)
				{
					offState = false;
					Trigger.setTemperature(23);
				}
			}
		}
	}

}

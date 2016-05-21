package au.com.rsutton.entryPoint;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.Date;
import java.util.List;
import java.util.concurrent.ConcurrentLinkedQueue;

import au.com.rsutton.vaadin.TempSchedule;

public class Scheduler implements Runnable
{

	private static ConcurrentLinkedQueue<TempSchedule> schedules = new ConcurrentLinkedQueue<>();

	@SuppressWarnings("deprecation")
	@Override
	public void run()
	{

		for (TempSchedule schedule : schedules)
		{
			Date now = new Date();
			if (schedule.hour == now.getHours()
					&& schedule.minute == now.getMinutes()
					&& checkDay(schedule))
			{
				Trigger.setTemperature(schedule.temp);

			}
		}

	}

	private boolean checkDay(TempSchedule schedule)
	{
		@SuppressWarnings("deprecation")
		int day = new Date().getDay();
		switch (day)
		{
		case 0:
			return schedule.sun;
		case 1:
			return schedule.mon;
		case 2:
			return schedule.tue;
		case 3:
			return schedule.wed;
		case 4:
			return schedule.thur;
		case 5:
			return schedule.fri;
		case 6:
			return schedule.sat;
		}
		return false;
	}

	public static void setSchedules(List<TempSchedule> pschedules)
	{
		schedules.clear();
		schedules.addAll(pschedules);

	}

	public static ConcurrentLinkedQueue<TempSchedule> getScheuleList()
	{
		return schedules;
	}

	public static void save()
	{
		try (ObjectOutputStream stream = new ObjectOutputStream(
				new FileOutputStream("save.obj")))
		{

			for (TempSchedule schedule : schedules)
			{
				stream.writeObject(schedule);
			}
		} catch (Exception e)
		{
			e.printStackTrace();

		}

	}

	public static void load()
	{
		try (ObjectInputStream stream = new ObjectInputStream(
				new FileInputStream("save.obj")))
		{

			TempSchedule schedule = (TempSchedule) stream.readObject();
			while (schedule != null)
			{
				schedules.add(schedule);

				schedule = (TempSchedule) stream.readObject();
			}

		} catch (Exception e)
		{
			e.printStackTrace();

		}

	}

}

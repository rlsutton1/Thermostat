package au.com.rsutton.entryPoint;

import java.util.Date;
import java.util.List;
import java.util.concurrent.ConcurrentLinkedQueue;

import au.com.rsutton.vaadin.TempSchedule;

public class Scheduler implements Runnable {

	private static ConcurrentLinkedQueue<TempSchedule> schedules = new ConcurrentLinkedQueue<>();

	@Override
	public void run() {

		for (TempSchedule schedule : schedules) {
			Date now = new Date();
			if (schedule.hour == now.getHours()
					&& schedule.minute == now.getMinutes()) {
				Trigger.setTemperature(schedule.temp);

			}
		}

	}

	public static void setSchedules(List<TempSchedule> pschedules) {
		schedules.clear();
		schedules.addAll(pschedules);

	}

}

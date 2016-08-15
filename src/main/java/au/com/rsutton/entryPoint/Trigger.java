package au.com.rsutton.entryPoint;

import com.pi4j.io.gpio.PinMode;
import com.pi4j.io.gpio.PinState;

public class Trigger implements Runnable
{

	private Monitor monitor;
	static volatile private double setTemperature = 18;
	private GrovePi grove;

	Trigger(GrovePi grove, Monitor monitor)
	{
		grove.setMode(GrovePiPin.GPIO_D7, PinMode.DIGITAL_OUTPUT);
		grove.setMode(GrovePiPin.GPIO_D8, PinMode.DIGITAL_OUTPUT);

		this.monitor = monitor;
		this.grove = grove;
	}

	static public void setTemperature(double temperature)
	{
		setTemperature = temperature;
	}

	static public double getSetTemperature()
	{
		return setTemperature;
	}

	@Override
	public void run()
	{
		try
		{
			double actualTemperature = monitor.getTemperature();
			System.out.println(actualTemperature + " " + setTemperature);
			if (actualTemperature > setTemperature + 0.35)
			{
				turnOff();
			}
			if (actualTemperature < setTemperature - 0.35)
			{
				turnOn();
			}

		} catch (Exception e)
		{
			e.printStackTrace();
		}
	}

	private void turnOn()
	{
		System.out.println("On");

		grove.setState(GrovePiPin.GPIO_D8, PinState.HIGH);

	}

	private void turnOff()
	{
		// TODO Auto-generated method stub
		System.out.println("Off");

		grove.setState(GrovePiPin.GPIO_D8, PinState.LOW);

	}

}

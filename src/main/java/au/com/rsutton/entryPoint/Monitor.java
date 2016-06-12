package au.com.rsutton.entryPoint;

import java.util.concurrent.atomic.AtomicReference;

import au.com.rsutton.entryPoint.GrovePiProvider.DHTType;

public class Monitor implements Runnable
{
	static final AtomicReference<Double> currentTemperature = new AtomicReference<>();
	final private GrovePi grove;

	public Monitor(GrovePi grove)
	{
		this.grove = grove;
		currentTemperature.set(18d);
	}

	@Override
	public void run()
	{
		Double currentTempValue = readThermometer();
		if (!currentTempValue.isInfinite() && !currentTempValue.isNaN())
		{
			currentTemperature.set((currentTemperature.get() * 0.75) + (currentTempValue * 0.25));
		}
	}

	public double getTemperature()
	{
		return currentTemperature.get();
	}

	private double readThermometer()
	{

		try
		{
			// was DHT11
			return grove.readDHT(GrovePiPin.GPIO_D3.getAddress(), DHTType.DHT22) - 1.0d;
		} catch (Exception e)
		{
			e.printStackTrace();
		}
		return 0;
		// return new Random().nextInt(10) + 14;
	}

	public static Double getCurrentTemp()
	{
		return currentTemperature.get();
	}

}

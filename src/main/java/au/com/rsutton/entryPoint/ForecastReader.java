package au.com.rsutton.entryPoint;

import java.io.IOException;

import org.bitpipeline.lib.owm.OwmClient;
import org.bitpipeline.lib.owm.WeatherData;
import org.bitpipeline.lib.owm.WeatherStatusResponse;
import org.json.JSONException;

import com.google.common.util.concurrent.AtomicDouble;

public class ForecastReader implements Runnable
{

	static final AtomicDouble currentTemp = new AtomicDouble(273.15 + 18);

	public static int getTemperature()
	{
		return (int) (currentTemp.get() - 273.15);
	}

	@Override
	public void run()
	{
		OwmClient owm = new OwmClient();
		WeatherStatusResponse currentWeather;
		try
		{

			currentWeather = owm.currentWeatherAtCity("Melbourne", "AU");

			if (currentWeather.hasWeatherStatus())
			{
				WeatherData weather = currentWeather.getWeatherStatus().get(0);

				if (weather.hasMain())
				{
					currentTemp.set(weather.getMain().getTemp());
				}

			}
		} catch (IOException | JSONException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}

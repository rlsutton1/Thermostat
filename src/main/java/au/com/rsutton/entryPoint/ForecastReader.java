package au.com.rsutton.entryPoint;

import java.io.IOException;

import org.bitpipeline.lib.owm.OwmClient;
import org.bitpipeline.lib.owm.WeatherData;
import org.bitpipeline.lib.owm.WeatherForecastResponse;
import org.bitpipeline.lib.owm.WeatherStatusResponse;
import org.json.JSONException;

public class ForecastReader implements Runnable
{

	static volatile double currentTemp = 273.15 + 18;

	public static int getTemperature()
	{
		return (int) (currentTemp - 273.15);
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
					currentTemp = weather.getTemp();
				}

			}
		} catch (IOException | JSONException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}

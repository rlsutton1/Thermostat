package au.com.rsutton.rollingaverage;

import java.util.LinkedList;
import java.util.List;

public class RollingAverage
{

	List<Double> values = new LinkedList<>();
	int sampleSize = 100;
	RollingAverage cascadeListener;
	private String description;

	public RollingAverage(String description, int sampleSize, RollingAverage cascadeListener)
	{
		this.sampleSize = sampleSize;
		this.cascadeListener = cascadeListener;
		this.description = description;
	}

	public String getDescription()
	{
		return description;
	}
	
	public void addValue(double value)
	{

		if (values.size() == sampleSize)
		{

			if (cascadeListener != null)
			{
				cascadeListener.addValue(getAverage());
			}
			values.remove(0);
		}
		values.add(value);
	}

	public double getAverage()
	{
		double total = 0;
		for (Double value : values)
		{
			total += value;
		}
		return total / Math.max(1, values.size());
	}

	public String getAverageInt()
	{
		return ""+((int)getAverage());
	}
}

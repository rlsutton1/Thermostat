package au.com.rsutton.rollingaverage;

import java.util.LinkedList;
import java.util.List;

import com.vaadin.ui.Component;

public class RollingAverage implements RollingAverageCascadeListener
{

	List<Double> values = new LinkedList<>();
	int sampleSize = 100;
	List<RollingAverageCascadeListener> cascadeListeners = new LinkedList<>();
	private String description;

	public RollingAverage(String description, int sampleSize)
	{
		this.sampleSize = sampleSize;

		this.description = description;
	}

	void addCascadeListener(RollingAverageCascadeListener cascadeListener)
	{
		cascadeListeners.add(cascadeListener);
	}

	public String getDescription()
	{
		return description;
	}

	int counter = 0;

	public void addValue(double value)
	{
		
		if (values.size() == sampleSize)
		{

			if (counter % sampleSize == 0)
			{
				for (RollingAverageCascadeListener cascadeListener : cascadeListeners)
				{
					cascadeListener.addValue(getAverage());
				}
			}
			values.remove(0);
			counter++;
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
		return "" + ((int) getAverage());
	}

	public List<Double> getData()
	{
		return values;
	}
}

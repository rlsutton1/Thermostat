package au.com.rsutton.rollingaverage;

import java.util.Random;

import org.junit.Test;

public class RollingAverageTest
{

	@Test
	public void test()
	{
		Random rand = new Random();
		RollingAverage ra = new RollingAverage("",60, null);

		for (int i = 0; i < 120; i++)
		{
			ra.addValue(rand.nextInt(100));
			System.out.println(ra.getAverage());
		}

	}

	@Test
	public void testCascade()
	{
		Random rand = new Random();
		RollingAverage rahour = new RollingAverage("",60, null);

		RollingAverage raminute = new RollingAverage("",60, rahour);

		for (int i = 0; i < 600; i++)
		{
			raminute.addValue(rand.nextInt(100));
			if (i % 30 == 0)
			{
				System.out.println("minute "+raminute.getAverage());
				System.out.println("hour "+rahour.getAverage());
			}
		}

	}

}

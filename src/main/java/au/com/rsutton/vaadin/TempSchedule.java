package au.com.rsutton.vaadin;

import java.io.Serializable;

public class TempSchedule implements Serializable
{

	/**
	 * 
	 */
	private static final long serialVersionUID = -6429199167175668949L;
	public TempSchedule()
	{

	}

	public int hour = 0;
	public int minute = 0;
	public int temp = 18;
	public boolean sun = true;
	public boolean mon = true;
	public boolean tue = true;
	public boolean wed = true;
	public boolean thur = true;
	public boolean fri = true;
	public boolean sat = true;
}

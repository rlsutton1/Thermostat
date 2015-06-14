package au.com.rsutton.vaadin;

import com.vaadin.ui.ComboBox;

public class RangeCombo extends ComboBox
{

	private static final long serialVersionUID = -942405229106829942L;

	RangeCombo(String caption, int min, int max)
	{
		this(caption,min,max,1);
	}
	
	public RangeCombo(String caption, int min, int max, int step)
	{
		setCaption(caption);
		for (int i = min; i <= max; i+=step)
		{
			addItem(i);
		}

		setItemCaptionMode(ItemCaptionMode.ID);
	}

	@Override
	public void setValue(Object newValue)
			throws com.vaadin.data.Property.ReadOnlyException
	{
		int i=0;
		if (newValue instanceof Double)
		{
			i = ((Double) newValue).intValue();
		}
		super.setValue(i);
	}

}

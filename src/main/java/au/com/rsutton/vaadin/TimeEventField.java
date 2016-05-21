package au.com.rsutton.vaadin;

import com.vaadin.data.Property;
import com.vaadin.data.Property.ValueChangeEvent;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.CheckBox;
import com.vaadin.ui.Component;
import com.vaadin.ui.HorizontalLayout;

public class TimeEventField extends HorizontalLayout
{

	private RangeCombo hourField;
	private RangeCombo minuteField;
	private RangeCombo tempField;
	private CheckBox sun;
	private CheckBox mon;
	private CheckBox wed;
	private CheckBox thur;
	private CheckBox fri;
	private CheckBox sat;
	private CheckBox tue;
	private TempSchedule schedule;

	public TimeEventField(TempSchedule s1)
	{
		// TODO Auto-generated constructor stub

		this.schedule = s1;
		setWidth("500");

		int hour = s1.hour;
		int minute = s1.minute;
		int temp = s1.temp;

		createDays(s1);

		hourField = new RangeCombo("Hour", 0, 23);
		hourField.setValue((double) hour);
		minuteField = new RangeCombo("Minute", 0, 59, 5);
		minuteField.setValue((double) minute);
		tempField = new RangeCombo("Temp", 10, 24);
		tempField.setValue((double) temp);
		addComponent(hourField);
		addComponent(minuteField);
		addComponent(tempField);
		tempField.setWidth("90%");
		hourField.setWidth("90%");
		minuteField.setWidth("90%");

	}

	private void createDays(TempSchedule s1)
	{

		sun =  setUpDayField("Su",s1.sun);
		mon = setUpDayField("M",s1.mon);
		tue = setUpDayField("T",s1.tue);
		wed = setUpDayField("W",s1.wed);
		thur = setUpDayField("Th",s1.thur);
		fri = setUpDayField("F",s1.fri);
		sat = setUpDayField("Sa",s1.sat);

		
	}
	
	private CheckBox setUpDayField(String caption,Boolean value)
	{
		CheckBox day = new CheckBox(caption);
		day.setValue(value);
		addComponent(day);
		setComponentAlignment(day, Alignment.BOTTOM_CENTER);
		return day;
		
	}

	private Property.ValueChangeListener updater(final Component component,
			final String caption)
	{
		return new Property.ValueChangeListener()
		{

			@Override
			public void valueChange(ValueChangeEvent event)
			{
				Double value;
				if (event.getProperty().getValue() instanceof Integer)
				{
					value = ((Integer) event.getProperty().getValue())
							.doubleValue();
				} else
				{
					value = (Double) event.getProperty().getValue();
				}
				component.setCaption(caption + " " + value.intValue());

			}
		};
	}

	public void save()
	{

		schedule.hour = ((int) hourField.getValue());
		schedule.minute = ((int) minuteField.getValue());
		schedule.temp = ((int) tempField.getValue());
		schedule.sun = sun.getValue();
		schedule.mon = mon.getValue();
		schedule.tue = tue.getValue();
		schedule.wed = wed.getValue();
		schedule.thur = thur.getValue();
		schedule.fri = fri.getValue();
		schedule.sat = sat.getValue();

	}

}

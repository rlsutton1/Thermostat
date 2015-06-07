package au.com.rsutton.vaadin;

import au.com.rsutton.entryPoint.Scheduler;

import com.vaadin.data.Validator;
import com.vaadin.data.Property.ValueChangeEvent;
import com.vaadin.data.Property.ValueChangeListener;
import com.vaadin.data.Validator.InvalidValueException;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.TextField;

public class TimeComponent extends HorizontalLayout
{

	private TextField onTimeField;

	TimeComponent(String caption,int defaultValue)
	{
		createNightModeStartTimeField();
		
		onTimeField.setCaption(caption);
		onTimeField.setValue("" + defaultValue);
		
		addComponent(onTimeField);

	}

	int getValue()
	{
		return Integer.parseInt(onTimeField.getValue());
	}

	private void createNightModeStartTimeField()
	{
		onTimeField = new TextField();
		onTimeField.setValue("" + Scheduler.getStartHour());
		onTimeField.setImmediate(true);
		onTimeField.addValidator(getIntegerValidator(onTimeField));
		onTimeField.addValueChangeListener(new ValueChangeListener()
		{

			@Override
			public void valueChange(ValueChangeEvent event)
			{
				Scheduler.setStart(Integer.parseInt((String) event
						.getProperty().getValue()));

			}
		});
	}

	private Validator getIntegerValidator(final TextField field)
	{
		return new Validator()
		{

			@Override
			public void validate(Object value) throws InvalidValueException
			{
				try
				{
					Integer.parseInt((String) value);
				} catch (NumberFormatException e)
				{
					throw new InvalidValueException(
							"Time must be a whone number between 0 and 23");
				}
			}
		};
	}

}

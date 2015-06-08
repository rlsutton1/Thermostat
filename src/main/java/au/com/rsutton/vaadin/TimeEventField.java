package au.com.rsutton.vaadin;

import com.vaadin.data.Property;
import com.vaadin.data.Property.ValueChangeEvent;
import com.vaadin.ui.Component;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Slider;

public class TimeEventField extends HorizontalLayout {

	private Slider hourField;
	private Slider minuteField;
	private Slider tempField;



	public TimeEventField(TempSchedule s1) {
		// TODO Auto-generated constructor stub
		
		setWidth("300");
		
		int hour = s1.hour;
		int minute = s1.minute;
		int temp = s1.temp;
		
		 hourField = new Slider("Hour " + hour, 0, 23);
		hourField.setValue((double) hour);
		 minuteField = new Slider("Minute " + minute, 0, 59);
		minuteField.setValue((double) minute);
		 tempField = new Slider("Temp " + temp, 14, 24);
		tempField.setValue((double) temp);
		addComponent(hourField);
		addComponent(minuteField);
		addComponent(tempField);
		tempField.setWidth("90%");
		hourField.setWidth("90%");
		minuteField.setWidth("90%");

		tempField.addValueChangeListener(updater(tempField, "Temp"));
		minuteField.addValueChangeListener(updater(minuteField, "Minute"));
		hourField.addValueChangeListener(updater(hourField, "Hour"));

		// hour.setValueListener(new RangeFieldValueListener() {
		//
		// @Override
		// public void valueChanged(int value) {
		// // TODO Auto-generated method stub
		//
		// }
		// });
	}

	

	private Property.ValueChangeListener updater(final Component component,
			final String caption) {
		return new Property.ValueChangeListener() {

			@Override
			public void valueChange(ValueChangeEvent event) {
				Double value = (Double) event.getProperty().getValue();
				component.setCaption(caption + " " + value.intValue());

			}
		};
	}



	public int getHour() {
		return hourField.getValue().intValue();
	}



	public int getTemp() {
		return tempField.getValue().intValue();
	}



	public int getMinute() {
		return minuteField.getValue().intValue();
	}
}

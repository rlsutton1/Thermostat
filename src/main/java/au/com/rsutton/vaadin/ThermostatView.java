package au.com.rsutton.vaadin;

import java.text.NumberFormat;
import java.util.LinkedList;
import java.util.List;

import com.vaadin.event.UIEvents.PollEvent;
import com.vaadin.event.UIEvents.PollListener;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.server.FontAwesome;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.Component;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.TabSheet;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.ValoTheme;

import au.com.rsutton.entryPoint.ForecastReader;
import au.com.rsutton.entryPoint.Monitor;
import au.com.rsutton.entryPoint.Scheduler;
import au.com.rsutton.entryPoint.Trigger;
import au.com.rsutton.rollingaverage.PowerMonitor;

public class ThermostatView extends VerticalLayout implements View
{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	double setTemp = Trigger.getSetTemperature();
	private Label setTempLabel;
	private Label currentTempLabel;
	private Label outside;

	NumberFormat nf = NumberFormat.getNumberInstance();

	@Override
	public void enter(ViewChangeEvent event)
	{

		setSizeFull();
		setHeight("500");
		outside = new Label();

		Label header = new Label("Thermostat");
		header.setStyleName(ValoTheme.LABEL_HUGE);
		header.setHeight("30");

		addComponent(header);

		setComponentAlignment(header, Alignment.MIDDLE_CENTER);

		TabSheet tabs = new TabSheet();
		tabs.setSizeFull();

		tabs.addTab(createCurrentLayout(), "Current");
		tabs.addTab(createNightSettingsLayout(), "Schedules");

		tabs.addTab(PowerMonitor.SELF.getVaadinLayout(), "Power Usage");

		addComponent(tabs);
		setExpandRatio(tabs, 1);

		UI.getCurrent().setPollInterval(2000);
		UI.getCurrent().addPollListener(new PollListener()
		{

			/**
			 * 
			 */
			private static final long serialVersionUID = 1L;

			@Override
			public void poll(PollEvent event)
			{
				currentTempLabel.setValue("Inside " + Monitor.getCurrentTemp().intValue());

				setTemp = Trigger.getSetTemperature();

				setTempLabel.setValue("" + nf.format(setTemp));

				outside.setValue("Outside " + ForecastReader.getTemperature());

			}
		});
	}

	private VerticalLayout createCurrentLayout()
	{
		VerticalLayout currentLayout = new VerticalLayout();
		currentLayout.setSizeFull();
		currentLayout.setHeight("400");
		currentLayout.setMargin(true);

		currentTempLabel = new Label("Inside " + Monitor.getCurrentTemp().intValue());
		currentTempLabel.setStyleName(ValoTheme.LABEL_HUGE);

		currentLayout.addComponent(outside);

		currentLayout.addComponent(currentTempLabel);

		currentLayout.addComponent(createSetTempLayout());

		return currentLayout;
	}

	private Component createNightSettingsLayout()
	{

		final VerticalLayout layout = new VerticalLayout();

		final List<TimeEventField> fields = new LinkedList<>();

		Button saveButton = new Button("Save");

		saveButton.addClickListener(new ClickListener()
		{

			/**
			 * 
			 */
			private static final long serialVersionUID = 1L;

			@Override
			public void buttonClick(ClickEvent event)
			{
				for (TimeEventField field : fields)
				{
					field.save();
				}
				Scheduler.save();

			}
		});

		Button add = new Button("Add");
		add.addClickListener(new ClickListener()
		{

			/**
			 * 
			 */
			private static final long serialVersionUID = 1L;

			@Override
			public void buttonClick(ClickEvent event)
			{
				TempSchedule s = new TempSchedule();
				Scheduler.getScheuleList().add(s);
				TimeEventField f = new TimeEventField(s);
				layout.addComponent(f);
				fields.add(f);

			}
		});

		layout.addComponent(saveButton);
		layout.addComponent(add);

		for (TempSchedule schedule : Scheduler.getScheuleList())
		{
			TimeEventField s = new TimeEventField(schedule);

			layout.addComponent(s);
			fields.add(s);

		}

		// hour.setValueListener(new RangeFieldValueListener() {
		//
		// @Override
		// public void valueChanged(int value) {
		// // TODO Auto-generated method stub
		//
		// }
		// });

		return layout;
	}

	private HorizontalLayout createSetTempLayout()
	{
		HorizontalLayout adjustLayout = new HorizontalLayout();
		adjustLayout.setSpacing(true);
		adjustLayout.setMargin(true);
		Button minusButton = new Button(FontAwesome.MINUS);
		minusButton.setStyleName(ValoTheme.BUTTON_HUGE);
		minusButton.addClickListener(getTemperatureAjuster(-0.5));
		adjustLayout.addComponent(minusButton);
		setTempLabel = new Label("" + setTemp);
		setTempLabel.setStyleName(ValoTheme.LABEL_HUGE);
		adjustLayout.addComponent(setTempLabel);
		Button plusButton = new Button(FontAwesome.PLUS);
		plusButton.setStyleName(ValoTheme.BUTTON_HUGE);
		plusButton.addClickListener(getTemperatureAjuster(0.5));
		adjustLayout.addComponent(plusButton);
		return adjustLayout;
	}

	private ClickListener getTemperatureAjuster(final double i)
	{
		return new ClickListener()
		{

			/**
			 * 
			 */
			private static final long serialVersionUID = 1L;

			@Override
			public void buttonClick(ClickEvent event)
			{
				setTemp = Trigger.getSetTemperature();
				setTemp += i;
				if (setTemp < 12)
				{
					setTemp = 12;
				}
				if (setTemp > 24)
				{
					setTemp = 24;
				}
				setTempLabel.setValue("" + nf.format(setTemp));
				Trigger.setTemperature(setTemp);

			}
		};
	}

}

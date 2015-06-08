package au.com.rsutton.vaadin;

import java.util.LinkedList;
import java.util.List;

import au.com.rsutton.entryPoint.Monitor;
import au.com.rsutton.entryPoint.Scheduler;
import au.com.rsutton.entryPoint.Trigger;

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
import com.vaadin.ui.themes.Reindeer;

public class ThermostatView extends VerticalLayout implements View {

	int setTemp = Trigger.getSetTemperature();
	private Label setTempLabel;
	private Label currentTempLabel;
	private TimeEventField f1;
	private TimeEventField f2;
	
	static final TempSchedule s1 = new TempSchedule();
	
	static final TempSchedule s2 = new TempSchedule();

	@Override
	public void enter(ViewChangeEvent event) {

		UI.getCurrent().setPollInterval(2000);
		UI.getCurrent().addPollListener(new PollListener() {

			@Override
			public void poll(PollEvent event) {
				currentTempLabel.setValue("Temperature "
						+ Monitor.getCurrentTemp().intValue());

				setTemp = Trigger.getSetTemperature();
				setTempLabel.setValue("" + setTemp);

			}
		});

		setSizeFull();
		setHeight("500");

		Label header = new Label("Thermostat");
		header.setStyleName(Reindeer.LABEL_H1);
		header.setHeight("30");

		addComponent(header);

		setComponentAlignment(header, Alignment.MIDDLE_CENTER);

		TabSheet tabs = new TabSheet();
		tabs.setSizeFull();

		tabs.addTab(createCurrentLayout(), "Current");
		tabs.addTab(createNightSettingsLayout(), "Schedules");

		addComponent(tabs);
		setExpandRatio(tabs, 1);
	}

	private VerticalLayout createCurrentLayout() {
		VerticalLayout currentLayout = new VerticalLayout();
		currentLayout.setSizeFull();
		currentLayout.setHeight("100");
		currentLayout.setMargin(true);

		currentTempLabel = new Label("Temperature "
				+ Monitor.getCurrentTemp().intValue());

		currentLayout.addComponent(currentTempLabel);

		currentLayout.addComponent(createSetTempLayout());
		return currentLayout;
	}

	private Component createNightSettingsLayout() {

		VerticalLayout layout = new VerticalLayout();
		 f1 = new TimeEventField(s1);
		 f2 = new TimeEventField(s2);
		
		layout.addComponent(f1);
		layout.addComponent(f2);
		
		Button saveButton = new Button("Save");
		
		saveButton.addClickListener(new ClickListener() {
			
			@Override
			public void buttonClick(ClickEvent event) {
				
				s1.hour = f1.getHour();
				s1.minute = f1.getMinute();
				s1.temp = f1.getTemp();
				
				s2.hour = f2.getHour();
				s2.minute = f2.getMinute();
				s2.temp = f2.getTemp();
				
				List<TempSchedule> schedules= new LinkedList<>();
				schedules.add(s1);
				schedules.add(s2);
				Scheduler.setSchedules(schedules);
				
			}
		});
		
		layout.addComponent(saveButton);
		

//		hour.setValueListener(new RangeFieldValueListener() {
//
//			@Override
//			public void valueChanged(int value) {
//				// TODO Auto-generated method stub
//
//			}
//		});

		return layout;
	}

	private HorizontalLayout createSetTempLayout() {
		HorizontalLayout adjustLayout = new HorizontalLayout();
		adjustLayout.setSpacing(true);
		adjustLayout.setMargin(true);
		Button minusButton = new Button(FontAwesome.MINUS);
		minusButton.setStyleName(Reindeer.BUTTON_SMALL);
		minusButton.addClickListener(getTemperatureAjuster(-1));
		adjustLayout.addComponent(minusButton);
		setTempLabel = new Label("" + setTemp);
		adjustLayout.addComponent(setTempLabel);
		Button plusButton = new Button(FontAwesome.PLUS);
		plusButton.setStyleName(Reindeer.BUTTON_SMALL);
		plusButton.addClickListener(getTemperatureAjuster(1));
		adjustLayout.addComponent(plusButton);
		return adjustLayout;
	}

	private ClickListener getTemperatureAjuster(final int i) {
		return new ClickListener() {

			@Override
			public void buttonClick(ClickEvent event) {
				setTemp = Trigger.getSetTemperature();
				setTemp += i;
				if (setTemp < 12) {
					setTemp = 12;
				}
				if (setTemp > 24) {
					setTemp = 24;
				}
				setTempLabel.setValue("" + setTemp);
				Trigger.setTemperature(setTemp);

			}
		};
	}

}

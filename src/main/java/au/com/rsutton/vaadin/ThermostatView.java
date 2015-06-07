package au.com.rsutton.vaadin;

import au.com.rsutton.entryPoint.Monitor;
import au.com.rsutton.entryPoint.Scheduler;
import au.com.rsutton.entryPoint.Trigger;

import com.vaadin.data.Property.ValueChangeListener;
import com.vaadin.data.Validator;
import com.vaadin.data.Property.ValueChangeEvent;
import com.vaadin.event.UIEvents.PollEvent;
import com.vaadin.event.UIEvents.PollListener;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.server.FontAwesome;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.TextField;
import com.vaadin.ui.themes.Reindeer;
import com.vaadin.ui.themes.ValoTheme;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Component;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.TabSheet;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;

public class ThermostatView extends VerticalLayout implements View
{

	int setTemp = Trigger.getSetTemperature();
	private Label setTempLabel;
	private Label currentTempLabel;
	private TextField onTimeField;
	private TextField offTimeField;

	@Override
	public void enter(ViewChangeEvent event)
	{

		UI.getCurrent().setPollInterval(2000);
		UI.getCurrent().addPollListener(new PollListener()
		{

			@Override
			public void poll(PollEvent event)
			{
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

	private VerticalLayout createCurrentLayout()
	{
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

	private Component createNightSettingsLayout()
	{

		FormLayout layout = new FormLayout();
		layout.setSpacing(true);
		layout.setMargin(true);
		layout.setSizeFull();
		layout.addComponent(new NightModeComponent("Monday", 22, 5, 14, new NightModeComponentListener()
		{
			
			@Override
			public void saveNightMode()
			{
				// TODO Auto-generated method stub
				
			}
		}));
		return layout;
	}

	





	private HorizontalLayout createSetTempLayout()
	{
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

	private ClickListener getTemperatureAjuster(final int i)
	{
		return new ClickListener()
		{

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
				setTempLabel.setValue("" + setTemp);
				Trigger.setTemperature(setTemp);

			}
		};
	}

}

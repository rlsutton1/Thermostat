package au.com.rsutton.vaadin;

import au.com.rsutton.entryPoint.Trigger;

import com.vaadin.server.FontAwesome;
import com.vaadin.ui.Button;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.Reindeer;

public class TemperatureComponent extends VerticalLayout
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private int setTemp;
	private Label setTempLabel;
	protected TemperatureChangeListener listener;

	TemperatureComponent(String caption, int defaultValue, TemperatureChangeListener plistener)
	{
		HorizontalLayout layout = new HorizontalLayout();
		
		this.listener = plistener;
		setTemp = defaultValue;
		layout.setSpacing(true);
		layout.setMargin(true);
		Button minusButton = new Button(FontAwesome.MINUS);
		minusButton.setStyleName(Reindeer.BUTTON_SMALL);
		minusButton.addClickListener(getTemperatureAjuster(-1));
		layout.addComponent(minusButton);
		setTempLabel = new Label("" + setTemp);
		layout.addComponent(setTempLabel);
		Button plusButton = new Button(FontAwesome.PLUS);
		plusButton.setStyleName(Reindeer.BUTTON_SMALL);
		plusButton.addClickListener(getTemperatureAjuster(1));
		layout.addComponent(plusButton);
		
		addComponent(new Label(caption));
		addComponent(layout);
	}

	private ClickListener getTemperatureAjuster(final int i)
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
				if (listener != null)
				{
					listener.valueChanged(setTemp);
				}
			}
		};
	}

}

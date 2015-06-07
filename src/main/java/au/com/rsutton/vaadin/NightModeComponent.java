package au.com.rsutton.vaadin;

import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.TextField;
import com.vaadin.ui.Button.ClickEvent;

public class NightModeComponent extends HorizontalLayout 
{

	private TextField onTimeField;
	private TextField offTimeField;

	public NightModeComponent(String caption, int defaultStart, int defaultEnd, int defaultTemperature,final NightModeComponentListener listener)
	{
		addComponent(new Label(caption));
		addComponent(new TimeComponent("Time",defaultStart));
		addComponent(new TemperatureComponent("Set Temperature",defaultTemperature,null));

		
		Button button = new Button("Save");
		button.addClickListener(new ClickListener()
		{
			
			@Override
			public void buttonClick(ClickEvent event)
			{
				listener.saveNightMode();
				
			}
		});
		
		addComponent(button);
	}
	
	
	
}

package au.com.rsutton.vaadin;

import com.vaadin.navigator.Navigator;
import com.vaadin.server.VaadinRequest;
import com.vaadin.ui.UI;

public class ThermostatUI extends UI
{
	Navigator navigator;
	protected static final String MAINVIEW = "main";

	@Override
	protected void init(VaadinRequest request)
	{
		getPage().setTitle("Thermostat");

		// Create a navigator to control the views
		navigator = new Navigator(this, this);

		// Create and register the views
		navigator.addView("", new ThermostatView());
		navigator.addView(MAINVIEW, new ThermostatView());
	}
}
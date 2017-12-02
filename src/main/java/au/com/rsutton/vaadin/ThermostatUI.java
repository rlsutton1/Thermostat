package au.com.rsutton.vaadin;

import com.vaadin.annotations.Theme;
import com.vaadin.navigator.Navigator;
import com.vaadin.server.VaadinRequest;
import com.vaadin.ui.UI;
import com.vaadin.ui.themes.ValoTheme;

@Theme(ValoTheme.THEME_NAME)
public class ThermostatUI extends UI
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
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
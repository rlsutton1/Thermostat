package au.com.rsutton.entryPoint;

import java.util.concurrent.TimeUnit;

import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.util.resource.Resource;
import org.eclipse.jetty.webapp.WebAppContext;

import com.pi4j.concurrent.DefaultExecutorServiceFactory;
import com.pi4j.concurrent.ExecutorServiceFactory;
import com.pi4j.io.gpio.GpioController;
import com.pi4j.io.gpio.GpioFactory;
import com.pi4j.io.gpio.GpioPinDigitalOutput;
import com.pi4j.io.gpio.RaspiPin;

import au.com.rsutton.rollingaverage.PowerMonitor;

public class Main
{

	public static void main(String[] args) throws Exception
	{

		configureGpioForGrove();

		GrovePi grove = null;
		// pi =1 , banana = 2
		if (args.length > 0 && args[0] != null && args[0].equalsIgnoreCase("sim"))
		{
			grove = new GrovePiSimulator();
		} else
		{
			grove = new GrovePiProvider(1, 4);
		}

		//
		// // grove.setMode(GrovePiPin.GPIO_A1, PinMode.ANALOG_INPUT);
		//

		PowerMonitor a = PowerMonitor.SELF;

		Monitor monitor = new Monitor(grove);
		Trigger trigger = new Trigger(grove, monitor);
		Scheduler scheduler = new Scheduler();
		ForecastReader forecast = new ForecastReader();
		Scheduler.load();

		TempLogger tempLogger = new TempLogger();

		ExecutorServiceFactory executorFactory = new DefaultExecutorServiceFactory();
		executorFactory.getScheduledExecutorService().scheduleAtFixedRate(monitor, 2, 2, TimeUnit.SECONDS);
		executorFactory.getScheduledExecutorService().scheduleAtFixedRate(trigger, 30, 30, TimeUnit.SECONDS);
		executorFactory.getScheduledExecutorService().scheduleAtFixedRate(scheduler, 30, 30, TimeUnit.SECONDS);

		executorFactory.getScheduledExecutorService().scheduleAtFixedRate(tempLogger, 1, 5, TimeUnit.MINUTES);

		executorFactory.getScheduledExecutorService().scheduleAtFixedRate(forecast, 0, 30, TimeUnit.MINUTES);

		setupJettyV3();

	}

	private static void configureGpioForGrove() throws InterruptedException
	{
		// the grove continously resets if GPIO_10 is low

		final GpioController gpio = GpioFactory.getInstance();

		GpioPinDigitalOutput myLed1 = gpio.provisionDigitalOutputPin(RaspiPin.GPIO_10);

		myLed1.high();

		TimeUnit.SECONDS.sleep(2);
	}

	private static void setupJettyV3() throws Exception
	{
		String contextPath = "/";

		Resource.setDefaultUseCaches(true);
		// Resource base = Resource
		// .newResource("jar:file:src/main/java");
		// Resource base =
		// Resource.newResource("jar:file:src/main/resources/content.jar!/");
		//
		// System.out.println(base.getFile().getAbsolutePath());

		Server server = new Server(8080);

		WebAppContext webapp = new WebAppContext();

		webapp.setContextPath(contextPath);
		webapp.setDescriptor("web.xml");

		webapp.setResourceBase("/home/rsutton/workspaces/piBot/thermostat/");

		webapp.setClassLoader(Thread.currentThread().getContextClassLoader());

		server.setHandler(webapp);

		server.start();

		System.out.println("Go to http://localhost:" + 80 + contextPath);

		server.join();
	}

	// private static void setupJettyv2() throws Exception, InterruptedException
	// {
	// Server server = new Server(8080);
	// // System.setProperty("is_DCA", "YES");
	// WebAppContext webAppContext = new WebAppContext();
	// webAppContext.setResourceBase("/home/myfolder/workspace/app/dca/src/main/webapp");
	// webAppContext.setDescriptor("/home/myfolder/workspace/app/dca/src/main/webapp/WEB-INF/web.xml");
	// webAppContext.setContextPath("/thermostat");
	// server.setHandler(webAppContext);
	// server.start();
	// server.join();
	// }

	// private void setupJettyv1()
	// {
	// // Create a basic jetty server object that will listen on port 8080.
	// // Note that if you set this to port 0 then a randomly available port
	// // will be assigned that you can either look in the logs for the port,
	// // or programmatically obtain it for use in test cases.
	// Server server = new Server(8080);
	//
	// // The ServletHandler is a dead simple way to create a context handler
	// // that is backed by an instance of a Servlet.
	// // This handler then needs to be registered with the Server object.
	// ServletHandler handler = new ServletHandler();
	// // server.setHandler(handler);
	//
	// // Passing in the class for the Servlet allows jetty to instantiate an
	// // instance of that Servlet and mount it on a given context path.
	//
	// // IMPORTANT:
	// // This is a raw Servlet, not a Servlet that has been configured
	// // through a web.xml @WebServlet annotation, or anything similar.
	// // handler.addServletWithMapping(HelloServlet.class, "/*");
	//
	// // Start things up!
	// // server.start();
	//
	// // The use of server.join() the will make the current thread join and
	// // wait until the server is done executing.
	// // See
	// // http://docs.oracle.com/javase/7/docs/api/java/lang/Thread.html#join()
	// // server.join();
	//
	// }
}

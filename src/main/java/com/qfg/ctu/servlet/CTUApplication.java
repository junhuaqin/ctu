package com.qfg.ctu.servlet;

import com.qfg.ctu.servlet.rest.exception.InvalidRequestExceptionMapper;
import com.qfg.ctu.util.ClassUtil;
import org.glassfish.hk2.utilities.binding.AbstractBinder;
import org.glassfish.jersey.server.ResourceConfig;
import org.jvnet.hk2.annotations.Service;

import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * This class is specified in the <tt>web.xml</tt> file, and is used when Jersey is loaded to determine where resources
 * (API classes) are located, and for any other dependencies.
 * 
 * @author Angus Macdonald (amacdonald@aetherworks.com)
 */
public class CTUApplication extends ResourceConfig {
    private final static Logger LOGGER = Logger.getLogger(CTUApplication.class.getName());
	/**
	 * This is the constructor called by the application server when loading the application (as specified by the
	 * web.xml).

	public CTUApplication() {
		this(new SetCallHandler());
	}
*/
	/**
	 * This is the constructor called directly by unit tests. This allows us to pass in a mocked version of the
	 * {@link SetCallHandler} to just test the functionality of the API and not the handler itself.
	 */
	public CTUApplication() {
		/*
		 * Register the mapping between internal exceptions and their outward facing messages.
		 */
		register(InvalidRequestExceptionMapper.class);

		register(new ServiceBinder("com.qfg.ctu.servlet.rest.services"));

		/*
		 * Specify where resource classes are located. These are the classes that constitute the API.
		 */
		packages(true, "com.qfg.ctu.servlet.rest.resources");
	}

	protected static class ServiceBinder extends AbstractBinder {

		private String packageName;
		public  ServiceBinder(String packageName) {
			this.packageName = packageName;
		}

		@Override
		protected void configure() {
			List<Class<?>> classEs = ClassUtil.getClasses(this.packageName);
			classEs.stream().filter(n->n.getAnnotation(Service.class)!=null).forEach(n->{
                try {
                    Class c = n;
                    LOGGER.log(Level.INFO, String.format("bind service:%s", c.getName()));
                    bind(c.newInstance()).to(c);
                } catch (InstantiationException e) {
                    e.printStackTrace();
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
            });
		}
	}
}
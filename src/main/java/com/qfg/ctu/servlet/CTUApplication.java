package com.qfg.ctu.servlet;

import com.qfg.ctu.proxy.DBProxyHandler;
import com.qfg.ctu.util.ClassUtil;
import org.glassfish.hk2.utilities.binding.AbstractBinder;
import org.glassfish.jersey.server.ResourceConfig;
import org.jvnet.hk2.annotations.Service;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Proxy;
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
//		register(InvalidRequestExceptionMapper.class);

		LOGGER.log(Level.INFO, "register ServiceBinder");

		register(new ServiceBinder("com.qfg.ctu.servlet.rest.services"));

		/*
		 * Specify where resource classes are located. These are the classes that constitute the API.
		 */
//		packages(true, "com.qfg.ctu.servlet.rest.resources");
	}

	protected static class ServiceBinder extends AbstractBinder {

		private String packageName;
		public  ServiceBinder(String packageName) {
			this.packageName = packageName;
		}

		@Override
		protected void configure() {
//			bind(UserService.class).to(UserService.class);
//			bind(OrderService.class).to(OrderService.class);
//			bind(ProductServiceImpl.class).to(ProductServiceImpl.class);

			LOGGER.log(Level.INFO, String.format("bind services start"));
			List<Class<?>> classEs = ClassUtil.getClasses(this.packageName);
			LOGGER.log(Level.INFO, String.format("bind services:%d", classEs.size()));
			classEs.stream().filter(n->n.getAnnotation(Service.class)!=null).forEach(n->{
				LOGGER.log(Level.INFO, String.format("bind service:%s", n.getName()));

				Service service = n.getAnnotation(Service.class);
				Class c = null;
				try {
					c = n.getClassLoader().loadClass(service.name());
				} catch (ClassNotFoundException e) {
					e.printStackTrace();
					LOGGER.log(Level.WARNING, String.format("failed to load class:%s", service.name()));
					return;
				}

				InvocationHandler ih= new DBProxyHandler(c);//代理实例的调用处理程序。
				Object object = Proxy.newProxyInstance(c.getClassLoader(), c.getInterfaces(), ih);
				bind(((Class) n).cast(object)).to((Class) n);
			});
			LOGGER.log(Level.INFO, "bind services end");
		}
	}
}

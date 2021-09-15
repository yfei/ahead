package cn.dcube.ahead.core.context;

import org.springframework.beans.BeansException;
import org.springframework.boot.autoconfigure.AutoConfigureOrder;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

@Component
public class SpringContext implements ApplicationContextAware {

	/**
	 * the spring application context
	 */
	private static ApplicationContext appContext;

	@Override
	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		appContext = applicationContext;
	}

	public static ApplicationContext getContext() {
		return appContext;
	}

	/**
	 * get spring bean by beanName.
	 * 
	 * @param beanName the bean name.
	 * @return the bean instance.
	 */
	@SuppressWarnings("unchecked")
	public static <T> T getBean(String beanName) {
		Assert.notNull(beanName, "beanName cannot be empty!");
		Object bean = appContext.getBean(beanName);
		if (bean != null) {
			return (T) bean;
		}
		return null;
	}
}

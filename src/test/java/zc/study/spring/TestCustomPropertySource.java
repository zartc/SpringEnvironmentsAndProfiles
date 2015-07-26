package zc.study.spring;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.Test;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.core.env.PropertySource;


/**
 * Test that we can use a cutom PropertySource<String> implementation to detect the running platform and use the
 * returned value to load the correct profile at runtime.
 * 
 * @author Pascal
 */
public class TestCustomPropertySource {

	private static class CustomPropertySource extends PropertySource<String> {

		private String platform = null;


		public CustomPropertySource() {
			super("custom");
		}

		@Override
		public String getProperty(String name) {
			if ("PLATFORM".equals(name)) {
				if (platform == null) platform = detectRunningPlatform();
				return platform;
			}
			return null;
		}

		private String detectRunningPlatform() {
			// use any clever means to detect and categorize the running platform
			// Often the running platform can be categorized by reading its IP address
			// other time it can be categorized by detecting or reading a file at a known place

			return "PROD";
		}
	}


	@Test
	public void test() {
		@SuppressWarnings("resource")
		ClassPathXmlApplicationContext ctx = new ClassPathXmlApplicationContext("classpath:config/applicationContext.xml");
		ctx.getEnvironment().getPropertySources().addLast(new CustomPropertySource());

		// test that the environment can detect the running platform via the CustomPropertySource
		assertThat(ctx.getEnvironment().getProperty("PLATFORM")).isEqualTo("PROD");

		// then set the profile
		ctx.getEnvironment().setActiveProfiles(ctx.getEnvironment().getProperty("PLATFORM"));
		ctx.refresh();

		// then test that we have the correct value in our current profile
		assertThat(ctx.getBeanFactory().resolveEmbeddedValue("${PLATFORM.PROD}")).isEqualTo("ws.mycompagnie.fr");
	}
}

/* EOF */

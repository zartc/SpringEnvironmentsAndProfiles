package zc.study.spring.multipleplateformconfiguration;


import static org.assertj.core.api.Assertions.assertThat;

import org.junit.BeforeClass;
import org.junit.Test;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.core.env.Environment;

import zc.study.spring.ServiceAInfo;
import zc.study.spring.ServiceBInfo;
import zc.study.spring.ServiceInfo;


public class Test_DEV_Properties {

	private static final String TARGET_PLATFORM = "DEV";

	protected static ClassPathXmlApplicationContext ctx;

	@BeforeClass
	public static void setup() {
		// simulate a -DPLATFORM=DEV on the command line
		System.setProperty("PLATFORM", TARGET_PLATFORM);
		
		// set the spring.profiles.active system property BEFORE loading the Spring context.
		System.setProperty("spring.profiles.active", System.getProperty("PLATFORM"));

		// Since the spring.profiles.active is set, Spring will load the correct profile itself
		ctx = new ClassPathXmlApplicationContext("classpath:config/applicationContext.xml");
	}

	@Test
	public void test_PLATFORM_property() {
		Environment environment = ctx.getEnvironment();
		assertThat(environment.getProperty("PLATFORM")).isEqualTo(TARGET_PLATFORM);
	}

	@Test
	public void test_ActiveProfile() {
		Environment environment = ctx.getEnvironment();
		assertThat(environment.getActiveProfiles()).containsExactly(TARGET_PLATFORM);
	}

	@Test
	public void testServiceAInfo() {
		ServiceInfo serviceAInfo = ctx.getBean(ServiceAInfo.class);

		assertThat(serviceAInfo.service_url).isEqualTo("http://192.168.0.101:8080/ws/service_a");
		assertThat(serviceAInfo.service_loging).isEqualTo("username_a");
		assertThat(serviceAInfo.service_passwd).isEqualTo("password_a");
	}

	@Test
	public void testServiceBInfo() {
		ServiceInfo serviceBInfo = ctx.getBean(ServiceBInfo.class);

		assertThat(serviceBInfo.service_url).isEqualTo("http://192.168.0.101:8080/ws/service_b");
		assertThat(serviceBInfo.service_loging).isEqualTo("username_b");
		assertThat(serviceBInfo.service_passwd).isEqualTo("password_b");
	}
}

/* EOF */

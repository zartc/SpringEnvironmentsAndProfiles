package zc.study.spring.multipleplateformconfiguration;


import static org.assertj.core.api.Assertions.assertThat;

import org.junit.BeforeClass;
import org.junit.Test;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.core.env.Environment;

import zc.study.spring.ServiceAInfo;
import zc.study.spring.ServiceBInfo;
import zc.study.spring.ServiceInfo;


/**
 * In this test case we expect the launcher to set the <code>PLATFORM</code> property on the command line. The propertie
 * files use <code>${PLATFORM}</code> wherever platform dependant value/path are used. The context initialisation code
 * copy the value of the <code>PLATFORM</code> system property into the <code>spring.profiles.active</code> system
 * property before loading the Spring context so that Spring load the correct profile.
 *
 * @author Pascal Jacob
 */
public class Test_QUALIF_Properties {

	private static final String TARGET_PLATFORM = "QUALIF";

	protected static ClassPathXmlApplicationContext ctx;


	@BeforeClass
	public static void setup() {
		// simulate a -DPLATFORM=PREPROD on the command line
		System.setProperty("PLATFORM", TARGET_PLATFORM);

		// set the spring.profiles.active system property BEFORE loading the Spring context.
		System.setProperty("spring.profiles.active", System.getProperty("PLATFORM"));

		// Since the spring.profiles.active is set, Spring will load the correct profile itself
		ctx = new ClassPathXmlApplicationContext("classpath:config/applicationContext.xml");
	}

	@Test
	public void test_PLATFORM_property() {
		String profileName = ctx.getEnvironment().getProperty("PLATFORM");
		assertThat(profileName).isEqualTo(TARGET_PLATFORM);

		profileName = ctx.getBeanFactory().resolveEmbeddedValue("${PLATFORM}");
		assertThat(profileName).isEqualTo(TARGET_PLATFORM);
	}

	@Test
	public void test_ActiveProfile() {
		Environment environment = ctx.getEnvironment();

		assertThat(environment.getActiveProfiles()).containsExactly(TARGET_PLATFORM);
	}

	@Test
	public void testServiceAInfo() {
		ServiceInfo serviceAInfo = ctx.getBean(ServiceAInfo.class);

		assertThat(serviceAInfo.service_url).isEqualTo("http://123.456.789:80/ws/service_a.1.0.0.ws");
		assertThat(serviceAInfo.service_loging).isEqualTo("HoneyFoal");
		assertThat(serviceAInfo.service_passwd).isEqualTo(">m}QcnlD@r");
	}

	@Test
	public void testServiceBInfo() {
		ServiceInfo serviceBInfo = ctx.getBean(ServiceBInfo.class);

		assertThat(serviceBInfo.service_url).isEqualTo("http://123.456.789:80/ws/service_b.1.0.0.ws");
		assertThat(serviceBInfo.service_loging).isEqualTo("CrazyRunnyFlipper");
		assertThat(serviceBInfo.service_passwd).isEqualTo("5+=mX;`f)u");
	}
}

/* EOF */

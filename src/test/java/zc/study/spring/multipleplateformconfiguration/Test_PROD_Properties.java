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
 * In this test case we expect the launcher to set the <code>spring.profiles.active</code> property on the command line.
 * The propertie files use <code>${spring.profiles.active}</code> wherever platform dependant value/path are used. The
 * context initialisation code have nothing special to do before loading the Spring context.
 *
 * @author Pascal Jacob
 */
public class Test_PROD_Properties {

	private static final String TARGET_PLATFORM = "PROD";

	protected static ClassPathXmlApplicationContext ctx;


	@BeforeClass
	public static void setup() {
		// simulate a -Dspring.profiles.active=PROD on the command line
		System.setProperty("spring.profiles.active", TARGET_PLATFORM);

		// Since the spring.profiles.active is set, Spring will load the correct profile itself
		ctx = new ClassPathXmlApplicationContext("classpath:config/applicationContext.xml");
	}

	@Test
	public void test_ActiveProfile() {
		Environment environment = ctx.getEnvironment();

		assertThat(environment.getActiveProfiles()).containsExactly(TARGET_PLATFORM);
	}

	@Test
	public void testServiceAInfo() {
		ServiceInfo serviceAInfo = ctx.getBean(ServiceAInfo.class);

		assertThat(serviceAInfo.service_url).isEqualTo("http://service_a.ws.mycompagnie.fr:80/ws/service_a.1.0.0.ws");
		assertThat(serviceAInfo.service_loging).isEqualTo("FlamingScrewdriver");
		assertThat(serviceAInfo.service_passwd).isEqualTo("([<>A~4$:pV[1:X");
	}

	@Test
	public void testServiceBInfo() {
		ServiceInfo serviceBInfo = ctx.getBean(ServiceBInfo.class);

		assertThat(serviceBInfo.service_url).isEqualTo("http://service_b.ws.mycompagnie.fr:80/ws/service_b.1.0.0.ws");
		assertThat(serviceBInfo.service_loging).isEqualTo("FlyingTombstone");
		assertThat(serviceBInfo.service_passwd).isEqualTo("+[,>d+^5w!J0Q0x");
	}
}

/* EOF */

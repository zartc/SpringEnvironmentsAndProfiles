package zc.study.spring;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;


@Component
public class ServiceAInfo extends ServiceInfo {

	@Value("http://${service_a.host}:${service_a.port}/${service_a.path}")
	public void setService_url(String service_url) {
		this.service_url = service_url;
	}

	@Value("${service_a.username}")
	public void setService_loging(String service_loging) {
		this.service_loging = service_loging;
	}

	@Value("${service_a.password}")
	public void setService_passwd(String service_passwd) {
		this.service_passwd = service_passwd;
	}
}

/* EOF */

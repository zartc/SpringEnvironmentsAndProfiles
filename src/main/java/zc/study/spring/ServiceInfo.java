package zc.study.spring;

import org.apache.commons.lang3.builder.ToStringBuilder;


public class ServiceInfo {
	public String service_url;

	public String service_loging;

	public String service_passwd;


	@Override
	public String toString() {
		ToStringBuilder builder = new ToStringBuilder(this);
		builder.append(service_url);
		builder.append(service_loging);
		builder.append(service_passwd);
		return builder.toString();
	}
}

/* EOF */

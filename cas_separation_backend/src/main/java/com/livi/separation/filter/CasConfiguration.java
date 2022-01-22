package com.livi.separation.filter;

import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Configuration;

import net.unicon.cas.client.configuration.CasClientConfigurerAdapter;

@Configuration
public class CasConfiguration extends CasClientConfigurerAdapter {

	@Override
	public void configureAuthenticationFilter(FilterRegistrationBean authenticationFilter) {
		super.configureAuthenticationFilter(authenticationFilter);
		// 配置地址，这里还可以配置很多，例如cas重定向策略等。
		authenticationFilter.getInitParameters().put("ignorePattern", "/auth/logout|/auth/cas");
		authenticationFilter.getInitParameters().put("authenticationRedirectStrategyClass",
				"com.livi.separation.filter.CustomAuthenticationRedirectStrategy");
	}
}
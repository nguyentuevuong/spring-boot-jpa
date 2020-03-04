package com.nittsu.kinjirou;

import com.nittsu.kinjirou.core.common.Mode;

import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;

public class ServletInitializer extends SpringBootServletInitializer {

	@Override
	protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
		UniversalApplication.mode = Mode.SERVLET;
		return application.sources(UniversalApplication.class);
	}
}

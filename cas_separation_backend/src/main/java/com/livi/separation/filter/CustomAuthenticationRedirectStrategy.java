package com.livi.separation.filter;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.jasig.cas.client.authentication.AuthenticationRedirectStrategy;

import com.fasterxml.jackson.databind.ObjectMapper;

public class CustomAuthenticationRedirectStrategy implements AuthenticationRedirectStrategy {

	@Override
	public void redirect(final HttpServletRequest request, final HttpServletResponse response,
			final String potentialRedirectUrl) throws IOException {
		response.setContentType("application/json");
		response.setStatus(401);
		Map<String, String> res = new HashMap<>(2);
		res.put("code", "402");
		res.put("msg", "auth fail");
		try (final PrintWriter writer = response.getWriter()) {
			writer.write(new ObjectMapper().writeValueAsString(res));
			writer.flush();
		}
	}
}
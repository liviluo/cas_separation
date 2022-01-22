package com.livi.separation.controller;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.jasig.cas.client.authentication.AttributePrincipal;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/auth")
public class AuthController {
	@Value("${casClientLoginUrl}")
	private String loginUrl;
	@Value("${casClientLogoutUrl}")
	private String logoutUrl;

	@RequestMapping(value = "cas", method = RequestMethod.GET)
	public void cas(HttpServletResponse response, String currentPath) throws IOException {
		response.sendRedirect(loginUrl + currentPath);
	}

	@RequestMapping(value = "login", method = RequestMethod.GET)
	public void login(HttpServletRequest request, HttpServletResponse response, String currentPath) throws IOException {
		AttributePrincipal principal = (AttributePrincipal) request.getUserPrincipal();
		String username = principal.getName();
		HttpSession session = request.getSession();
		session.setAttribute("username", username);
		response.sendRedirect(currentPath);
	}

	@RequestMapping(value = "logout", method = RequestMethod.GET)
	public void logout(HttpServletRequest request, HttpServletResponse response, String currentPath)
			throws IOException {
		HttpSession session = request.getSession();
		session.invalidate();
		response.sendRedirect(logoutUrl);
	}

	@RequestMapping(value = "user", method = RequestMethod.GET)
	public String user(HttpServletRequest request) {
		HttpSession session = request.getSession();
		return (String) session.getAttribute("username");
	}
}

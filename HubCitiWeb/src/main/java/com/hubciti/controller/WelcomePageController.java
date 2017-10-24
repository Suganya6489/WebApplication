package com.hubciti.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
public class WelcomePageController {

	@RequestMapping(value = "/setupwelcomescreen.htm", method = RequestMethod.GET)
	public String showWelcomePage() {

		return "welcomepage";
	}

}

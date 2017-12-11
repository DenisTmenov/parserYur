package com.denis.parser.yur.frontend.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class PageController {

	private static final Logger logger = LoggerFactory.getLogger(PageController.class);

	@RequestMapping(value = { "/", "/home", "/index" })
	public ModelAndView index() {
		ModelAndView mv = new ModelAndView("page");
		mv.addObject("title", "Home");

		logger.info("Inside PageController index method - INFO");
		logger.debug("Inside PageController index method - DEBUG");

		mv.addObject("userClickHome", true);
		return mv;
	}

	@RequestMapping(value = { "/about" })
	public ModelAndView about() {
		ModelAndView mv = new ModelAndView("page");
		mv.addObject("title", "About");

		logger.info("Inside PageController index method - INFO");
		logger.debug("Inside PageController index method - DEBUG");

		mv.addObject("userClickAbout", true);
		return mv;
	}

	@RequestMapping(value = { "/contacts" })
	public ModelAndView contacts() {
		ModelAndView mv = new ModelAndView("page");
		mv.addObject("title", "Contacts");

		logger.info("Inside PageController index method - INFO");
		logger.debug("Inside PageController index method - DEBUG");

		mv.addObject("userClickContacts", true);
		return mv;
	}

	@RequestMapping(value = { "/services" })
	public ModelAndView services() {
		ModelAndView mv = new ModelAndView("page");
		mv.addObject("title", "Services");

		logger.info("Inside PageController index method - INFO");
		logger.debug("Inside PageController index method - DEBUG");

		mv.addObject("userClickServices", true);
		return mv;
	}

}

package com.app.demo.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.servlet.ModelAndView;

@Controller
@Scope(value="prototype")
@RequestMapping("/")
@SessionAttributes({"demo"})
public class AppController {

	@RequestMapping(value = "app")
	public ModelAndView appIndex(HttpServletRequest request,
			HttpServletResponse response) {
		ModelAndView mav = new ModelAndView("/app/index");
		return mav;
	}

	@RequestMapping(value = "")
	public ModelAndView index(HttpServletRequest request,
			HttpServletResponse response) {
		ModelAndView mav = new ModelAndView("/index");
		return mav;
	}
	
}

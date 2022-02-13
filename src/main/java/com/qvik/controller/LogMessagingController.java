package com.qvik.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class LogMessagingController {
	
	@RequestMapping(value="/")
	public String hello() {
		return "Hello world ";
	}

}

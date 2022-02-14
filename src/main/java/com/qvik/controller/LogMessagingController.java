package com.qvik.controller;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.qvik.service.LogMessagingService;

@RestController
@RequestMapping(value = "/")
public class LogMessagingController {

	@Autowired
	private LogMessagingService logMessagingService;

	@GetMapping
	public Map hello() {
		return logMessagingService.LogMessageStatistics();
	}

	@GetMapping(value = "{logId}")
	public List<Map> getLogs(@PathVariable int logId) {
		List<Map> logs = logMessagingService.getLog(logId);
		return logs;
	}

	@PostMapping(value = "addmessage")
	public String getLogs(@RequestBody Map<String, Object> log) {
		logMessagingService.addMessage((String) log.get("name"), (Integer) log.get("logId"),
				(String) log.get("message"));
		return "Logs Successfully added";
	}

	@PatchMapping(value = "set_max_age/{maxAge}")
	public String setMaxAge(@PathVariable int maxAge) {
		logMessagingService.setMaxAge(maxAge);
		return "Updated Max age";
	}
}

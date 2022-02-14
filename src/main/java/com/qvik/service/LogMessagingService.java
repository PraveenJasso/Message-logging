package com.qvik.service;

import java.util.List;
import java.util.Map;

public interface LogMessagingService {
	
	List<Map> getLog(int logId);
	
	void addMessage(String name, int logId, String message);
	
	void setMaxAge(int maxAge);
	
	Map<String, Object> LogMessageStatistics();
}

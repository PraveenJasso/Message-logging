package com.qvik.Repository;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public interface LogMessaging {
	
	List<Map> getLog(int logId);
	
	void addMessage(String name, int logId, String message);
	
	void setMaxAge(int maxAge);
	
	Map<String, Object> LogMessageStatistics();
	
	ConcurrentHashMap<Integer, List<Map>> getAllLogs();
	
}

package com.qvik.Repository;

import java.util.List;
import java.util.Map;

public interface LogMessaging {
	
	List<Map> getLog(int logId);
	
	void addMessage(String name, int logId, String message);
	
}

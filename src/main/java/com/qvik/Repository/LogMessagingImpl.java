package com.qvik.Repository;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.stereotype.Repository;

@Repository
public class LogMessagingImpl implements LogMessaging {

	ConcurrentHashMap<Integer, List<Map>> logs = new ConcurrentHashMap<>();
	
	@Override
	public List<Map> getLog(int logId) {
		return logs.get(logId);
	}

	@Override
	public void addMessage(String name, int logId, String message) {
		Map<String, Object> log = new HashMap<>();
		log.put("name", name);
		log.put("mesage", message);
		log.put("logId", logId);
		log.put("createdon",new Timestamp(System.currentTimeMillis()));
		if (Objects.nonNull(logs.get(logId))) {
			List<Map> existingLogs = logs.get(logId);
			existingLogs.add(log);
			logs.put(logId, existingLogs);
		} else {
			List<Map> logMessage = new ArrayList<>();
			logMessage.add(log);
			logs.put(logId, logMessage);
		}
	}

}

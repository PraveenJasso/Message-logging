package com.qvik.Repository;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
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
		List<Map> activeLogs = new ArrayList<>();
		List<Map> existingLogs = logs.get(logId);
		if (Objects.nonNull(existingLogs)) {
			Calendar calendar = Calendar.getInstance();
			calendar.setTime(new Timestamp(System.currentTimeMillis()));

			for (Map log : existingLogs) {
				if (Objects.nonNull(log.get("maxAge"))) {
					calendar.add(Calendar.SECOND, -(Integer) log.get("maxAge"));
					Date date = calendar.getTime();
					Timestamp createdOn = new Timestamp(date.getTime());
					if (createdOn.before((Timestamp) log.get("createdOn"))) {
						activeLogs.add(log);
					}
				} else {
					activeLogs.add(log);
				}
			}
		}
		return activeLogs;
	}

	@Override
	public void addMessage(String name, int logId, String message) {
		Map<String, Object> log = new HashMap<>();
		log.put("name", name);
		log.put("mesage", message);
		log.put("logId", logId);
		log.put("createdOn", new Timestamp(System.currentTimeMillis()));
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

	@Override
	public void setMaxAge(int maxAge) {
		for (Integer key : logs.keySet()) {
			List<Map> existingLogs = logs.get(key);
			for (Map log : existingLogs) {
				if (Objects.isNull(log.get("maxAge"))) {
					log.put("maxAge", maxAge);
				}
			}
		}
	}
}

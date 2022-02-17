package com.qvik.Repository;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.OptionalDouble;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Repository;

@Repository
public class LogMessagingImpl implements LogMessaging, Tasklet, InitializingBean {

	private static final Logger logger = LogManager.getLogger(LogMessagingImpl.class.getName());
	ConcurrentHashMap<Integer, List<Map>> logs = new ConcurrentHashMap<>();

	public LogMessagingImpl(ConcurrentHashMap<Integer, List<Map>> logs) {
		super();
		this.logs = logs;
	}

	public LogMessagingImpl() {
		super();
	}

	@Override
	public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) throws Exception {
		logger.info("****** started execute ******* " + new Timestamp(System.currentTimeMillis()));
		logger.info("Main logs total size " + logs.size());
		for (Integer logId : logs.keySet()) {
			logger.info("Removing logs total size " + logs.get(logId).size());
			List<Map> existingLogs = logs.get(logId);
			Calendar calendar = Calendar.getInstance();
			calendar.setTime(new Timestamp(System.currentTimeMillis()));
			if (existingLogs != null) {
				for (int i = 0; i < existingLogs.size(); i++) {
					logger.info("Removing logs " + existingLogs.get(i).get("logId"));
					if (Objects.nonNull(existingLogs.get(i).get("maxAge"))) {
						calendar.add(Calendar.SECOND, -(Integer) existingLogs.get(i).get("maxAge"));
						Date date = calendar.getTime();
						Timestamp createdOn = new Timestamp(date.getTime());
						logger.info("Removing createdOn " + existingLogs.get(i).get("createdOn"));
						logger.info("Current time witn minus max age secods " + createdOn);
						logger.info("Removing maxAge " + existingLogs.get(i).get("maxAge"));
						if (createdOn.after((Timestamp) existingLogs.get(i).get("createdOn"))) {
							existingLogs.remove(existingLogs.get(i));
						} else {
							logger.info("Max not exceed for log id " + existingLogs.get(i).get("logId")
									+ " and created on " + existingLogs.get(i).get("createdOn"));
						}
					} else {
						logger.info("Max age not avalable for log id " + existingLogs.get(i).get("logId")
								+ " and created on " + existingLogs.get(i).get("createdOn"));
					}
				}
				logs.put(logId, existingLogs);
			}
			if (existingLogs == null || existingLogs.size() < 1) {
				logs.remove(logId);
			}
		}
		logger.info("****** End execute ******* ");
		return RepeatStatus.FINISHED;
	}

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

	@SuppressWarnings("unchecked")
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

	@Override
	public Map<String, Object> LogMessageStatistics() {
		ConcurrentHashMap<Integer, List<Map>> data = logs;
		Map<String, Object> MapResult = new HashMap<>();
		if (Objects.isNull(data) || data.isEmpty()) {
			MapResult.put("message", "Hello Word!!!!");
			return MapResult;
		}
		MapResult.put("currentNoOfStoredLogs", data.size());
		List<Map> logList = new ArrayList<>();
		Map<Integer, Integer> logsSize = new HashMap<>();
		for (Integer key : data.keySet()) {
			logList.addAll(data.get(key));
			logsSize.put(key, data.get(key).size());
		}
		List<Integer> maxAges = new ArrayList<>();
		for (Map map : logList) {
			if (Objects.nonNull(map.get("maxAge"))) {
				maxAges.add((Integer) map.get("maxAge"));
			}
		}
		Integer max = maxAges.stream().mapToInt(v -> v).max().orElseThrow(NoSuchElementException::new);
		MapResult.put("maxAgeLimit", max);
		MapResult.put("totalNoOfStoredMessages", logList.size());
		OptionalDouble average = logsSize.values().stream().mapToInt(number -> number.intValue()).average();
		MapResult.put("averageNoOfMsgPerLog", Integer.parseInt(String.valueOf(Math.round(average.getAsDouble()))));
		return MapResult;
	}

	@Override
	public void afterPropertiesSet() throws Exception {
		// TODO Auto-generated method stub

	}

	@Override
	public ConcurrentHashMap<Integer, List<Map>> getAllLogs() {
		// TODO Auto-generated method stub
		return logs;
	}
}

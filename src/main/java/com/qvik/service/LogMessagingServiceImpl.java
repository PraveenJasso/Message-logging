package com.qvik.service;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.qvik.Repository.LogMessaging;

@Service
public class LogMessagingServiceImpl implements LogMessagingService {

	@Autowired
	LogMessaging logMessaging;
	
	@Override
	public List<Map> getLog(int logId) {
		// TODO Auto-generated method stub
		return logMessaging.getLog(logId);
	}

	@Override
	public void addMessage(String name, int logId, String message) {
		logMessaging.addMessage(name, logId, message);
	}

}

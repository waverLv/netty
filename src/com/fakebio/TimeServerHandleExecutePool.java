package com.fakebio;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class TimeServerHandleExecutePool {
	private ExecutorService executor;

	public TimeServerHandleExecutePool(int threadNum,int querySize) {
		executor = new ThreadPoolExecutor(Runtime.getRuntime()
				.availableProcessors(), threadNum, 120L, TimeUnit.SECONDS,
				new ArrayBlockingQueue<java.lang.Runnable>(querySize));
	}
	
	public void execute(java.lang.Runnable task){
		executor.execute(task);
	}

}

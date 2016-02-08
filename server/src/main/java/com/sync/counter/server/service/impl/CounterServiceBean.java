package com.sync.counter.server.service.impl;

import com.sync.counter.server.exception.ServerException;
import com.sync.counter.server.service.CounterService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;

import javax.annotation.PostConstruct;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;

@Repository
public class CounterServiceBean implements CounterService {

	private static final Object LOCK = new Object();
	
	private RandomAccessFile file;

	@Value("${counter.file}")
	private String filename;
	
	@PostConstruct
	public void init() {
		try {
			file = new RandomAccessFile(new File(filename),"rw");
		}catch(FileNotFoundException ex) {
			throw new RuntimeException("Error reading/creating file", ex);
		}
	}
	
	private Integer readInteger() throws IOException {
		if(file.length() == 0) {
			return 0;
		}
		file.seek(0);
		return file.readInt();
	}
	
	private void writeInteger(Integer value) throws IOException {
		file.setLength(0);
		file.writeInt(value);
	}

	@Override
	public Integer currentValue() throws ServerException {
		synchronized (LOCK) {
			try {
				return readInteger();
			}catch(IOException e) {
				throw new ServerException(String.format("Error reading/writing to file. Error: %s",e.getMessage()));
			} finally {
				LOCK.notify();
			}
		}
	}

	@Override
	public Integer decrementAndReturn(Integer delta) throws ServerException {
		return incrementAndReturn(delta * -1);
	}

	@Override
	public Integer incrementAndReturn(Integer delta) throws ServerException {	
		synchronized (LOCK) {
			try {
				Integer value = readInteger();
				try { Thread.sleep(2); /*just to add some trouble ;) */}catch(InterruptedException ex) {}
				value = value + delta;
				writeInteger(value);
				return value;
			}catch(IOException e) {
				throw new ServerException(String.format("Error reading/writing to file. Error: %s",e.getMessage()));
			} finally {
				LOCK.notify();
			}
		}
	}

	@Override
	public void resetCounter() throws ServerException {
		synchronized (LOCK) {
			try {
				writeInteger(0);
			}catch(IOException e) {
				throw new ServerException(String.format("Error reading/writing to file. Error: %s",e.getMessage()));
			} finally {
				LOCK.notify();
			}
		}
	}
}

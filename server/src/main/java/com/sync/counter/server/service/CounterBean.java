package com.sync.counter.server.service;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;

import javax.annotation.PostConstruct;

import org.springframework.stereotype.Component;

import com.sync.counter.server.exception.ServerException;

@Component
public class CounterBean {

	private static final Object LOCK = new Object();
	
	private RandomAccessFile file;
	
	@PostConstruct
	public void init() {
		try {
			file = new RandomAccessFile(new File("counter.dat"),"rw");
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
	
	public Integer decrementAndReturn(Integer delta) throws ServerException {
		return incrementAndReturn(delta * -1);
	}
	
	public Integer incrementAndReturn(Integer delta) throws ServerException {	
		synchronized (LOCK) {
			try {
				Integer value = readInteger();
				try { Thread.sleep(150); /*just to add some trouble ;) */}catch(InterruptedException ex) {}
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
}

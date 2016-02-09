package com.sync.counter.server.service;

import com.sync.counter.server.exception.ServerException;

public interface CounterService {

    Integer currentValue() throws ServerException;

    Integer decrementAndReturn(Integer delta) throws ServerException;

    Integer incrementAndReturn(Integer delta) throws ServerException;

    void resetCounter() throws ServerException;

}

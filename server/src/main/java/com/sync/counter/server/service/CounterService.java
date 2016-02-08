package com.sync.counter.server.service;

import com.sync.counter.server.exception.ServerException;

/**
 * Created by sidnei on 08/02/16.
 */
public interface CounterService {

    Integer currentValue() throws ServerException;

    Integer decrementAndReturn(Integer delta) throws ServerException;

    Integer incrementAndReturn(Integer delta) throws ServerException;

    void resetCounter() throws ServerException;

}

package com.sync.counter.server.service;

import com.sync.counter.server.exception.ServerException;
import com.sync.counter.server.spring.AppConfig;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import static org.junit.Assert.assertEquals;

/**
 * Created by sidnei on 08/02/16.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {AppConfig.class}) /* Configuration */
public class CounterServiceTest {

    @Autowired
    private CounterService service;

    @Before
    public void resetCounter() throws ServerException {
        service.resetCounter();
    }

    @Test
    public void testZero() throws ServerException {
        assertEquals(0, service.currentValue().intValue());
    }

    @Test
    public void testIncrementCounter() throws ServerException {
        service.incrementAndReturn(5);
        assertEquals(5, service.currentValue().intValue());
    }

    @Test
    public void testDecrementCounter() throws ServerException {
        service.decrementAndReturn(20);
        assertEquals(-20, service.currentValue().intValue());
    }
}

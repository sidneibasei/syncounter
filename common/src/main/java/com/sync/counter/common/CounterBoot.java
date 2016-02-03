package com.sync.counter.common;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Created by sidnei on 03/02/16.
 */
public class CounterBoot {

    private static final Logger logger = LogManager.getLogger(CounterBoot.class);

    public void test(String n) {
        logger.info(n);
    }

}

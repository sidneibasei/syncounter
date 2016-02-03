package com.sync.counter.client;

import com.sync.counter.client.parser.CommandParser;
import com.sync.counter.common.CounterBoot;

/**
 * Created by sidnei on 03/02/16.
 */
public class Main {

    public static void main(String[] args) {


        new CommandParser("open connection to 127.0.0.1").parse();

    }
}

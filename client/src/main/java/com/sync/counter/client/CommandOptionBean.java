package com.sync.counter.client;

import com.sync.counter.common.protocol.CounterProtocolContants;
import org.apache.commons.cli.*;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

/**
 * Created by sidnei on 09/02/16.
 */
@Component
public class CommandOptionBean {

    private Options options;
    private CommandLineParser parser = new DefaultParser();

    private CommandLine commandLine;

    @PostConstruct
    protected void setup() {
        options = new Options();
        options.addOption("i","ip",true,"Remote IP address");
        options.addOption("r","random",false,"Run client without command Line. It sends 1000 requests to the server");
        options.addOption("h","help",false,"Show this screen");
    }

    public void initialize(String[] args) throws ParseException {
        commandLine = parser.parse( options, args);
    }

    public boolean isHelp() {
        return commandLine.hasOption('h');
    }

    public String getRemoteIp() {
        if(commandLine.hasOption('i')) {
           return commandLine.getOptionValue('i');
        }
        return CounterProtocolContants.DEFAULT_SERVER;
    }

    public Boolean isRandomMode() {
        return commandLine.hasOption('r');
    }

    public void showOptions() {
        HelpFormatter formatter = new HelpFormatter();
        formatter.printHelp( "java -jar client/target/client-exec.jar", options );
    }

}

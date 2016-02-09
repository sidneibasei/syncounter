package com.sync.counter.client.console;

import java.io.BufferedReader;
import java.io.Console;
import java.io.IOException;
import java.io.InputStreamReader;

import org.springframework.stereotype.Component;

@Component
public class ConsoleManager {

    private Console console = System.console();

    public String readConsole() {
    	if(console != null) {
            return console.readLine();
    	} else {
    		try {
    			return new BufferedReader(new InputStreamReader(System.in)).readLine();
    		}catch(IOException e) {
    			e.printStackTrace();
    		}
    	}
    	return null;
    }

    public void writeToConsole(String str, Object ... objs) {

    	if(console != null) {
    		console.printf(str, objs);
			console.printf("\n");
    	} else {
    		System.out.println(String.format(str,  objs));
    	}
    }
}

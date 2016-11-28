package com.c301t19.cs.ualberta.seekaride.core;

import java.util.ArrayList;

/**
 * driver command patten
 */
public class DriverCommand {

    public enum CommandType {  ACCEPT_REQUEST}

    protected CommandType commandType;
    protected ArrayList<Object> params;

    /**
     * Driver Command
     * @param commandType
     * @param params
     */
    public DriverCommand(CommandType commandType, ArrayList<Object> params) {
        this.commandType = commandType;
        this.params = params;
    }

    /**
     * execute
     */
    public void execute() {
        switch (commandType) {
            case ACCEPT_REQUEST:
                Driver.getInstance().acceptRequest((Request)params.get(0));
                break;

        }
    }
}

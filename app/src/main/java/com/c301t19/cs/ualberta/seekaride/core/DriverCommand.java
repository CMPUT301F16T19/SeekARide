package com.c301t19.cs.ualberta.seekaride.core;

import java.util.ArrayList;

public class DriverCommand {

    public enum CommandType {  ACCEPT_REQUEST}

    private CommandType commandType;
    private ArrayList<Object> params;

    public DriverCommand(CommandType commandType, ArrayList<Object> params) {
        this.commandType = commandType;
        this.params = params;
    }

    public void execute() {
        switch (commandType) {
            case ACCEPT_REQUEST:
                Driver.getInstance().acceptRequest((Request)params.get(0));
                break;

        }
    }
}

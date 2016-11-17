package com.c301t19.cs.ualberta.seekaride.core;

import java.util.ArrayList;

public class DriverCommand {

    public enum CommandType { SEARCH_REQUESTS_BY_KEYWORD, ACCEPT_REQUEST, REMOVE_ACCEPTED_REQUEST }

    private CommandType commandType;
    private ArrayList<Object> params;

    public DriverCommand(CommandType commandType, ArrayList<Object> params) {
        this.commandType = commandType;
        this.params = params;
    }

    public void execute() {
        switch (commandType) {
            case SEARCH_REQUESTS_BY_KEYWORD:
                Driver.getInstance().searchRequestsByKeyword((String)params.get(0), (String)params.get(1));
                break;
            case ACCEPT_REQUEST:
                Driver.getInstance().acceptRequest((Request)params.get(0));
                break;
            case REMOVE_ACCEPTED_REQUEST:
                Driver.getInstance().removeAcceptedRequest((Request)params.get(0));
                break;
        }
    }
}

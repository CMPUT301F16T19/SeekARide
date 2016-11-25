package com.c301t19.cs.ualberta.seekaride.core;

import java.util.ArrayList;

public class RiderCommand {

    public enum CommandType {MAKE_REQUEST, DELETE_REQUEST, EDIT_REQUEST, MAKE_PAYMENT, }

    private CommandType commandType;
    private ArrayList<Object> params;

    public RiderCommand(CommandType commandType, ArrayList<Object> params) {
        this.commandType = commandType;
        this.params = params;
    }

    public void execute() {
        switch (commandType) {
            case MAKE_REQUEST:
                Rider.getInstance().makeRequest((String)params.get(0), (Location)params.get(1),
                                                (Location)params.get(2), (Double)params.get(3));
                break;
            case DELETE_REQUEST:
                Rider.getInstance().deleteRequest((Request)params.get(0));
                break;
            case EDIT_REQUEST:
                Rider.getInstance().editRequest((Request)params.get(0));
                break;
            case MAKE_PAYMENT:
                Rider.getInstance().makePayment((Request)params.get(0));
                break;

        }
    }
}

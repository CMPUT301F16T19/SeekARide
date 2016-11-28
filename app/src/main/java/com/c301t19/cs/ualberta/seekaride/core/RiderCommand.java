package com.c301t19.cs.ualberta.seekaride.core;

import java.util.ArrayList;

/**
 * Command pattern for Rider tasks. These can be stored while the Rider is offline,
 * then executed on reconnection.
 */
public class RiderCommand {

    /**
     * Enum corresponding to various Rider tasks.
     */
    public enum CommandType {MAKE_REQUEST, DELETE_REQUEST, EDIT_REQUEST, MAKE_PAYMENT, }

    protected CommandType commandType;
    protected ArrayList<Object> params;

    /**
     * Create a command by passing a command type and the associated parameters.
     * @param commandType Indicates the Rider function associated with the command.
     * @param params Parameters associated with the function.
     */
    public RiderCommand(CommandType commandType, ArrayList<Object> params) {
        this.commandType = commandType;
        this.params = params;
    }

    /**
     * Executes the command.
     */
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

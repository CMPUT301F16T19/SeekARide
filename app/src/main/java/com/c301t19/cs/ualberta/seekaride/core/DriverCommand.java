package com.c301t19.cs.ualberta.seekaride.core;

import java.util.ArrayList;

/**
 * Command pattern for Driver tasks. These can be stored while the Driver is offline,
 * then executed on reconnection.
 */
public class DriverCommand {

    /**
     * Enum corresponding to various Driver tasks
     */
    public enum CommandType { ACCEPT_REQUEST }

    protected CommandType commandType;
    protected ArrayList<Object> params;

    /**
     * Create a command by passing a command type and the associated parameters.
     * @param commandType Indicates the Driver function associated with the command.
     * @param params Parameters associated with the function.
     */
    public DriverCommand(CommandType commandType, ArrayList<Object> params) {
        this.commandType = commandType;
        this.params = params;
    }

    /**
     * Executes the command.
     */
    public void execute() {
        switch (commandType) {
            case ACCEPT_REQUEST:
                Driver.getInstance().acceptRequest((Request)params.get(0));
                break;

        }
    }
}

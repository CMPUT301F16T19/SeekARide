package com.c301t19.cs.ualberta.seekaride.mock;

import com.c301t19.cs.ualberta.seekaride.core.Driver;
import com.c301t19.cs.ualberta.seekaride.core.DriverCommand;
import com.c301t19.cs.ualberta.seekaride.core.Request;

import java.util.ArrayList;

/**
 * Created by Master Chief on 2016-11-25.
 */
public class MockDriverCommand extends DriverCommand {

    public MockDriverCommand(CommandType commandType, ArrayList<Object> params) {
        super(commandType,params);
    }

    @Override
    public void execute() {
        switch (commandType) {
            case ACCEPT_REQUEST:
                MockDriver.getInstance().acceptRequest((Request)params.get(0));
                break;

        }
    }
}

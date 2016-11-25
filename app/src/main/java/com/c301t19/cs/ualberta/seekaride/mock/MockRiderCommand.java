package com.c301t19.cs.ualberta.seekaride.mock;

import com.c301t19.cs.ualberta.seekaride.core.Location;
import com.c301t19.cs.ualberta.seekaride.core.Request;
import com.c301t19.cs.ualberta.seekaride.core.Rider;
import com.c301t19.cs.ualberta.seekaride.core.RiderCommand;

import java.util.ArrayList;

/**
 * Created by Master Chief on 2016-11-25.
 */
public class MockRiderCommand extends RiderCommand {

    public MockRiderCommand(CommandType commandType, ArrayList<Object> params) {
        super(commandType,params);
    }

    @Override
    public void execute() {
        switch (commandType) {
            case MAKE_REQUEST:
                MockRider.getInstance().makeRequest((String)params.get(0), (Location)params.get(1),
                        (Location)params.get(2), (Double)params.get(3));
                break;
            case DELETE_REQUEST:
                MockRider.getInstance().deleteRequest((Request)params.get(0));
                break;
            case EDIT_REQUEST:
                MockRider.getInstance().editRequest((Request)params.get(0));
                break;
            case MAKE_PAYMENT:
                MockRider.getInstance().makePayment((Request)params.get(0));
                break;

        }
    }
}

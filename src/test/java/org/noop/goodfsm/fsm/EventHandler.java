package org.noop.goodfsm.fsm;


import org.noop.processing.AbstractActionHandler;
import org.noop.processing.IEventProcessor;

public class EventHandler extends AbstractActionHandler<Action1> {

    public EventHandler() {
        super(Action1.class);
    }


    @Override
    public void onReceive(IEventProcessor p_EventProcessor, Action1 p_Event) throws Exception {
    }


}

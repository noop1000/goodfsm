package org.noop.goodfsm.fsm;


import org.noop.processing.AbstractActionHandler;
import org.noop.processing.IEventProcessor;

import java.util.function.BiConsumer;


public class LambdaEventHandler<T>
        extends AbstractActionHandler<T> {
    private final BiConsumer<IEventProcessor, T> Lambda;


    public LambdaEventHandler(Class p_Class, BiConsumer<IEventProcessor, T> p_eventLambda) {
        super(p_Class);
        this.Lambda = p_eventLambda;
    }


    @Override
    public void onReceive(IEventProcessor p_eventProcessor, T p_event) throws Exception {
        if (this.Lambda != null) {
            this.Lambda.accept(p_eventProcessor, p_event);
        }
    }
}

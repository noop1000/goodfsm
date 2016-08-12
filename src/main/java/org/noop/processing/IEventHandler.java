package org.noop.processing;


public interface IEventHandler<T> {

    void onReceive(IEventProcessor p_eventProcessor, T p_event) throws Exception;


    Class getSupportedAction();

}

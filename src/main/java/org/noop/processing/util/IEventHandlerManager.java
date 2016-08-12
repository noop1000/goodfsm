package org.noop.processing.util;

import org.noop.processing.IEventHandler;
import org.noop.processing.IEventProcessor;

import java.util.Set;

public interface IEventHandlerManager {


    /**
     * return true if handled.
     *
     * @param p_eventProcessor
     * @param p_event
     * @return
     * @throws Exception
     */
    boolean applyEvent(IEventProcessor p_eventProcessor, Object p_event) throws Exception;

    void addHandler(IEventHandler p_handler);

    boolean removeHandler(IEventHandler p_handler);

}

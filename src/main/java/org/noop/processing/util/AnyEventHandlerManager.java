package org.noop.processing.util;


import org.noop.processing.IEventHandler;
import org.noop.processing.IEventProcessor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;


public class AnyEventHandlerManager implements IEventHandlerManager {

    private final static Logger logger = LoggerFactory.getLogger(AnyEventHandlerManager.class);


    private final Set<IEventHandler> handlers = new HashSet<>();


    @Override
    public boolean applyEvent(final IEventProcessor p_eventProcessor, final Object p_event) throws Exception {
        boolean result = false;

        if (this.handlers.size() > 0) {
            result = true;

            List<IEventHandler> HandlersToProcess = new ArrayList<>(this.handlers);

            for (IEventHandler Handler : HandlersToProcess) {
                try {
                    Handler.onReceive(p_eventProcessor, p_event);
                } catch (Exception ex) {
                    logger.error("error in handler {}", ex);
                }
            }
        }

        return result;
    }


    @Override
    public void addHandler(IEventHandler p_handler) {
        if (p_handler != null) {
            this.handlers.add(p_handler);
        }
    }


    @Override
    public boolean removeHandler(IEventHandler p_handler) {
        boolean result = false;

        if (p_handler != null) {
            this.handlers.remove(p_handler);
        }

        return result;
    }


}

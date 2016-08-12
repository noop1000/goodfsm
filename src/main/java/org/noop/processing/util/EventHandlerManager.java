package org.noop.processing.util;


import org.noop.processing.IEventHandler;
import org.noop.processing.IEventProcessor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;


public class EventHandlerManager implements IEventHandlerManager {

    private final Logger logger = LoggerFactory.getLogger(EventHandlerManager.class);


    private final Map<Class, Set<IEventHandler>> handlers = new HashMap<>();


    @Override
    public boolean applyEvent(final IEventProcessor p_eventProcessor, final Object p_event) throws Exception {
        boolean result = false;

        Class Action = p_event.getClass();

        if (this.handlers.containsKey(Action)) {
            result = true;

            List<IEventHandler> HandlersToProcess = new ArrayList<>(this.handlers.get(Action));

            for (IEventHandler Handler : HandlersToProcess) {
                try {
                    Handler.onReceive(p_eventProcessor, p_event);
                } catch (Exception ex) {
                    logger.error("error in handler. {}", ex);
                }
            }
        }

        return result;
    }


    @Override
    public void addHandler(IEventHandler p_handler) {
        if (p_handler != null) {
            Class Action = p_handler.getSupportedAction();

            Set<IEventHandler> Handlers = this.handlers.get(Action);

            if (Handlers == null) {
                Handlers = new LinkedHashSet<>();
                this.handlers.put(Action, Handlers);
            }

            Handlers.add(p_handler);

        }
    }


    @Override
    public boolean removeHandler(IEventHandler p_handler) {
        boolean result = false;

        if (p_handler != null) {
            Class Action = p_handler.getSupportedAction();

            Set<IEventHandler> PojoHandlers = this.handlers.get(Action);

            if (PojoHandlers != null) {
                result = PojoHandlers.remove(p_handler);
                if (PojoHandlers.isEmpty()) {
                    this.handlers.remove(Action);
                }
            }

        }

        return result;
    }


    public Set<IEventHandler> getHandlersForEvent(Object p_event) {
        Set<IEventHandler> result = null;

        Class Action = p_event.getClass();

        result = this.handlers.get(Action);

        return result;

    }


    public Set<Class> getRegisteredEvents() {
        Set<Class> result = new HashSet<>(this.handlers.keySet());

        return result;
    }


}

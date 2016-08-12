package org.noop.processing;

import org.noop.processing.AbstractActionHandler;
import org.noop.processing.IEventHandler;
import org.noop.processing.IEventProcessor;

import java.util.HashSet;
import java.util.Set;
import java.util.function.BiConsumer;


public class HandlerUtils {


    public static Set<IEventHandler> buildMessageHandlers(final Set<? extends Class> p_MessageTypes,
                                                   final BiConsumer<IEventProcessor, Object> p_handler) {
        Set<IEventHandler> result = new HashSet<>();

        for (Class EventClass : p_MessageTypes) {
            IEventHandler<Object> tmpHandler = new AbstractActionHandler<Object>(EventClass) {
                @Override
                public void onReceive(IEventProcessor p_eventProcessor, Object p_event) throws Exception {
                    p_handler.accept(p_eventProcessor, p_event);
                }
            };

            result.add(tmpHandler);
        }

        return result;
    }


    public static Set<IEventHandler> buildMessageHandlers(final Set<? extends Class> p_MessageTypes,
                                                   final IEventHandler<Object> p_handler) {
        Set<IEventHandler> result = new HashSet<>();

        for (Class EventClass : p_MessageTypes) {
            IEventHandler<Object> tmpHandler = new AbstractActionHandler<Object>(EventClass) {
                @Override
                public void onReceive(IEventProcessor p_eventProcessor, Object p_event) throws Exception {
                    p_handler.onReceive(p_eventProcessor, p_event);
                }
            };

            result.add(tmpHandler);
        }

        return result;

    }

}

package org.noop.processing.util;


import org.noop.processing.IEventHandler;
import org.noop.processing.IEventProcessor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;


/**
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */
public class EventHandlerManager implements IEventHandlerManager {

    private final Logger logger = LoggerFactory.getLogger(EventHandlerManager.class);


    private final Map<Class, Set<IEventHandler>> handlers = new HashMap<>();


    @Override
    public boolean applyEvent(final IEventProcessor p_eventProcessor, final Object p_event) throws Exception {
        boolean result = false;

        Class event = p_event.getClass();

        if (this.handlers.containsKey(event)) {
            result = true;

            List<IEventHandler> HandlersToProcess = new ArrayList<>(this.handlers.get(event));

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
            Class event = p_handler.getSupportedAction();

            Set<IEventHandler> Handlers = this.handlers.get(event);

            if (Handlers == null) {
                Handlers = new LinkedHashSet<>();
                this.handlers.put(event, Handlers);
            }

            Handlers.add(p_handler);

        }
    }


    @Override
    public boolean removeHandler(IEventHandler p_handler) {
        boolean result = false;

        if (p_handler != null) {
            Class event = p_handler.getSupportedAction();

            Set<IEventHandler> pojoHandlers = this.handlers.get(event);

            if (pojoHandlers != null) {
                result = pojoHandlers.remove(p_handler);
                if (pojoHandlers.isEmpty()) {
                    this.handlers.remove(event);
                }
            }

        }

        return result;
    }


    public Set<IEventHandler> getHandlersForEvent(Object p_event) {
        Set<IEventHandler> result = null;

        Class event = p_event.getClass();

        result = this.handlers.get(event);

        return result;

    }


    public Set<Class> getRegisteredEvents() {
        Set<Class> result = new HashSet<>(this.handlers.keySet());

        return result;
    }


}

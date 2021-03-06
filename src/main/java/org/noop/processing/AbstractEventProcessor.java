package org.noop.processing;


import org.noop.exceptions.NotImplementedException;
import org.noop.goodfsm.fsm.scheduler.IScheduler;
import org.noop.goodfsm.fsm.scheduler.ISchedulerAware;
import org.noop.processing.util.AnyEventHandlerManager;
import org.noop.processing.util.EventHandlerManager;
import org.noop.processing.util.IEventHandlerManager;

import java.util.Set;


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
public abstract class AbstractEventProcessor
        implements IEventProcessor<Object>, ISchedulerAware<Object> {

    private EventHandlerManager eventHandlers = new EventHandlerManager();


    private EventHandlerManager preEventHandlers = new EventHandlerManager();


    private EventHandlerManager postEventHandlers = new EventHandlerManager();


    private AnyEventHandlerManager preDefaultEventHandlers = new AnyEventHandlerManager();

    private AnyEventHandlerManager defaultEventHandlers = new AnyEventHandlerManager();

    private AnyEventHandlerManager postDefaultEventHandlers = new AnyEventHandlerManager();


    private AnyEventHandlerManager preUnknownEventHandlers = new AnyEventHandlerManager();

    private AnyEventHandlerManager unknownEventHandlers = new AnyEventHandlerManager();

    private AnyEventHandlerManager postUnknownEventHandlers = new AnyEventHandlerManager();


    private Object currentEvent = null;


    private volatile IScheduler scheduler = null;


    private boolean postEventHandlersEnabled = true;


    private boolean preEventHandlersEnabled = true;


    private IEventProcessor container = this;


    public AbstractEventProcessor() {
        super();

    }


    public AbstractEventProcessor(IEventProcessor p_Container) {
        this();

        if (p_Container != null) {
            this.container = p_Container;
        }
    }


    @Override
    public boolean onReceive(Object p_event) throws Exception {
        setCurrentEvent(p_event);

        prepareHandlersForNotify(p_event);

        if (this.preEventHandlersEnabled) {
            firePreEventHandlers(p_event);
        }

        boolean handled = fireEventHandlers(p_event);

        if (this.postEventHandlersEnabled) {
            firePostEventHandlers(p_event);
        }

        return handled;
    }


    public boolean firePreEventHandlers(Object p_event) throws Exception {

        boolean eventHandled = this.applyEventToHandlers(p_event,
                this.preEventHandlers,
                this.preDefaultEventHandlers,
                this.preUnknownEventHandlers);

        return eventHandled;
    }


    public boolean firePostEventHandlers(Object p_event) throws Exception {
        boolean eventHandled = this.applyEventToHandlers(p_event,
                this.postEventHandlers,
                this.postDefaultEventHandlers,
                this.postUnknownEventHandlers);

        return eventHandled;

    }


    public boolean fireEventHandlers(Object p_event) throws Exception {

        boolean result = this.eventHandlers.applyEvent(getContainer(), p_event);

        result = result || this.defaultEventHandlers.applyEvent(getContainer(), p_event);

        return result;

    }

    @Override
    public boolean handleUnknownEvent(Object p_event) throws Exception {
        return this.unknownEventHandlers.applyEvent(getContainer(), p_event);
    }


    /**
     * override for custom prep here as part of the onReceive.
     */
    public void prepareHandlersForNotify(Object p_event) {
        // overwrite to do something.
    }


    @Override
    public Object addPreEventHandler(IEventHandler p_handler) {
        if (p_handler != null) {
            this.preEventHandlers.addHandler(p_handler);
        }

        return this;
    }


    @Override
    public void removePreEventHandler(IEventHandler p_handler) {
        if (p_handler != null) {
            this.preEventHandlers.removeHandler(p_handler);
        }
    }


    @Override
    public Object addEventHandler(IEventHandler p_handler) {
        if (p_handler != null) {
            this.eventHandlers.addHandler(p_handler);
        }

        return this;
    }


    @Override
    public void removeEventHandler(IEventHandler p_handler) {
        if (p_handler != null) {
            this.eventHandlers.removeHandler(p_handler);
        }
    }


    public IEventHandlerManager getEventHandlers() {
        return this.eventHandlers;
    }


    @Override
    public Object getCurrentEvent() {
        return this.currentEvent;
    }


    public void setCurrentEvent(Object p_event) {
        this.currentEvent = p_event;
    }


    @Override
    public Set<Class> getRegisteredEventTypes() {
        Set<Class> result = this.eventHandlers.getRegisteredEvents();

        return result;
    }


    @Override
    public IScheduler getScheduler() {
        return this.scheduler;
    }


    @Override
    public Object setScheduler(IScheduler p_Scheduler) {
        this.scheduler = p_Scheduler;
        return this;
    }


    @Override
    public Object addPostEventHandler(IEventHandler p_handler) {
        if (p_handler != null) {
            this.postEventHandlers.addHandler(p_handler);
        }

        return this;
    }


    @Override
    public void removePostEventHandler(IEventHandler p_handler) {
        if (p_handler != null) {
            this.postEventHandlers.addHandler(p_handler);
        }
    }


    protected IEventHandlerManager getPostEventHandlers() {
        return this.postEventHandlers;
    }


    public boolean isPostEventHandlersEnabled() {
        return this.postEventHandlersEnabled;
    }


    public void setPostEventHandlersEnabled(boolean p_postEventHandlersEnabled) {
        this.postEventHandlersEnabled = p_postEventHandlersEnabled;
    }


    public boolean isPreEventHandlersEnabled() {
        return this.preEventHandlersEnabled;
    }


    public void setPreEventHandlersEnabled(boolean p_preEventHandlersEnabled) {
        this.preEventHandlersEnabled = p_preEventHandlersEnabled;
    }


    @Override
    public Object addPreEventHandlers(Set<IEventHandler> p_handlers) {
        if (p_handlers != null) {
            for (IEventHandler Handler : p_handlers) {
                addPreEventHandler(Handler);
            }
        }

        return this;
    }


    @Override
    public Object addEventHandlers(Set<IEventHandler> p_handlers) {
        if (p_handlers != null) {
            for (IEventHandler Handler : p_handlers) {
                addEventHandler(Handler);
            }
        }

        return this;
    }


    @Override
    public Object addPostEventHandlers(Set<IEventHandler> p_handlers) {
        if (p_handlers != null) {
            for (IEventHandler Handler : p_handlers) {
                addPostEventHandler(Handler);
            }
        }

        return this;
    }


    @Override
    public void removePreEventHandlers(Set<IEventHandler> p_handlers) {
        if (p_handlers != null) {
            for (IEventHandler Handler : p_handlers) {
                removePreEventHandler(Handler);
            }
        }
    }


    @Override
    public void removeEventHandlers(Set<IEventHandler> p_handlers) {
        if (p_handlers != null) {
            for (IEventHandler Handler : p_handlers) {
                removeEventHandler(Handler);
            }
        }
    }


    @Override
    public void removePostEventHandlers(Set<IEventHandler> p_handlers) {
        if (p_handlers != null) {
            for (IEventHandler Handler : p_handlers) {
                removePostEventHandler(Handler);
            }
        }
    }


    public Object addPreDefaultEventHandler(IEventHandler<Object> p_handler) {
        if (p_handler != null) {
            this.preDefaultEventHandlers.addHandler(p_handler);
        }

        return this;
    }


    /**
     * Register a message handler that is called for ALL messages.
     *
     * @param p_handler
     */
    public Object addDefaultEventHandler(IEventHandler<Object> p_handler) {
        if (p_handler != null) {
            this.defaultEventHandlers.addHandler(p_handler);
        }

        return this;
    }


    public Object addPostDefaultEventHandler(IEventHandler<Object> p_handler) {
        if (p_handler != null) {
            this.postDefaultEventHandlers.addHandler(p_handler);
        }

        return this;
    }


    public void removeDefaultEventHandler(IEventHandler<Object> p_handler) {
        if (p_handler != null) {
            this.defaultEventHandlers.removeHandler(p_handler);
        }
    }


    public void removePreDefaultEventHandler(IEventHandler<Object> p_handler) {
        if (p_handler != null) {
            this.preDefaultEventHandlers.removeHandler(p_handler);
        }
    }


    public void removePostDefaultEventHandler(IEventHandler<Object> p_handler) {
        if (p_handler != null) {
            this.postDefaultEventHandlers.removeHandler(p_handler);
        }
    }


    public Object addPreUnhandledHandler(IEventHandler<Object> p_handler) {
        if (p_handler != null) {
            this.preUnknownEventHandlers.addHandler(p_handler);
        }

        return this;
    }


    /**
     * If there is no message handler registered for a given message type or if the there is
     * no default handler (called for all messages) this handler will be called.  Good for logging or some
     * default behavior for messages that don't  have a specific handler registered.
     *
     * @param p_handler
     */
    public Object addUnhandledEventHandler(IEventHandler<Object> p_handler) {
        if (p_handler != null) {
            this.unknownEventHandlers.addHandler(p_handler);
        }

        return this;
    }


    public Object addPostUnhandledEventHandler(IEventHandler<Object> p_handler) {
        if (p_handler != null) {
            this.postUnknownEventHandlers.addHandler(p_handler);
        }

        return this;
    }


    public void removeUnhandledEventHandler(IEventHandler<Object> p_handler) {
        if (p_handler != null) {
            this.unknownEventHandlers.removeHandler(p_handler);
        }
    }


    public void removePreUnhandledEventHandler(IEventHandler<Object> p_handler) {
        if (p_handler != null) {
            this.preUnknownEventHandlers.removeHandler(p_handler);
        }
    }


    public void removePostUnhandledEventHandler(IEventHandler<Object> p_handler) {
        if (p_handler != null) {
            this.postUnknownEventHandlers.removeHandler(p_handler);
        }
    }


    @Override
    public void init() {

    }


    protected IEventProcessor getContainer() {
        return this.container;
    }


    private boolean applyEventToHandlers(Object p_event,
                                         IEventHandlerManager p_mainHanders,
                                         IEventHandlerManager p_defaultHandlers,
                                         IEventHandlerManager p_unknownHandlers) throws Exception {

        boolean result = p_mainHanders.applyEvent(this, p_event);

        result = result || p_defaultHandlers.applyEvent(this, p_event);

        if (!result) {
            result = p_unknownHandlers.applyEvent(this, p_event);
        }

        return result;
    }


}

package org.noop.goodfsm.fsm;


import org.noop.goodfsm.akka.IStashAware;
import org.noop.goodfsm.events.common.EventStateTimeout;
import org.noop.goodfsm.fsm.scheduler.ICancellable;
import org.noop.goodfsm.fsm.scheduler.IScheduler;
import org.noop.goodfsm.fsm.scheduler.ISchedulerAware;
import org.noop.processing.AbstractActionHandler;
import org.noop.processing.IEventHandler;
import org.noop.processing.IEventProcessor;
import org.noop.processing.util.AnyEventHandlerManager;
import org.noop.processing.util.EventHandlerManager;
import org.noop.processing.util.IEventHandlerManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Set;
import java.util.UUID;

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
public abstract class AbstractState
        implements IState, ISchedulerAware<IState> {

    private final Logger logger = LoggerFactory.getLogger(AbstractState.class);
    private final EventHandlerManager eventHandlers = new EventHandlerManager();
    private IFsm fsm;
    private UUID id = null;
    private Class<? extends IState> prevState;
    private Object reason;
    private long enterTime = System.currentTimeMillis();

    private EventHandlerManager preEventHandlers = new EventHandlerManager();
    private EventHandlerManager postEventHandlers = new EventHandlerManager();
    private AnyEventHandlerManager preDefaultEventHandlers = new AnyEventHandlerManager();
    private AnyEventHandlerManager defaultEventHandlers = new AnyEventHandlerManager();
    private AnyEventHandlerManager postDefaultEventHandlers = new AnyEventHandlerManager();
    private AnyEventHandlerManager preUnknownEventHandlers = new AnyEventHandlerManager();
    private AnyEventHandlerManager unknownEventHandlers = new AnyEventHandlerManager();
    private AnyEventHandlerManager postUnknownEventHandlers = new AnyEventHandlerManager();


    private Class<? extends IState> timeoutState = null;


    IEventHandler<EventStateTimeout> timeoutHandler = new AbstractActionHandler<EventStateTimeout>(EventStateTimeout.class) {
        @Override
        public void onReceive(IEventProcessor p_eventProcessor, EventStateTimeout p_event) throws Exception {

            if (p_event.getStateId().equals(getId())) {
                fsm.transitionTo(timeoutState);
            }
        }
    };

    private long timeoutDuration = -1;
    private Object currentEvent = null;

    private IScheduler scheduler = null;
    private ICancellable timeoutCancel = null;

    public AbstractState() {
        super();
        this.id = UUID.randomUUID();
    }

    @Override
    public UUID getId() {
        return this.id;
    }


    public void setId(UUID p_id) {
        this.id = p_id;
    }


    @Override
    public IFsm getFsm() {
        return this.fsm;
    }


    public void setFsm(IFsm p_fsm) {
        this.fsm = p_fsm;
    }


    @Override
    public void onStateEnter(IState p_oldState,
                             IState p_newState) {
        // TODO - in sub classes overwrite as needed
    }


    @Override
    public void onStateExit(IState p_State) {
        // TODO - in sub classes overwrite as needed
    }


    @Override
    public Class<? extends IState> getPrevState() {
        return this.prevState;
    }


    @Override
    public void setPrevState(Class<? extends IState> p_prevState) {
        this.prevState = p_prevState;
    }


    @Override
    public Object getReason() {
        return this.reason;
    }


    @Override
    public void setReason(Object p_reason) {
        this.reason = p_reason;
    }


    @Override
    public long getEnterTime() {
        return this.enterTime;
    }


    @Override
    public IState addPreEventHandler(IEventHandler p_handler) {
        if (p_handler != null) {
            this.eventHandlers.addHandler(p_handler);
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
    public IState addEventHandler(IEventHandler p_handler) {
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


    @Override
    public IState addPostEventHandler(IEventHandler p_handler) {
        if (p_handler != null) {
            this.eventHandlers.removeHandler(p_handler);
        }

        return this;
    }


    @Override
    public void removePostEventHandler(IEventHandler p_handler) {
        if (p_handler != null) {
            this.postEventHandlers.removeHandler(p_handler);
        }
    }


    @Override
    public void setTimeout(long p_TimeMillisec,
                           Class<? extends IState> p_newState) {
        if (this.timeoutCancel != null) {
            this.timeoutCancel.cancel();
            this.timeoutCancel = null;
        }

        if (p_TimeMillisec > 0) {
            this.timeoutState = p_newState;
            this.timeoutDuration = p_TimeMillisec;
            if (getScheduler() != null) {
                processTimeoutSetting();
            }
        }
    }


    /**
     * Default impl just calls the handlers
     *
     * @param p_event
     * @return
     * @throws Exception
     */
    @Override
    public boolean onReceive(Object p_event) throws Exception {

        boolean eventHandled = false;

        if (this.currentEvent != null) {
            throw new InvalidStateOpException(this.getClass().getName(), null);
        }

        boolean allGood = true;

        if (!DontCheckStateAnnotationProcessor.canIgnoreState(p_event)) {
            allGood = allGood && this.canProcess(p_event);
        }

        if (allGood) {
            setCurrentEvent(p_event);
            prepareHandlersForNotify(p_event);

            eventHandled = this.eventHandlers.applyEvent(this, p_event);

            eventHandled = eventHandled || this.defaultEventHandlers.applyEvent(this, p_event);

        }

        return eventHandled;
    }

    @Override
    public boolean handleUnknownEvent(Object p_event) throws Exception {
        return this.unknownEventHandlers.applyEvent(this, p_event);
    }


    /**
     * default impl always return true;
     *
     * @param p_event
     * @return
     */
    @Override
    public boolean canProcess(Object p_event) {
        return true;
    }


    protected IEventHandlerManager getEventHandlers() {
        return this.eventHandlers;
    }


    protected void prepareHandlersForNotify(Object p_event) {
        // override
    }


    public void transitionTo(Class<? extends IState> p_newState) throws Exception {
        if (this.timeoutCancel != null) {
            this.timeoutCancel.cancel();
        }

        this.fsm.transitionTo(p_newState);
    }

    @Override
    public Object getCurrentEvent() {
        return this.currentEvent;
    }

    @Override
    public void setCurrentEvent(Object p_event) {
        this.currentEvent = p_event;
    }

    public Set<Class> getRegisteredEventTypes() {
        Set<Class> result = this.eventHandlers.getRegisteredEvents();

        return result;
    }


    @Override
    public IScheduler getScheduler() {
        return this.scheduler;
    }


    @Override
    public IState setScheduler(IScheduler p_Scheduler) {
        if (this.scheduler != p_Scheduler) {
            this.scheduler = p_Scheduler;

            if (this.scheduler != null) {
                processTimeoutSetting();
            }
        }

        return this;
    }


    protected void processTimeoutSetting() {
        if (getScheduler() != null) {
            if ((this.timeoutDuration > 0) && (this.timeoutCancel == null)) {
                this.addEventHandler(this.timeoutHandler);

                final EventStateTimeout timeoutEvent = new EventStateTimeout(calcTimeoutScheduleId(), getId());

                this.timeoutCancel = getScheduler().scheduleOnce(this.timeoutDuration, timeoutEvent.getScheduleId(), timeoutEvent);
            }
        }

    }


    @Override
    public void registerTransition(Class p_event, Class<? extends IState> p_newState) {
        final Class actionClass = p_event;
        final Class<? extends IState> newState = p_newState;

        IEventHandler<Object> eventHandler = new IEventHandler<Object>() {
            @Override
            public void onReceive(IEventProcessor p_eventProcessor, Object p_event) throws Exception {
                transitionTo(newState);
            }


            @Override
            public Class getSupportedAction() {
                return actionClass;
            }
        };

        addEventHandler(eventHandler);

    }


    public String calcTimeoutScheduleId() {
        return new StringBuffer(this.getId().toString())
                .append(this.getClass().getName())
                .append("_")
                .append(EventStateTimeout.class.getSimpleName())
                .toString();

    }


    @Override
    public IState addPreEventHandlers(Set<IEventHandler> p_handlers) {
        if (p_handlers != null) {
            for (IEventHandler tmpEventHandler : p_handlers) {
                addPreEventHandler(tmpEventHandler);
            }
        }

        return this;
    }


    @Override
    public IState addEventHandlers(Set<IEventHandler> p_handlers) {
        if (p_handlers != null) {
            for (IEventHandler tmpEventHandler : p_handlers) {
                addEventHandler(tmpEventHandler);
            }
        }

        return this;
    }


    @Override
    public IState addPostEventHandlers(Set<IEventHandler> p_handlers) {
        if (p_handlers != null) {
            for (IEventHandler tmpEventHandler : p_handlers) {
                addPostEventHandler(tmpEventHandler);
            }
        }

        return this;
    }


    @Override
    public void removePreEventHandlers(Set<IEventHandler> p_handlers) {
        if (p_handlers != null) {
            for (IEventHandler tmpEventHandler : p_handlers) {
                removePreEventHandler(tmpEventHandler);
            }
        }
    }


    @Override
    public void removeEventHandlers(Set<IEventHandler> p_handlers) {
        if (p_handlers != null) {
            for (IEventHandler tmpEventHandler : p_handlers) {
                removeEventHandler(tmpEventHandler);
            }
        }
    }


    @Override
    public void removePostEventHandlers(Set<IEventHandler> p_handlers) {
        if (p_handlers != null) {
            for (IEventHandler tmpEventHandler : p_handlers) {
                removePostEventHandler(tmpEventHandler);
            }
        }
    }


    public IState addPreDefaultEventHandler(IEventHandler<Object> p_handler) {
        if (p_handler != null) {
            this.preDefaultEventHandlers.addHandler(p_handler);
        }

        return this;
    }


    public IState addDefaultEventHandler(IEventHandler<Object> p_handler) {
        if (p_handler != null) {
            this.defaultEventHandlers.addHandler(p_handler);
        }

        return this;
    }


    public IState addPostDefaultEventHandler(IEventHandler<Object> p_handler) {
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


    public IState addPreUnhandledHandler(IEventHandler<Object> p_handler) {
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
    public IState addUnhandledEventHandler(IEventHandler<Object> p_handler) {
        if (p_handler != null) {
            this.unknownEventHandlers.addHandler(p_handler);
        }

        return this;
    }


    public IState addPostUnhandledEventHandler(IEventHandler<Object> p_handler) {
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


    public Set<String> getSupportedEvents() {
        return null;
    }


    public boolean preOnReceive(Object p_event) throws Exception {
        boolean eventHandled = this.applyEventToHandlers(p_event,
                this.preEventHandlers,
                this.preDefaultEventHandlers,
                this.preUnknownEventHandlers);

        return eventHandled;
    }


    public boolean postOnReceive(Object p_event) throws Exception {
        boolean eventHandled = this.applyEventToHandlers(p_event,
                this.postEventHandlers,
                this.postDefaultEventHandlers,
                this.postUnknownEventHandlers);

        return eventHandled;
    }


    @Override
    public void init() {
    }


    private boolean applyEventToHandlers(Object p_event,
                                         IEventHandlerManager p_mainHanders,
                                         IEventHandlerManager p_defaultHandlers,
                                         IEventHandlerManager p_unknownHandlers) throws Exception {

        if (this.currentEvent != null) {
            throw new InvalidStateOpException(this.getClass().getName(), null);
        }

        boolean result = p_mainHanders.applyEvent(this, p_event);

        result = result || p_defaultHandlers.applyEvent(this, p_event);

        if (!result) {
            result = p_unknownHandlers.applyEvent(this, p_event);
        }

        return result;
    }
}

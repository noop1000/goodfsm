package org.noop.goodfsm.fsm;


import org.noop.goodfsm.fsm.scheduler.IScheduler;
import org.noop.goodfsm.fsm.scheduler.ISchedulerAware;
import org.noop.processing.AbstractEventProcessor;
import org.noop.processing.IEventHandler;
import org.noop.processing.IEventProcessor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Constructor;
import java.util.ArrayDeque;
import java.util.Queue;
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
public abstract class AbstractFsm
        implements IFsm, IEventProcessor, ISchedulerAware {
    private final Logger logger = LoggerFactory.getLogger(AbstractFsm.class);


    private final AbstractEventProcessor abstractEventProcessor = createAbstractEventProcessor();
    private final Queue eventsToProcess = new ArrayDeque<>();
    private final StateListenerMgr listenerManager = new StateListenerMgr();
    private IState currentState = null;
    private Class<? extends IState> pendingState = null;
    private Object pendingReason = null;


    public AbstractFsm() {
        super();
        this.listenerManager.addStateListener(this);
    }


    @Override
    public void onReceive(Object p_event) throws Exception {
        this.eventsToProcess.offer(p_event);

        if (getCurrentEvent() == null) {
            Object Event = this.eventsToProcess.remove();

            while (Event != null) {
                handleCurrentEvent(Event);
                Event = this.eventsToProcess.poll();
            }

            postReceiveCleanup();
        }
    }


    protected void handleCurrentEvent(Object p_event) throws Exception {
        getAbstractEventProcessor().setCurrentEvent(p_event);

        try {
            getAbstractEventProcessor().firePreEventHandlers(p_event);

            if (this.currentState != null) {
                prepareCurrentStateForNotify();
                this.currentState.preOnReceive(p_event);
                this.currentState.onReceive(p_event);
                postCurrentStateNotify();
            }

            getAbstractEventProcessor().onReceive(p_event);

        } finally {
            processPendingState();

            if (this.currentState != null) {
                this.currentState.setCurrentEvent(null);
                this.currentState.postOnReceive(p_event);
            }

            getAbstractEventProcessor().firePostEventHandlers(p_event);
            getAbstractEventProcessor().setCurrentEvent(null);


        }

    }


    protected void prepareCurrentStateForNotify() {
        // do nothing.
    }


    protected void postCurrentStateNotify() {
    }


    protected void postReceiveCleanup() {
    }


    protected void processPendingState() throws Exception {
        while(this.pendingState != null) {

            IState OldState = this.currentState;

            if (OldState != null) {
                this.listenerManager.fireStateExit(OldState);
                this.listenerManager.removeStateListener(OldState);
            }

            IState NewState = initState(this.pendingState);
            NewState.setReason(this.pendingReason);
            NewState.setPrevState(OldState.getClass());
            NewState.setScheduler(getScheduler());
            NewState.setCurrentEvent(getCurrentEvent());

            this.currentState = NewState;
            this.listenerManager.addStateListener(this.currentState);
            this.pendingState = null;
            this.pendingReason = null;
            this.listenerManager.fireStateEnter(OldState, this.currentState);
            OldState.setCurrentEvent(null);
            cleanupOldCurrentState(OldState);

        }
    }


    protected void cleanupOldCurrentState(IState p_State) {

    }


    @Override
    public IState getCurrentState() {
        return this.currentState;  //To change body of implemented methods use File | Settings | File Templates.
    }


    @Override
    public IState initFsm(Class<? extends IState> p_newState) throws Exception {
        if (this.currentState == null) {
            this.currentState = initState(p_newState);
            this.listenerManager.addStateListener(this.currentState);

        }

        return this.currentState;
    }


    protected IState initState(Class<? extends IState> p_newState) throws Exception {
        IState result = null;

        Class<?> DeclareClass = p_newState.getDeclaringClass();

        if (DeclareClass != null) {
            Constructor<?> Cstr = p_newState.getDeclaredConstructor(DeclareClass);
            result = (IState) Cstr.newInstance(this);
        } else {
            result = p_newState.newInstance();
        }

        result.setFsm(this);
        result.init();

        return result;
    }


    @Override
    public void transitionTo(Class<? extends IState> p_newState) throws Exception {
        if (this.pendingState == null) {
            this.pendingState = p_newState;
            this.pendingReason = getCurrentEvent();
        } else {
            throw new MultiplePendingStateTransitionException(this.pendingState.getName(),
                    p_newState.getName(),
                    this.getFsmName());
        }
    }


    @Override
    public void onStateEnter(IState p_oldState,
                             IState p_newState) {
    }


    @Override
    public void onStateExit(IState p_State) {
    }


    @Override
    public String getFsmName() {
        return this.getClass().getName();
    }


    @Override
    public boolean removeStateListener(IStateListener p_listener) {
        return this.listenerManager.removeStateListener(p_listener);
    }


    @Override
    public boolean addStateListener(IStateListener p_listener) {
        return this.listenerManager.addStateListener(p_listener);
    }


    @Override
    public void clearAllStateListeners() {
        this.listenerManager.clearAll();
    }


    @Override
    public Set<Class> getRegisteredEventTypes() {

        Set<Class> result = getAbstractEventProcessor().getRegisteredEventTypes();

        if (this.currentState != null) {
            Set<Class> curStateEventTypes = this.currentState.getRegisteredEventTypes();
            if (curStateEventTypes != null) {
                result.addAll(curStateEventTypes);
            }
        }

        return result;
    }


    @Override
    public Object getCurrentEvent() {
        return getAbstractEventProcessor().getCurrentEvent();
    }


    @Override
    public IScheduler getScheduler() {
        return getAbstractEventProcessor().getScheduler();
    }


    @Override
    public void setScheduler(IScheduler p_Scheduler) {
        getAbstractEventProcessor().setScheduler(p_Scheduler);
    }


    @Override
    public void addEventHandler(IEventHandler p_handler) {
        getAbstractEventProcessor().addEventHandler(p_handler);
    }


    @Override
    public void removeEventHandler(IEventHandler p_handler) {
        getAbstractEventProcessor().removeEventHandler(p_handler);
    }


    protected AbstractEventProcessor createAbstractEventProcessor() {
        AbstractEventProcessor result = new AbstractEventProcessor(this) {
        };
        result.setPreEventHandlersEnabled(false);
        result.setPostEventHandlersEnabled(false);
        return result;
    }


    protected AbstractEventProcessor getAbstractEventProcessor() {
        return this.abstractEventProcessor;
    }


    @Override
    public void addPreEventHandler(IEventHandler p_handler) {
        getAbstractEventProcessor().addPreEventHandler(p_handler);
    }


    @Override
    public void removePreEventHandler(IEventHandler p_handler) {
        getAbstractEventProcessor().removePreEventHandler(p_handler);
    }


    @Override
    public void addPostEventHandler(IEventHandler p_handler) {
        getAbstractEventProcessor().addPostEventHandler(p_handler);
    }


    @Override
    public void removePostEventHandler(IEventHandler p_handler) {
        getAbstractEventProcessor().removePostEventHandler(p_handler);
    }


    @Override
    public void addPreEventHandlers(Set<IEventHandler> p_handlers) {
        getAbstractEventProcessor().addPreEventHandlers(p_handlers);
    }


    @Override
    public void addEventHandlers(Set<IEventHandler> p_handlers) {
        getAbstractEventProcessor().addEventHandlers(p_handlers);
    }


    @Override
    public void addPostEventHandlers(Set<IEventHandler> p_handlers) {
        getAbstractEventProcessor().addPostEventHandlers(p_handlers);
    }


    @Override
    public void removePreEventHandlers(Set<IEventHandler> p_handlers) {
        getAbstractEventProcessor().removePreEventHandlers(p_handlers);
    }


    @Override
    public void removeEventHandlers(Set<IEventHandler> p_handlers) {
        getAbstractEventProcessor().removeEventHandlers(p_handlers);
    }


    @Override
    public void removePostEventHandlers(Set<IEventHandler> p_handlers) {
        getAbstractEventProcessor().removePostEventHandlers(p_handlers);
    }


    @Override
    public void addPreDefaultEventHandler(IEventHandler<Object> p_handler) {
        getAbstractEventProcessor().addPreDefaultEventHandler(p_handler);
    }


    @Override
    public void addDefaultEventHandler(IEventHandler<Object> p_handler) {
        getAbstractEventProcessor().addDefaultEventHandler(p_handler);
    }


    @Override
    public void addPostDefaultEventHandler(IEventHandler<Object> p_handler) {
        getAbstractEventProcessor().addPostDefaultEventHandler(p_handler);
    }


    @Override
    public void removeDefaultEventHandler(IEventHandler<Object> p_handler) {
        getAbstractEventProcessor().removeDefaultEventHandler(p_handler);
    }


    @Override
    public void removePreDefaultEventHandler(IEventHandler<Object> p_handler) {
        getAbstractEventProcessor().removePreDefaultEventHandler(p_handler);
    }


    @Override
    public void removePostDefaultEventHandler(IEventHandler<Object> p_handler) {
        getAbstractEventProcessor().removePostDefaultEventHandler(p_handler);
    }


    @Override
    public void addPreUnhandledHandler(IEventHandler<Object> p_handler) {
        getAbstractEventProcessor().addPreUnhandledHandler(p_handler);
    }


    @Override
    public void addUnhandledEventHandler(IEventHandler<Object> p_handler) {
        getAbstractEventProcessor().addUnhandledEventHandler(p_handler);

    }


    @Override
    public void addPostUnhandledEventHandler(IEventHandler<Object> p_handler) {
        getAbstractEventProcessor().addPostUnhandledEventHandler(p_handler);

    }


    @Override
    public void removeUnhandledEventHandler(IEventHandler<Object> p_handler) {
        getAbstractEventProcessor().removeUnhandledEventHandler(p_handler);

    }


    @Override
    public void removePreUnhandledEventHandler(IEventHandler<Object> p_handler) {
        getAbstractEventProcessor().removePreUnhandledEventHandler(p_handler);
    }


    @Override
    public void removePostUnhandledEventHandler(IEventHandler<Object> p_handler) {
        getAbstractEventProcessor().removePostUnhandledEventHandler(p_handler);
    }


    @Override
    public void init() {
    }
}

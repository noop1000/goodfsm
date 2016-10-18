package org.noop.goodfsm.fsm;


import org.noop.goodfsm.fsm.scheduler.IScheduler;
import org.noop.processing.AbstractEventProcessor;
import org.noop.processing.IEventHandler;
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
        implements IFsm {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());


    private final AbstractEventProcessor abstractEventProcessor = createAbstractEventProcessor();
    private final Queue eventsToProcess = new ArrayDeque<>();
    private final StateEnterListenerMgr stateEnterListenerMgr = new StateEnterListenerMgr();
    private final StateExitListenerMgr stateExitListenerMgr = new StateExitListenerMgr();
    private IState currentState = null;
    private Class<? extends IState> pendingState = null;
    private Object pendingReason = null;


    public AbstractFsm() {
        super();
        this.stateExitListenerMgr.addStateListener(this);
        this.stateEnterListenerMgr.addStateListener(this);
    }


    @Override
    public boolean onReceive(Object p_event) throws Exception {
        this.eventsToProcess.offer(p_event);

        if (getCurrentEvent() == null) {
            Object Event = this.eventsToProcess.remove();

            while (Event != null) {
                handleCurrentEvent(Event);
                Event = this.eventsToProcess.poll();
            }

            postReceiveCleanup();
        }

        return true;
    }


    protected boolean handleCurrentEvent(Object p_event) throws Exception {
        getAbstractEventProcessor().setCurrentEvent(p_event);

        boolean handledPre = false;
        boolean handled = false;
        boolean handledPost = false;

        try {
            getAbstractEventProcessor().firePreEventHandlers(p_event);

            if (this.currentState != null) {
                prepareCurrentStateForNotify();
                handledPre = this.currentState.preOnReceive(p_event);
                handled = this.currentState.onReceive(p_event);
                postCurrentStateNotify();
            }

            handled = handled || getAbstractEventProcessor().onReceive(p_event);

            if (!handled) {
                this.handleUnknownEvent(p_event);
            }

        } finally {
            processPendingState();

            if (this.currentState != null) {
                this.currentState.setCurrentEvent(null);
                handledPost= this.currentState.postOnReceive(p_event);
            }

            getAbstractEventProcessor().firePostEventHandlers(p_event);
            getAbstractEventProcessor().setCurrentEvent(null);

        }

        return handled;
    }

    @Override
    public boolean handleUnknownEvent(Object p_event) throws Exception {

        boolean handled = false;

        if (this.getCurrentState() != null) {
           handled = this.getCurrentState().handleUnknownEvent(p_event);
        }

        return (handled || getAbstractEventProcessor().handleUnknownEvent(p_event));
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
                this.stateExitListenerMgr.fireStateExit(OldState);
                this.stateExitListenerMgr.removeStateListener(OldState);
            }

            IState NewState = initState(this.pendingState);
            NewState.setReason(this.pendingReason);
            NewState.setPrevState(OldState.getClass());
            NewState.setScheduler(getScheduler());
            NewState.setCurrentEvent(getCurrentEvent());

            this.currentState = NewState;
            this.stateExitListenerMgr.addStateListener(this.currentState);
            this.pendingState = null;
            this.pendingReason = null;
            this.stateExitListenerMgr.fireStateExit(OldState);
            this.stateEnterListenerMgr.fireStateEnter(OldState, this.currentState);
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
    public IFsm initFsm(Class<? extends IState> p_newState) throws Exception {
        if (this.currentState == null) {
            this.currentState = initState(p_newState);
            this.stateExitListenerMgr.addStateListener(this.currentState);

        }

        return this;
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
    public IFsm transitionTo(Class<? extends IState> p_newState) throws Exception {
        if (this.pendingState == null) {
            this.pendingState = p_newState;
            this.pendingReason = getCurrentEvent();
        } else {
            throw new MultiplePendingStateTransitionException(this.pendingState.getName(),
                    p_newState.getName(),
                    this.getFsmName());
        }

        return this;
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
    public boolean removeStateListener(IStateExitListener p_listener) {
        return this.stateExitListenerMgr.removeStateListener(p_listener);
    }


    @Override
    public IFsm addStateEnterListener(IStateEnterListener p_listener) {
        this.stateEnterListenerMgr.addStateListener(p_listener);
        return this;
    }

    @Override
    public IFsm addStateExitListener(IStateExitListener p_listener) {
        this.stateExitListenerMgr.addStateListener(p_listener);
        return this;
    }


    @Override
    public void clearAllStateListeners() {
        this.stateExitListenerMgr.clearAll();
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
        return this.getAbstractEventProcessor().getCurrentEvent();
    }


    @Override
    public IScheduler getScheduler() {
        return this.getAbstractEventProcessor().getScheduler();
    }


    @Override
    public IFsm setScheduler(IScheduler p_Scheduler) {
        this.getAbstractEventProcessor().setScheduler(p_Scheduler);
        return this;
    }


    @Override
    public IFsm addEventHandler(IEventHandler p_handler) {
        this.getAbstractEventProcessor().addEventHandler(p_handler);
        return this;
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
    public IFsm addPreEventHandler(IEventHandler p_handler) {
        getAbstractEventProcessor().addPreEventHandler(p_handler);
        return this;
    }


    @Override
    public void removePreEventHandler(IEventHandler p_handler) {
        getAbstractEventProcessor().removePreEventHandler(p_handler);
    }


    @Override
    public IFsm addPostEventHandler(IEventHandler p_handler) {
        getAbstractEventProcessor().addPostEventHandler(p_handler);
        return this;
    }


    @Override
    public void removePostEventHandler(IEventHandler p_handler) {
        getAbstractEventProcessor().removePostEventHandler(p_handler);
    }


    @Override
    public IFsm addPreEventHandlers(Set<IEventHandler> p_handlers) {
        getAbstractEventProcessor().addPreEventHandlers(p_handlers);
        return this;
    }


    @Override
    public IFsm addEventHandlers(Set<IEventHandler> p_handlers) {
        this.getAbstractEventProcessor().addEventHandlers(p_handlers);
        return this;
    }


    @Override
    public IFsm addPostEventHandlers(Set<IEventHandler> p_handlers) {
        this.getAbstractEventProcessor().addPostEventHandlers(p_handlers);
        return this;
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
    public IFsm addPreDefaultEventHandler(IEventHandler<Object> p_handler) {
        this.getAbstractEventProcessor().addPreDefaultEventHandler(p_handler);
        return this;
    }


    @Override
    public IFsm addDefaultEventHandler(IEventHandler<Object> p_handler) {
        this.getAbstractEventProcessor().addDefaultEventHandler(p_handler);
        return this;
    }


    @Override
    public IFsm addPostDefaultEventHandler(IEventHandler<Object> p_handler) {
        this.getAbstractEventProcessor().addPostDefaultEventHandler(p_handler);
        return this;
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
    public IFsm addPreUnhandledHandler(IEventHandler<Object> p_handler) {
        getAbstractEventProcessor().addPreUnhandledHandler(p_handler);
        return this;
    }


    @Override
    public IFsm addUnhandledEventHandler(IEventHandler<Object> p_handler) {
        getAbstractEventProcessor().addUnhandledEventHandler(p_handler);
        return this;

    }


    @Override
    public IFsm addPostUnhandledEventHandler(IEventHandler<Object> p_handler) {
        getAbstractEventProcessor().addPostUnhandledEventHandler(p_handler);
        return this;

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

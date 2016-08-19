package org.noop.goodfsm.fsm;


import org.noop.goodfsm.fsm.scheduler.IScheduler;
import org.noop.goodfsm.fsm.scheduler.ISchedulerAware;
import org.noop.processing.IEventProcessor;

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
public interface IFsm
        extends IEventProcessor, IStateListener, ISchedulerAware {


    public IState getCurrentState();


    /**
     * returns the old state.
     * This will set the state NOW, and will NOT fire events etc
     *
     * @return
     */
    public IState initFsm(Class<? extends IState> p_newState) throws Exception;


    /**
     * This will transition to the given state.  This should only be called once per handled
     * message.  If called more than once for a event in a given state, it will throw an exception.
     * The transition will not happen until all registered handlers for the current message have been
     * processed.
     * <p/>
     * The reason will be the currently goodfsm Event.
     *
     * @return
     */
    public void transitionTo(Class<? extends IState> p_newState) throws Exception;


    public Object getCurrentEvent();


    public String getFsmName();


    public boolean removeStateListener(IStateListener p_listener);


    public boolean addStateListener(IStateListener p_listener);


    public void clearAllStateListeners();


    public IScheduler getScheduler();


    public void setScheduler(IScheduler p_Scheduler);


}

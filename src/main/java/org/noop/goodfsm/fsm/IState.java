package org.noop.goodfsm.fsm;


import org.noop.goodfsm.fsm.scheduler.ISchedulerAware;
import org.noop.processing.IEventProcessor;

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
public interface IState
        extends IEventProcessor, IStateListener, ISchedulerAware {

    IFsm getFsm();


    void setFsm(IFsm p_fsm);


    UUID getId();


    Class<? extends IState> getPrevState();


    void setPrevState(Class<? extends IState> p_PrevState);


    Object getReason();


    void setReason(Object p_Reason);


    void setTimeout(long p_TimeMillisec, Class<? extends IState> p_newState);


    long getEnterTime();


    boolean canProcess(Object p_event);


    void transitionTo(Class<? extends IState> p_newState) throws Exception;


    void setCurrentEvent(Object p_event);


    void registerTransition(Class p_event, Class<? extends IState> p_newState);


    void init();

    String calcTimeoutScheduleId();


    boolean preOnReceive(Object p_event) throws Exception;


    boolean postOnReceive(Object p_event) throws Exception;


}

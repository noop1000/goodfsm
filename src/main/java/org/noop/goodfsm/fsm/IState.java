package org.noop.goodfsm.fsm;


import org.noop.processing.IEventProcessor;
import org.noop.goodfsm.fsm.scheduler.ISchedulerAware;

import java.util.UUID;


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

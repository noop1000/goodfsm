package org.noop.goodfsm.fsm;


import org.noop.processing.IEventProcessor;
import org.noop.goodfsm.fsm.scheduler.IScheduler;
import org.noop.goodfsm.fsm.scheduler.ISchedulerAware;


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

package org.noop.goodfsm.fsm.scheduler;


/**
 * Marks a class as being aware of the scheduler (and wants it injected)
 */
public interface ISchedulerAware {

    IScheduler getScheduler();


    void setScheduler(IScheduler p_Scheduler);

}

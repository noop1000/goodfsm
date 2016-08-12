package org.noop.goodfsm.fsm.scheduler;

public interface IScheduler {
    /**
     * Fires the given event after a delay (in millisec) to itself
     *
     * @param p_Delay
     * @param p_event
     * @return
     */
    ICancellable scheduleOnce(long p_Delay, String p_ScheduleId, Object p_event);


    /**
     * schedule the sending of a given message at the given interval, after an initl delay.
     *
     * @param p_Delay
     * @param p_Interval
     * @param p_event
     * @return
     */
    ICancellable schedule(long p_Delay, long p_Interval, String p_ScheduleId, Object p_event);


    /**
     * @param p_ScheduleId
     */
    void cancelScheduledItem(String p_ScheduleId);


}

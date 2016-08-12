package org.noop.goodfsm.fsm.scheduler;


public interface ICancellable {
    void cancel();


    boolean isCancelled();


    String getScheduleId();

}

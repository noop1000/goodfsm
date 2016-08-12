package org.noop.goodfsm.events.common;


import java.io.Serializable;
import java.util.UUID;

/**
 * When a state was set a timeout, the timer will send this event
 * to the state to tell it has timed out.
 */
public class EventStateTimeout implements Serializable {
    private final UUID stateId;

    private final String scheduleId;


    public EventStateTimeout(String p_ScheduleId, UUID p_StateId) {
        this.scheduleId = p_ScheduleId;
        this.stateId = p_StateId;
    }


    public String getScheduleId() {
        return this.scheduleId;
    }


    public UUID getStateId() {
        return this.stateId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        EventStateTimeout that = (EventStateTimeout) o;

        if (stateId != null ? !stateId.equals(that.stateId) : that.stateId != null) return false;
        return scheduleId != null ? scheduleId.equals(that.scheduleId) : that.scheduleId == null;

    }

    @Override
    public int hashCode() {
        int result = stateId != null ? stateId.hashCode() : 0;
        result = 31 * result + (scheduleId != null ? scheduleId.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "EventStateTimeout{" +
                "stateId=" + stateId +
                ", scheduleId='" + scheduleId + '\'' +
                '}';
    }


}

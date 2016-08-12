package org.noop.goodfsm.fsm;


import org.noop.goodfsm.fsm.annotations.DontCheckState;


public class DontCheckStateAnnotationProcessor {

    public static boolean canIgnoreState(Object p_event) {
        boolean result = (p_event != null) && p_event.getClass().isAnnotationPresent(DontCheckState.class);

        return result;
    }

}

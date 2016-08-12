package org.noop.goodfsm.fsm.annotations;


import java.lang.annotation.*;

/**
 * When you annotate an event it says
 * Don't try to match an FSM's current state
 * to see if it corresponds to the state id
 * of the event.
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface DontCheckState {

}

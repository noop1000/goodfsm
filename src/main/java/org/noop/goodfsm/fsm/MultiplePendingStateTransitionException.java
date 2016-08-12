package org.noop.goodfsm.fsm;


public class MultiplePendingStateTransitionException
        extends Exception {
    private final String existingState;

    private final String attemptedState;

    private final String fsm;


    public MultiplePendingStateTransitionException(String p_existingState, String p_attemptedState, String p_fsm) {
        this.existingState = p_existingState;
        this.attemptedState = p_attemptedState;
        this.fsm = p_fsm;
    }


    @Override
    public String toString() {
        return "MultiplePendingStateTransitionException{" +
                "existingState='" + existingState + '\'' +
                ", attemptedState='" + attemptedState + '\'' +
                ", goodfsm='" + fsm + '\'' +
                '}';
    }
}

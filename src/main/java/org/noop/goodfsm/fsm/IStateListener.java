package org.noop.goodfsm.fsm;


public interface IStateListener {
    public void onStateEnter(IState p_oldState, IState p_newState);


    public void onStateExit(IState p_State);


}

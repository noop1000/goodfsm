package org.noop.goodfsm.fsm;


import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.Set;


public class StateListenerMgr {

    private final Set<IStateListener> listeners = new LinkedHashSet<>();


    public StateListenerMgr() {
        super();
    }


    public boolean addStateListener(IStateListener p_listener) {
        boolean result = false;

        if (p_listener != null) {
            result = this.listeners.add(p_listener);
        }

        return result;
    }


    public boolean removeStateListener(IStateListener p_listener) {
        boolean result = false;

        if (p_listener != null) {
            result = this.listeners.remove(p_listener);
        }

        return result;
    }


    public void clearAll() {
        this.listeners.clear();
    }


    public void fireStateExit(IState p_oldState) {
        // make a copy as listeners may be added/removed during

        Collection<IStateListener> Listeners = new ArrayList<>(this.listeners);

        for (IStateListener tmpListener : Listeners) {
            tmpListener.onStateExit(p_oldState);
        }

    }


    public void fireStateEnter(IState p_oldState, IState p_newState) {
        // make a copy as listeners may be added/removed during

        Collection<IStateListener> Listeners = new ArrayList<>(this.listeners);

        for (IStateListener tmpListener : Listeners) {
            tmpListener.onStateEnter(p_oldState, p_newState);
        }

    }


}

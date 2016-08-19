package org.noop.goodfsm.fsm;


import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.Set;

/**
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */
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

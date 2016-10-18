package org.noop.goodfsm.akka;

import org.noop.goodfsm.fsm.AbstractFsm;

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
public abstract class AbstractFsmWithStash<T> extends AbstractFsm<T> implements IFsmWithStash<T> {

    private IStashAware stashAdaptor;

    @Override
    public void stash() {
        if (stashAdaptor != null) {
            stashAdaptor.stash();
        }
    }

    @Override
    public void unstashAll() {
        if (stashAdaptor != null) {
            stashAdaptor.stash();
        }
    }

    @Override
    public void unstash() {
        if (stashAdaptor != null) {
            stashAdaptor.stash();
        }
    }

    @Override
    public void setStashAdaptor(IStashAware adaptor) {
        this.stashAdaptor = adaptor;
    }
}

package org.noop.goodfsm.fsm;


import org.noop.processing.AbstractActionHandler;
import org.noop.processing.IEventProcessor;

import java.util.function.BiConsumer;

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
public class LambdaEventHandler<T>
        extends AbstractActionHandler<T> {
    private final BiConsumer<IEventProcessor, T> Lambda;


    public LambdaEventHandler(Class p_Class, BiConsumer<IEventProcessor, T> p_eventLambda) {
        super(p_Class);
        this.Lambda = p_eventLambda;
    }


    @Override
    public void onReceive(IEventProcessor p_eventProcessor, T p_event) throws Exception {
        if (this.Lambda != null) {
            this.Lambda.accept(p_eventProcessor, p_event);
        }
    }
}

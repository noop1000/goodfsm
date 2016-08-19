package org.noop.processing;

import java.util.HashSet;
import java.util.Set;
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
public class HandlerUtils {


    public static Set<IEventHandler> buildMessageHandlers(final Set<? extends Class> p_MessageTypes,
                                                          final BiConsumer<IEventProcessor, Object> p_handler) {
        Set<IEventHandler> result = new HashSet<>();

        for (Class EventClass : p_MessageTypes) {
            IEventHandler<Object> tmpHandler = new AbstractActionHandler<Object>(EventClass) {
                @Override
                public void onReceive(IEventProcessor p_eventProcessor, Object p_event) throws Exception {
                    p_handler.accept(p_eventProcessor, p_event);
                }
            };

            result.add(tmpHandler);
        }

        return result;
    }


    public static Set<IEventHandler> buildMessageHandlers(final Set<? extends Class> p_MessageTypes,
                                                          final IEventHandler<Object> p_handler) {
        Set<IEventHandler> result = new HashSet<>();

        for (Class EventClass : p_MessageTypes) {
            IEventHandler<Object> tmpHandler = new AbstractActionHandler<Object>(EventClass) {
                @Override
                public void onReceive(IEventProcessor p_eventProcessor, Object p_event) throws Exception {
                    p_handler.onReceive(p_eventProcessor, p_event);
                }
            };

            result.add(tmpHandler);
        }

        return result;

    }

}

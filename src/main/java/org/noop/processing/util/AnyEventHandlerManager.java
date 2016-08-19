package org.noop.processing.util;


import org.noop.processing.IEventHandler;
import org.noop.processing.IEventProcessor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
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
public class AnyEventHandlerManager implements IEventHandlerManager {

    private final static Logger logger = LoggerFactory.getLogger(AnyEventHandlerManager.class);


    private final Set<IEventHandler> handlers = new HashSet<>();


    @Override
    public boolean applyEvent(final IEventProcessor p_eventProcessor, final Object p_event) throws Exception {
        boolean result = false;

        if (this.handlers.size() > 0) {
            result = true;

            List<IEventHandler> HandlersToProcess = new ArrayList<>(this.handlers);

            for (IEventHandler Handler : HandlersToProcess) {
                try {
                    Handler.onReceive(p_eventProcessor, p_event);
                } catch (Exception ex) {
                    logger.error("error in handler {}", ex);
                }
            }
        }

        return result;
    }


    @Override
    public void addHandler(IEventHandler p_handler) {
        if (p_handler != null) {
            this.handlers.add(p_handler);
        }
    }


    @Override
    public boolean removeHandler(IEventHandler p_handler) {
        boolean result = false;

        if (p_handler != null) {
            this.handlers.remove(p_handler);
        }

        return result;
    }


}

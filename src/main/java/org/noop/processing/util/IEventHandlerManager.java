package org.noop.processing.util;

import org.noop.processing.IEventHandler;
import org.noop.processing.IEventProcessor;


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
public interface IEventHandlerManager {


    /**
     * return true if handled.
     *
     * @param p_eventProcessor
     * @param p_event
     * @return
     * @throws Exception
     */
    boolean applyEvent(IEventProcessor p_eventProcessor, Object p_event) throws Exception;

    void addHandler(IEventHandler p_handler);

    boolean removeHandler(IEventHandler p_handler);

}

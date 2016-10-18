package org.noop.processing;


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
public interface IEventProcessor<T> {


    boolean onReceive(Object p_event) throws Exception;


    boolean handleUnknownEvent(Object p_event) throws Exception;


    T addPreEventHandler(IEventHandler p_handler);


    void removePreEventHandler(IEventHandler p_handler);


    T addEventHandler(IEventHandler p_handler);


    void removeEventHandler(IEventHandler p_handler);


    T addPostEventHandler(IEventHandler p_handler);


    void removePostEventHandler(IEventHandler p_handler);


    Object getCurrentEvent();


    Set<Class> getRegisteredEventTypes();


    T addPreEventHandlers(Set<IEventHandler> p_handlers);


    T addEventHandlers(Set<IEventHandler> p_handlers);


    T addPostEventHandlers(Set<IEventHandler> p_handlers);


    void removePreEventHandlers(Set<IEventHandler> p_handlers);


    void removeEventHandlers(Set<IEventHandler> p_handlers);


    void removePostEventHandlers(Set<IEventHandler> p_handlers);


    T addPreDefaultEventHandler(IEventHandler<Object> p_handler);


    T addDefaultEventHandler(IEventHandler<Object> p_handler);


    T addPostDefaultEventHandler(IEventHandler<Object> p_handler);


    void removeDefaultEventHandler(IEventHandler<Object> p_handler);


    void removePreDefaultEventHandler(IEventHandler<Object> p_handler);


    void removePostDefaultEventHandler(IEventHandler<Object> p_handler);


    T addPreUnhandledHandler(IEventHandler<Object> p_handler);


    T addUnhandledEventHandler(IEventHandler<Object> p_handler);


    T addPostUnhandledEventHandler(IEventHandler<Object> p_handler);


    void removeUnhandledEventHandler(IEventHandler<Object> p_handler);


    void removePreUnhandledEventHandler(IEventHandler<Object> p_handler);


    void removePostUnhandledEventHandler(IEventHandler<Object> p_handler);


    void init();

}

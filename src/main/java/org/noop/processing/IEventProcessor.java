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
public interface IEventProcessor {


    void onReceive(Object p_event) throws Exception;


    void addPreEventHandler(IEventHandler p_handler);


    void removePreEventHandler(IEventHandler p_handler);


    void addEventHandler(IEventHandler p_handler);


    void removeEventHandler(IEventHandler p_handler);


    void addPostEventHandler(IEventHandler p_handler);


    void removePostEventHandler(IEventHandler p_handler);


    Object getCurrentEvent();


    Set<Class> getRegisteredEventTypes();


    void addPreEventHandlers(Set<IEventHandler> p_handlers);


    void addEventHandlers(Set<IEventHandler> p_handlers);


    void addPostEventHandlers(Set<IEventHandler> p_handlers);


    void removePreEventHandlers(Set<IEventHandler> p_handlers);


    void removeEventHandlers(Set<IEventHandler> p_handlers);


    void removePostEventHandlers(Set<IEventHandler> p_handlers);


    void addPreDefaultEventHandler(IEventHandler<Object> p_handler);


    void addDefaultEventHandler(IEventHandler<Object> p_handler);


    void addPostDefaultEventHandler(IEventHandler<Object> p_handler);


    void removeDefaultEventHandler(IEventHandler<Object> p_handler);


    void removePreDefaultEventHandler(IEventHandler<Object> p_handler);


    void removePostDefaultEventHandler(IEventHandler<Object> p_handler);


    void addPreUnhandledHandler(IEventHandler<Object> p_handler);


    void addUnhandledEventHandler(IEventHandler<Object> p_handler);


    void addPostUnhandledEventHandler(IEventHandler<Object> p_handler);


    void removeUnhandledEventHandler(IEventHandler<Object> p_handler);


    void removePreUnhandledEventHandler(IEventHandler<Object> p_handler);


    void removePostUnhandledEventHandler(IEventHandler<Object> p_handler);


    void init();

}

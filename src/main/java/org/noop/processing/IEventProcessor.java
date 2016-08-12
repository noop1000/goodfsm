package org.noop.processing;


import java.util.Set;
import java.util.function.BiConsumer;


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


    Set<String> getSupportedEvents();


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

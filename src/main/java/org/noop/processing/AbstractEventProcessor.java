package org.noop.processing;


import org.noop.exceptions.NotImplementedException;
import org.noop.goodfsm.fsm.InvalidStateOpException;
import org.noop.goodfsm.fsm.scheduler.IScheduler;
import org.noop.goodfsm.fsm.scheduler.ISchedulerAware;
import org.noop.processing.util.AnyEventHandlerManager;
import org.noop.processing.util.EventHandlerManager;
import org.noop.processing.util.IEventHandlerManager;

import java.util.Set;


public abstract class AbstractEventProcessor
        implements IEventProcessor, ISchedulerAware {

    private EventHandlerManager eventHandlers = new EventHandlerManager();


    private EventHandlerManager preEventHandlers = new EventHandlerManager();


    private EventHandlerManager postEventHandlers = new EventHandlerManager();


    private AnyEventHandlerManager preDefaultEventHandlers = new AnyEventHandlerManager();

    private AnyEventHandlerManager defaultEventHandlers = new AnyEventHandlerManager();

    private AnyEventHandlerManager postDefaultEventHandlers = new AnyEventHandlerManager();


    private AnyEventHandlerManager preUnknownEventHandlers = new AnyEventHandlerManager();

    private AnyEventHandlerManager unknownEventHandlers = new AnyEventHandlerManager();

    private AnyEventHandlerManager postUnknownEventHandlers = new AnyEventHandlerManager();


    private Object currentEvent = null;


    private volatile IScheduler scheduler = null;


    private boolean postEventHandlersEnabled = true;


    private boolean preEventHandlersEnabled = true;


    private IEventProcessor container = this;


    public AbstractEventProcessor() {
        super();

    }


    public AbstractEventProcessor(IEventProcessor p_Container) {
        this();

        if (p_Container != null) {
            this.container = p_Container;
        }
    }


    @Override
    public void onReceive(Object p_event) throws Exception {
        setCurrentEvent(p_event);

        prepareHandlersForNotify(p_event);

        if (this.preEventHandlersEnabled) {
            firePreEventHandlers(p_event);
        }

        fireEventHandlers(p_event);

        if (this.postEventHandlersEnabled) {
            firePostEventHandlers(p_event);
        }

    }


    public boolean firePreEventHandlers(Object p_event) throws Exception {

        boolean eventHandled = this.applyEventToHandlers(p_event,
                this.preEventHandlers,
                this.preDefaultEventHandlers,
                this.preUnknownEventHandlers);

        return eventHandled;
    }


    public boolean firePostEventHandlers(Object p_event) throws Exception {
        boolean eventHandled = this.applyEventToHandlers(p_event,
                this.postEventHandlers,
                this.postDefaultEventHandlers,
                this.postUnknownEventHandlers);

        return eventHandled;

    }


    public boolean fireEventHandlers(Object p_event) throws Exception {

        boolean result = this.eventHandlers.applyEvent(getContainer(), p_event);

        result = result || this.defaultEventHandlers.applyEvent(getContainer(), p_event);

        if (!result) {
            result = this.unknownEventHandlers.applyEvent(getContainer(), p_event);
        }

        return result;


    }


    /**
     * override for custom prep here as part of the onReceive.
     */
    public void prepareHandlersForNotify(Object p_event) {
        // overwrite to do something.
    }


    @Override
    public void addPreEventHandler(IEventHandler p_handler) {
        if (p_handler != null) {
            this.preEventHandlers.addHandler(p_handler);
        }
    }


    @Override
    public void removePreEventHandler(IEventHandler p_handler) {
        if (p_handler != null) {
            this.preEventHandlers.removeHandler(p_handler);
        }
    }


    @Override
    public void addEventHandler(IEventHandler p_handler) {
        if (p_handler != null) {
            this.eventHandlers.addHandler(p_handler);
        }
    }


    @Override
    public void removeEventHandler(IEventHandler p_handler) {
        if (p_handler != null) {
            this.eventHandlers.removeHandler(p_handler);
        }
    }


    public IEventHandlerManager getEventHandlers() {
        return this.eventHandlers;
    }


    @Override
    public Object getCurrentEvent() {
        return this.currentEvent;
    }


    public void setCurrentEvent(Object p_event) {
        this.currentEvent = p_event;
    }


    @Override
    public Set<Class> getRegisteredEventTypes() {
        Set<Class> result = this.eventHandlers.getRegisteredEvents();

        return result;
    }


    @Override
    public IScheduler getScheduler() {
        return this.scheduler;
    }


    @Override
    public void setScheduler(IScheduler p_Scheduler) {
        this.scheduler = p_Scheduler;
    }


    @Override
    public void addPostEventHandler(IEventHandler p_handler) {
        if (p_handler != null) {
            this.postEventHandlers.addHandler(p_handler);
        }
    }


    @Override
    public void removePostEventHandler(IEventHandler p_handler) {
        if (p_handler != null) {
            this.postEventHandlers.addHandler(p_handler);
        }
    }


    protected IEventHandlerManager getPostEventHandlers() {
        return this.postEventHandlers;
    }


    public boolean isPostEventHandlersEnabled() {
        return this.postEventHandlersEnabled;
    }


    public void setPostEventHandlersEnabled(boolean p_postEventHandlersEnabled) {
        this.postEventHandlersEnabled = p_postEventHandlersEnabled;
    }


    public boolean isPreEventHandlersEnabled() {
        return this.preEventHandlersEnabled;
    }


    public void setPreEventHandlersEnabled(boolean p_preEventHandlersEnabled) {
        this.preEventHandlersEnabled = p_preEventHandlersEnabled;
    }


    @Override
    public void addPreEventHandlers(Set<IEventHandler> p_handlers) {
        if (p_handlers != null) {
            for (IEventHandler Handler : p_handlers) {
                addPreEventHandler(Handler);
            }
        }
    }


    @Override
    public void addEventHandlers(Set<IEventHandler> p_handlers) {
        if (p_handlers != null) {
            for (IEventHandler Handler : p_handlers) {
                addEventHandler(Handler);
            }
        }
    }


    @Override
    public void addPostEventHandlers(Set<IEventHandler> p_handlers) {
        if (p_handlers != null) {
            for (IEventHandler Handler : p_handlers) {
                addPostEventHandler(Handler);
            }
        }
    }


    @Override
    public void removePreEventHandlers(Set<IEventHandler> p_handlers) {
        if (p_handlers != null) {
            for (IEventHandler Handler : p_handlers) {
                removePreEventHandler(Handler);
            }
        }
    }


    @Override
    public void removeEventHandlers(Set<IEventHandler> p_handlers) {
        if (p_handlers != null) {
            for (IEventHandler Handler : p_handlers) {
                removeEventHandler(Handler);
            }
        }
    }


    @Override
    public void removePostEventHandlers(Set<IEventHandler> p_handlers) {
        if (p_handlers != null) {
            for (IEventHandler Handler : p_handlers) {
                removePostEventHandler(Handler);
            }
        }
    }


    @Override
    public Set<String> getSupportedEvents() {
        throw new NotImplementedException();
    }


    public void addPreDefaultEventHandler(IEventHandler<Object> p_handler) {
        if (p_handler != null) {
            this.preDefaultEventHandlers.addHandler(p_handler);
        }
    }


    /**
     * Register a message handler that is called for ALL messages.
     *
     * @param p_handler
     */
    public void addDefaultEventHandler(IEventHandler<Object> p_handler) {
        if (p_handler != null) {
            this.defaultEventHandlers.addHandler(p_handler);
        }
    }


    public void addPostDefaultEventHandler(IEventHandler<Object> p_handler) {
        if (p_handler != null) {
            this.postDefaultEventHandlers.addHandler(p_handler);
        }
    }


    public void removeDefaultEventHandler(IEventHandler<Object> p_handler) {
        if (p_handler != null) {
            this.defaultEventHandlers.removeHandler(p_handler);
        }
    }


    public void removePreDefaultEventHandler(IEventHandler<Object> p_handler) {
        if (p_handler != null) {
            this.preDefaultEventHandlers.removeHandler(p_handler);
        }
    }


    public void removePostDefaultEventHandler(IEventHandler<Object> p_handler) {
        if (p_handler != null) {
            this.postDefaultEventHandlers.removeHandler(p_handler);
        }
    }


    public void addPreUnhandledHandler(IEventHandler<Object> p_handler) {
        if (p_handler != null) {
            this.preUnknownEventHandlers.addHandler(p_handler);
        }
    }


    /**
     * If there is no message handler registered for a given message type or if the there is
     * no default handler (called for all messages) this handler will be called.  Good for logging or some
     * default behavior for messages that don't  have a specific handler registered.
     *
     * @param p_handler
     */
    public void addUnhandledEventHandler(IEventHandler<Object> p_handler) {
        if (p_handler != null) {
            this.unknownEventHandlers.addHandler(p_handler);
        }
    }


    public void addPostUnhandledEventHandler(IEventHandler<Object> p_handler) {
        if (p_handler != null) {
            this.postUnknownEventHandlers.addHandler(p_handler);
        }
    }


    public void removeUnhandledEventHandler(IEventHandler<Object> p_handler) {
        if (p_handler != null) {
            this.unknownEventHandlers.removeHandler(p_handler);
        }
    }


    public void removePreUnhandledEventHandler(IEventHandler<Object> p_handler) {
        if (p_handler != null) {
            this.preUnknownEventHandlers.removeHandler(p_handler);
        }
    }


    public void removePostUnhandledEventHandler(IEventHandler<Object> p_handler) {
        if (p_handler != null) {
            this.postUnknownEventHandlers.removeHandler(p_handler);
        }
    }


    @Override
    public void init() {

    }


    protected IEventProcessor getContainer() {
        return this.container;
    }


    private boolean applyEventToHandlers(Object p_event,
                                         IEventHandlerManager p_mainHanders,
                                         IEventHandlerManager p_defaultHandlers,
                                         IEventHandlerManager p_unknownHandlers) throws Exception {

        boolean result = p_mainHanders.applyEvent(this, p_event);

        result = result || p_defaultHandlers.applyEvent(this, p_event);

        if (!result) {
            result = p_unknownHandlers.applyEvent(this, p_event);
        }

        return result;
    }


}

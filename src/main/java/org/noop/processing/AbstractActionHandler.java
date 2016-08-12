package org.noop.processing;


public abstract class AbstractActionHandler<T>
        implements IEventHandler<T> {
    private final Class supportedAction;


    public AbstractActionHandler(Class p_SupportedClass) {
        this.supportedAction = p_SupportedClass;
    }


    public Class getSupportedAction() {
        return this.supportedAction;
    }

}

package org.noop.goodfsm.akka;

import org.noop.goodfsm.fsm.AbstractFsm;


public abstract class AbstractFsmWithStash extends AbstractFsm implements IFsmWithStash {

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

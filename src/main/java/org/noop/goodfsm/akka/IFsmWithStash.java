package org.noop.goodfsm.akka;

import org.noop.goodfsm.fsm.IFsm;

public interface IFsmWithStash extends IFsm, IStashAware{

    void setStashAdaptor(IStashAware adaptor);

}

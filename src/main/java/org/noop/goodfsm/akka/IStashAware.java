package org.noop.goodfsm.akka;

public interface IStashAware {

    void stash();

    void unstashAll();

    void unstash();
}

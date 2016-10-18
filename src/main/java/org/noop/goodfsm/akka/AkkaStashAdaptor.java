package org.noop.goodfsm.akka;


import akka.actor.Stash;

public class AkkaStashAdaptor implements IStashAware {

    private Stash actorWithStash;


    public AkkaStashAdaptor(Stash actorWithStash) {
        this.actorWithStash = actorWithStash;
    }

    @Override
    public void stash() {
        actorWithStash.stash();
    }

    @Override
    public void unstashAll() {
        actorWithStash.unstashAll();
    }

    @Override
    public void unstash() {
        actorWithStash.unstash();
    }
}

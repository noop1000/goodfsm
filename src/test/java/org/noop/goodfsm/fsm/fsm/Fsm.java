package org.noop.goodfsm.fsm.fsm;


import org.noop.goodfsm.fsm.*;
import org.noop.processing.AbstractActionHandler;
import org.noop.processing.IEventProcessor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Set;
import java.util.concurrent.ConcurrentSkipListSet;


public class Fsm
        extends AbstractFsm {
    public final static Logger logger = LoggerFactory.getLogger(Fsm.class);


    public Fsm() throws Exception {
        super();

    }

    @Override
    public void init() {
        try {
            initFsm(State1.class);
        } catch (Exception ex) {
            logger.error("init() - ", ex);
        }
    }

    @Override
    public Set<String> getSupportedEvents() {
        final Set<String> Events = new ConcurrentSkipListSet<>();
        Events.add(Action1.class.getSimpleName());
        Events.add(Action2.class.getSimpleName());
        Events.add(Action3.class.getSimpleName());
        Events.add(Action4.class.getSimpleName());

        return Events;
    }

    public class State1
            extends AbstractState {
        public final Logger logger = LoggerFactory.getLogger(State1.class);

        private int someVar = 0;


        public State1() {
            super();
        }

        @Override
        public void init() {
            // setup the event transitions

            registerTransition(Action1.class, State2.class);
            registerTransition(Action2.class, State3.class);
            registerTransition(Action3.class, State1.class);

            addEventHandler(new AbstractActionHandler<Action1>(Action1.class) {
                @Override
                public void onReceive(IEventProcessor p_EventProcessor, Action1 p_Event) throws Exception {
                    logger.info("State1 Action1 event handler for " + p_Event);
                }
            });

            setTimeout(5000, State4.class);
        }


        @Override
        public void onStateEnter(IState p_OldState,
                                 IState p_NewState) {
            logger.info("on state state enter " + p_NewState);

            this.someVar = 1;
            super.onStateEnter(p_OldState, p_NewState);
        }


        @Override
        public void onStateExit(IState p_State) {
            logger.info("State1 on state state exit " + p_State);

            super.onStateExit(p_State);
        }


    }

    public class State2
            extends AbstractState {


        public State2() {
            super();
        }

        @Override
        public void init() {
            addEventHandler(new AbstractActionHandler<Action2>(Action2.class) {
                @Override
                public void onReceive(IEventProcessor p_EventProcessor, Action2 p_Event) throws Exception {
                    transitionTo(State3.class);
                }
            });
        }

    }

    public class State3
            extends AbstractState {

        public State3() {
            super();
        }

        @Override
        public void init() {
            registerTransition(Action3.class, State1.class);
            registerTransition(Action2.class, State2.class);

        }

    }

    public class State4
            extends AbstractState {

        public State4() {
            super();
        }

        @Override
        public void init() {
        }
    }
}

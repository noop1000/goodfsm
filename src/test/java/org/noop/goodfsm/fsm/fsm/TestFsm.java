package org.noop.goodfsm.fsm.fsm;

import org.noop.goodfsm.fsm.annotations.DontCheckState;
import org.junit.Test;
import org.noop.goodfsm.fsm.*;
import org.noop.processing.AbstractActionHandler;
import org.noop.processing.IEventHandler;
import org.noop.processing.IEventProcessor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


@DontCheckState
public class TestFsm {

    public final static Logger logger = LoggerFactory.getLogger(TestFsm.class);

    public TestFsm() {
        super();
    }

    @Test
    public void testFsm1() throws Exception {

        final IEventHandler<Action1> Action1Handler = new AbstractActionHandler<Action1>(Action1.class) {
            @Override
            public void onReceive(IEventProcessor p_EventProcessor, Action1 p_Event) throws Exception {
                logger.info("testFsm1.Action1 event handler for " + p_Event);
            }
        };


        final IEventHandler<Action2> Action2Handler = new AbstractActionHandler<Action2>(Action2.class) {
            @Override
            public void onReceive(IEventProcessor p_EventProcessor, Action2 p_Event) throws Exception {
                logger.info("testFsm1.Action2 event handler for {}", p_Event);
            }
        };


        final IEventHandler<Action3> Action3Handler = new AbstractActionHandler<Action3>(Action3.class) {
            @Override
            public void onReceive(IEventProcessor p_EventProcessor, Action3 p_Event) throws Exception {
                logger.info("testFsm1.Action3 event handler for {}", p_Event);
            }
        };


        final IEventHandler<Action4> Action4Handler = new AbstractActionHandler<Action4>(Action4.class) {
            @Override
            public void onReceive(IEventProcessor p_EventProcessor, Action4 p_Event) throws Exception {
                logger.info("testFsm1.Action4 event handler for {}", p_Event);
            }
        };

        final IStateListener StateListener1 = new IStateListener() {
            @Override
            public void onStateEnter(IState p_OldState, IState p_NewState) {
                logger.info("testFsm1.stateListener1 - onStateEnter - current state {}", p_NewState.getFsm().getCurrentState());
                logger.info("testFsm1.StateListener1 - onStateEnter , old State {} , new State {}", p_OldState, p_NewState);
                logger.info("testFsm1.StateListener1 - reason {}", p_NewState.getReason());
            }

            @Override
            public void onStateExit(IState p_State) {
                logger.info("testFsm1.StateListener1 - onStateExit , state {}", p_State);
            }
        };


        final IStateListener StateListener2 = new IStateListener() {
            @Override
            public void onStateEnter(IState p_OldState, IState p_NewState) {
                logger.info("testFsm1.stateListener2 - onStateEnter - current state {}", p_NewState.getFsm().getCurrentState());
                logger.info("testFsm1.StateListener2 - onStateEnter , old State {}, new state {}", p_OldState, p_NewState);
                logger.info("testFsm1.StateListener2 - reason {}", p_NewState.getReason());

                try {
                    p_NewState.getFsm().onReceive(new Action4());
                } catch (Exception ex) {
                    logger.info("testFsm1 onReceive error {}", ex);
                }
            }

            @Override
            public void onStateExit(IState p_State) {
                logger.info("testFsm1.StateListener2 - onStateExit , state {}", p_State);
            }
        };

        final IFsm Fsm1 = new Fsm();
        Fsm1.init();

        Fsm1.addEventHandler(Action1Handler);
        Fsm1.addEventHandler(Action2Handler);
        Fsm1.addEventHandler(Action3Handler);
        Fsm1.addEventHandler(Action4Handler);

        Fsm1.addStateListener(StateListener1);
        Fsm1.addStateListener(StateListener2);

        Fsm1.addStateListener(new AbstractStateListener() {
            @Override
            public void onStateEnter(IState p_OldState, IState p_NewState) {
                if (p_NewState instanceof Fsm.State4) {
                    logger.info("lamba state listener, old state {}, new state {}", p_OldState, p_NewState);
                }
            }
        });

        Fsm1.onReceive(new Action1());
        Fsm1.onReceive(new Action2());
        Fsm1.onReceive(new Action3());

    }

}

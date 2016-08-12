package org.noop.goodfsm.fsm.annotations;


import org.noop.goodfsm.fsm.Action1;
import org.noop.goodfsm.fsm.Action2;
import org.noop.goodfsm.fsm.Action3;
import org.noop.goodfsm.fsm.DontCheckStateAnnotationProcessor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.junit.Assert;
import org.junit.Test;


public class TestDontCheckStateAnnotation {

    public final static Logger logger = LoggerFactory.getLogger(TestDontCheckStateAnnotation.class);


    @Test
    public void test1() {
        boolean Ignore1 = DontCheckStateAnnotationProcessor.canIgnoreState(new Action1());
        logger.info("ignore 1 is " + Ignore1);

        boolean Ignore2 = DontCheckStateAnnotationProcessor.canIgnoreState(new Action2());
        logger.info("ignore 2 is " + Ignore2);

        boolean Ignore3 = DontCheckStateAnnotationProcessor.canIgnoreState(new Action3());
        logger.info("ignore 3 is " + Ignore3);

        Assert.assertTrue(Ignore1);
        Assert.assertTrue(!Ignore2);
        Assert.assertTrue(!Ignore3);
    }

}

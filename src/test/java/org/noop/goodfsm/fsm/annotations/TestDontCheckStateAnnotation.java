package org.noop.goodfsm.fsm.annotations;


import org.noop.goodfsm.fsm.Action1;
import org.noop.goodfsm.fsm.Action2;
import org.noop.goodfsm.fsm.Action3;
import org.noop.goodfsm.fsm.DontCheckStateAnnotationProcessor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.junit.Assert;
import org.junit.Test;


/**
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */
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

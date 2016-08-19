package org.noop.goodfsm.akka;


import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.Cancellable;
import org.noop.goodfsm.fsm.scheduler.ICancellable;
import org.noop.goodfsm.fsm.scheduler.IScheduler;
import scala.concurrent.duration.Duration;

import java.util.concurrent.TimeUnit;

/**
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */
public class AkkaSchedulerAdaptor implements IScheduler {

    private final ActorSystem system;
    private final ActorRef actor;

    public AkkaSchedulerAdaptor(ActorSystem p_actorSystem, ActorRef p_actor) {
        this.system = p_actorSystem;
        this.actor = p_actor;
    }

    @Override
    public ICancellable scheduleOnce(long p_Delay, String p_ScheduleId, Object p_event) {
        final Cancellable Cancellable = this.system.scheduler().scheduleOnce(Duration.create(p_Delay, TimeUnit.MILLISECONDS),
                this.actor,
                p_event,
                this.system.dispatcher(),
                null);


        return new AkkaCancellable(p_ScheduleId, Cancellable);

    }

    @Override
    public ICancellable schedule(long p_Delay, long p_Interval, String p_ScheduleId, Object p_event) {
        final Cancellable Cancellable = this.system.scheduler().schedule(Duration.create(p_Delay, TimeUnit.MILLISECONDS),
                Duration.create(p_Interval, TimeUnit.MILLISECONDS),
                this.actor,
                p_event,
                this.system.dispatcher(),
                null);

        return new AkkaCancellable(p_ScheduleId, Cancellable);

    }

    @Override
    public void cancelScheduledItem(String p_ScheduleId) {

    }

    public static class AkkaCancellable implements ICancellable {
        final String scheduleId;

        final Cancellable cancellable;


        public AkkaCancellable(String p_ScheduleId, Cancellable p_Cancellable) {
            this.scheduleId = p_ScheduleId;
            this.cancellable = p_Cancellable;
        }

        @Override
        public void cancel() {
            this.cancellable.cancel();
        }


        @Override
        public boolean isCancelled() {
            return this.cancellable.isCancelled();
        }


        @Override
        public String getScheduleId() {
            return this.scheduleId;
        }

    }
}

package org.noop.goodfsm.fsm.scheduler;

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
public interface IScheduler {
    /**
     * Fires the given event after a delay (in millisec) to itself
     *
     * @param p_Delay
     * @param p_event
     * @return
     */
    ICancellable scheduleOnce(long p_Delay, String p_ScheduleId, Object p_event);


    /**
     * schedule the sending of a given message at the given interval, after an initl delay.
     *
     * @param p_Delay
     * @param p_Interval
     * @param p_event
     * @return
     */
    ICancellable schedule(long p_Delay, long p_Interval, String p_ScheduleId, Object p_event);


    /**
     * @param p_ScheduleId
     */
    void cancelScheduledItem(String p_ScheduleId);


}

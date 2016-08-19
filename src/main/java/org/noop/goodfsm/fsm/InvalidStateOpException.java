package org.noop.goodfsm.fsm;

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
public class InvalidStateOpException
        extends RuntimeException {
    final private String stateName;


    public InvalidStateOpException(String p_StateName, Exception p_Reason) {
        super(p_Reason);
        this.stateName = p_StateName;

    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        InvalidStateOpException that = (InvalidStateOpException) o;

        return stateName != null ? stateName.equals(that.stateName) : that.stateName == null;

    }

    @Override
    public int hashCode() {
        return stateName != null ? stateName.hashCode() : 0;
    }


    @Override
    public String toString() {
        return "InvalidStateOpException{" +
                "stateName='" + stateName + '\'' +
                '}';
    }
}

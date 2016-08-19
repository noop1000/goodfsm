package org.noop.processing.util;


import org.noop.processing.IEventProcessor;

import java.lang.reflect.Constructor;


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
public class ProcessorUtil {


    public static IEventProcessor createProcessor(Class p_eventProcessorClass, Object... p_args) throws Exception {
        IEventProcessor result = null;

        Class[] argsTypes = new Class[p_args.length];

        int i = 0;
        for (Object Arg : p_args) {
            argsTypes[i] = Arg.getClass();
            i++;
        }

        if (argsTypes.length > 0) {
            Constructor FoundConstructor = null;

            FoundConstructor = p_eventProcessorClass.getConstructor(argsTypes);

            result = (IEventProcessor) FoundConstructor.newInstance(p_args);

        } else {
            result = (IEventProcessor) p_eventProcessorClass.newInstance();
        }

        return result;
    }
}

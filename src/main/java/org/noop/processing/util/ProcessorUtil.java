package org.noop.processing.util;


import org.noop.processing.IEventProcessor;

import java.lang.reflect.Constructor;


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

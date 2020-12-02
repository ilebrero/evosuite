package org.evosuite.symbolic.vm.instrument;

import org.evosuite.Properties;

import java.lang.instrument.Instrumentation;

/**
 * You have to tell the JVM that it should use this agent class:
 * <pre>java -javaagent:JAR_PATH CLASS</pre>
 * <p>
 * Example:
 * java -javaagent:instrument.jar MyClass
 *
 * <p>This class satisfies the JVM's agent convention, i.e., it has a
 * public static void premain(..) method.</p>
 *
 * <p>The main instrumentation class is {@link DscMethodVisitor}</p>
 * <p>
 * TODO: java.lang.instrument.Instrumentation.retransformClasses(Class[])
 * on the system classes we would like to instrument, i.e., java.util.HashMap
 *
 * @author csallner@uta.edu (Christoph Csallner)
 * @see http://java.sun.com/javase/6/docs/api/java/lang/instrument/package-summary.html
 */
public final class JvmAgent {

    /**
     * @param agentArgs
     */
    public static void agentMain(String agentArgs, Instrumentation instrumentation) {
        if (Properties.ENABLE_LAMBDA_INSTRUMENTING_AGENT && Properties.isDSEStrategySelected()) {
            instrumentation.addTransformer(new EvosuiteLambdaClassFileTransformer(), false);
        }
    }

}
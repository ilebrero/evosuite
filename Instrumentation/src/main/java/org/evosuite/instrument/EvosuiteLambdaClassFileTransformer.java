package org.evosuite.symbolic.vm.instrument;

import org.evosuite.dse.MainConfig;
import org.evosuite.symbolic.instrument.ConcolicBytecodeInstrumentation;
import org.evosuite.symbolic.vm.SymbolicEnvironment;
import org.objectweb.asm.ClassReader;

import java.lang.instrument.ClassFileTransformer;
import java.lang.instrument.IllegalClassFormatException;
import java.security.ProtectionDomain;
import java.util.HashMap;
import java.util.Map;

/**
 * Secondary instrumentation section. Only for invokedynamic generated lambdas
 *
 * @author csallner@uta.edu (Christoph Csallner)
 */
final class EvosuiteLambdaClassFileTransformer implements ClassFileTransformer {

  public static final Map<String, Class> transformedClasses = new HashMap();

  @Override
  public byte[] transform(
      ClassLoader cl,
      String typeName,
      Class<?> clazz,
      ProtectionDomain pd,
      byte[] bytes)
  throws IllegalClassFormatException
  {
  	final boolean isIgnored = !SymbolicEnvironment.isLambda(clazz) && MainConfig.get().isIgnored(clazz.getName());

    if (isIgnored)     // Only for lambdas, the rest is instrumented in ConcolicinstrumentingClassLoader
      return bytes;

    ClassReader classReader = new ClassReader(bytes);

    return new ConcolicBytecodeInstrumentation().transformBytes(clazz.getName(), classReader);
  }
}
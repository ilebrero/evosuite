package org.evosuite.symbolic.vm.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.PrintStream;

/**
 * Central log.
 * <p>
 * Offers convenience methods for logging short lists of strings that
 * do not use the String "+" operator.
 * <p>
 * FIXME: very primitive
 *
 * @author csallner@uta.edu (Christoph Csallner)
 */
public abstract class Log {

    private static final transient Logger logger = LoggerFactory.getLogger(Log.class);

    public final static String NL = System.getProperty("line.separator");
    public final static String FS = System.getProperty("file.separator");

    /**
     * FIXME: need a logging system
     */
    protected final static PrintStream out = System.out;


    /**
     * Log parameter as p.
     */
    public static void log(int p) {
        logger.info(String.valueOf(p));
    }

    /**
     * Log parameter as p.
     */
    public static void log(String p) {
        logger.info(p);
    }

    /**
     * Log parameters as ab.
     */
    public static void log(String a, String b) {
        log(a);
        log(b);
    }

    /**
     * Log parameters as abc.
     */
    public static void log(String a, String b, String c) {
        log(a);
        log(b);
        log(c);
    }

    /**
     * Log parameters as abcd.
     */
    public static void log(String a, String b, String c, String d) {
        log(a);
        log(b);
        log(c);
        log(d);
    }

    /**
     * Log parameters as abcde.
     */
    public static void log(String a, String b, String c, String d, String e) {
        log(a);
        log(b);
        log(c);
        log(d);
        log(e);
    }

    /**
     * Log newline.
     */
    public static void logln() {
        logger.info("");
    }

    /**
     * Log parameter as p followed by newline.
     */
    public static void logln(int p) {
        logger.info(String.valueOf(p));
        logln();
    }

    /**
     * Log stack trace of exception. If it is an exception thrown by the user
     * program, omit lower part of stack trace that shows Dsc invocation
     * machinery.
     */
    public static void logln(Throwable e) {
        if (e == null)
            return;

        log("Aborted with: ");
        logln(e.toString());

        StackTraceElement[] trace = e.getStackTrace();
        if (trace == null)
            return;

        for (StackTraceElement ste : trace) {
            if (ste == null)
                continue;

            String className = ste.getClassName();
            if (className != null && className.startsWith("edu.uta.cse.dsc.vm.MethodExploration")) {
                logln("\t.. invoked by Dsc.");
                break;
            }

            log("\tat ");
            logln(ste.toString());
        }
    }

    /**
     * Log parameter as p followed by newline.
     */
    public static void logln(Object p) {
        logln(p.toString());
    }

    /**
     * Log parameter as p followed by newline.
     */
    public static void logln(String p) {
        logger.info(p);
        logln();
    }

    /**
     * Log parameters as ab followed by newline.
     */
    public static void logln(String a, String b) {
        log(a, b);
        logln();
    }

    /**
     * Log parameters as abc followed by newline.
     */
    public static void logln(String a, String b, String c) {
        log(a, b, c);
        logln();
    }

    /**
     * Log parameters as abcd followed by newline.
     */
    public static void logln(String a, String b, String c, String d) {
        log(a, b, c, d);
        logln();
    }

    /**
     * Log parameters as abcde followed by newline.
     */
    public static void logln(String a, String b, String c, String d, String e) {
        log(a, b, c, d, e);
        logln();
    }

    public static void logfileIf(boolean doLog, Object o, String fileName) {
        if (!doLog)    // src-util should not depend on src-vm
            return;

        try (FileWriter fstream = new FileWriter(fileName);) {
            final BufferedWriter writer = new BufferedWriter(fstream);
            writer.write(o.toString());
            writer.close();
        } catch (Exception e) { //Catch exception if any
            System.err.println("File error: " + e.getMessage());
        }
    }


    /**
     * Logs parameter, if doLog.
     */
    public static void logIf(boolean doLog, String s) {
        if (doLog)
            log(s);
    }


    /**
     * Logs newline, if doLog.
     */
    public static void loglnIf(boolean doLog) {
        if (doLog)
            logln();
    }

    /**
     * Logs parameter followed by newline, if doLog.
     */
    public static void loglnIf(boolean doLog, String s) {
        if (doLog)
            logln(s);
    }

    /**
     * Logs parameters as ab followed by newline, if doLog.
     */
    public static void loglnIf(boolean doLog, String a, String b) {
        if (doLog)
            logln(a, b);
    }

    /**
     * Logs parameters as abc followed by newline, if doLog.
     */
    public static void loglnIf(boolean doLog, String a, String b, String c) {
        if (doLog)
            logln(a, b, c);
    }

    /**
     * Logs parameters as abcd followed by newline, if doLog.
     */
    public static void loglnIf(boolean doLog, String a, String b, String c, String d) {
        if (doLog)
            logln(a, b, c, d);
    }

    /**
     * Logs parameters as abcde followed by newline, if doLog.
     */
    public static void loglnIf(boolean doLog, String a, String b, String c, String d, String e) {
        if (doLog)
            logln(a, b, c, d, e);
    }
}
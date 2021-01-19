/*
 * Copyright (C) 2010-2018 Gordon Fraser, Andrea Arcuri and EvoSuite
 * contributors
 *
 * This file is part of EvoSuite.
 *
 * EvoSuite is free software: you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as published
 * by the Free Software Foundation, either version 3.0 of the License, or
 * (at your option) any later version.
 *
 * EvoSuite is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with EvoSuite. If not, see <http://www.gnu.org/licenses/>.
 */
package org.evosuite.symbolic.vm.instructionlogger;

import org.evosuite.Properties;
import org.evosuite.symbolic.vm.instructionlogger.implementations.FileDumpInstructionLogger;
import org.evosuite.symbolic.vm.instructionlogger.implementations.StandardOutputInstructionLogger;

import java.util.StringJoiner;

import static org.evosuite.symbolic.vm.instructionlogger.implementations.FileDumpInstructionLogger.EXECUTED_BYTECODE_FILE_NAME;

/**
 * Factory of possible bytecode loggers.
 *
 * @author Ignacio Lebrero
 */
public class InstructionLoggerFactory {
    public static final String LOGGING_MODE_NOT_PROVIDED = "A logging mode must be provided";
    public static final String LOGGING_MODE_NOT_YET_IMPLEMENTED = "logging mode not yet implemented: ";

    /** Simple Delimiter for path creation */
    private static final String PATH_DELIMITER = "/";

    /** Simple Delimiter for file creation */
    private static final String FILE_NAME_DELIMITER = "_";

    /** Singleton instance */
    private static final InstructionLoggerFactory self = new InstructionLoggerFactory();

    public static InstructionLoggerFactory getInstance() {
        return self;
    }

    public IInstructionLogger getInstructionLogger(Properties.DSEBytecodeLoggingMode bytecodeLoggingMode) {
        if (bytecodeLoggingMode == null) {
            throw new IllegalArgumentException(LOGGING_MODE_NOT_PROVIDED);
        }

        switch (bytecodeLoggingMode) {
            case STD_OUT:
                return new StandardOutputInstructionLogger(buildFileName(Properties.TARGET_CLASS, Properties.TARGET_METHOD));
            case FILE_DUMP:
                return new FileDumpInstructionLogger(buildFilePath(Properties.REPORT_DIR, buildFileName(
                        EXECUTED_BYTECODE_FILE_NAME,
                        Properties.TARGET_CLASS,
                        Properties.TARGET_METHOD)));
            default:
                throw new IllegalStateException(LOGGING_MODE_NOT_YET_IMPLEMENTED + bytecodeLoggingMode.name());
        }
    }

    /**
     * Creates a file name from a series of given Strings.
     * TODO(ilebrero): Move this to some "filePathUtils" at some point.
     *
     * @param params
     * @return
     */
    private String buildFileName(String... params) {
        return joinWithDelimeter(FILE_NAME_DELIMITER, params);
    }

    /**
     * Creates a path from a series of given Strings.
     * TODO(ilebrero): Move this to some "filePathUtils" at some point.
     *
     * @param path
     * @return
     */
    private String buildFilePath(String... path) {
        return joinWithDelimeter(PATH_DELIMITER, path);
    }

    /**
     * Joins a series of String using a delimiter.
     * TODO(ilebrero): Move this to some "filePathUtils" at some point.
     *
     * @param delimiter
     * @param elements
     * @return
     */
    private String joinWithDelimeter(String delimiter, String... elements) {
        StringJoiner joiner = new StringJoiner(delimiter);

        for (String element : elements) {
            joiner.add(element);
        }

        return joiner.toString();
    }
}

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
package org.evosuite.symbolic.vm.instructionlogger.implementations;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

/**
 * Instruction Logger that outputs through a text file.
 *
 * @author Ignacio Lebrero
 */
public final class FileDumpInstructionLogger extends AbstractInstructionLogger {

    private static final Logger logger = LoggerFactory.getLogger(FileDumpInstructionLogger.class);
    public static final String EXECUTED_BYTECODE_FILE_NAME = "executedBytecode.txt";


    private String filename;
    private FileWriter fstream;
    private BufferedWriter writer;

    public FileDumpInstructionLogger(String filename) {
        this.filename = filename;

        try {
            this.fstream = new FileWriter(filename);
            this.writer = new BufferedWriter(this.fstream);
        } catch (IOException e) {
            logger.error("Error when opening file " + filename);
            logger.error(e.getMessage());
        }
    }

    @Override
    public void log(String p) {
        try {
            writer.write(p);
        } catch (IOException e) {
            logger.error("Error when trying to write: " + p);
            logger.error(e.getMessage());
        }
    }

    @Override
    public void logln() {
        try {
            writer.write("\n");
        } catch (IOException e) {
            logger.error("Error when trying to write a new line");
            logger.error(e.getMessage());
        }
    }

    @Override
    public void cleanUp() {
        try {
            writer.close();
            fstream.close();
        } catch (IOException e) {
            logger.error("Error when trying to close file " + filename);
            logger.error(e.getMessage());
        }
    }
}

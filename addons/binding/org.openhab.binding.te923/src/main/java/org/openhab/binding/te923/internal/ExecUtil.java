/**
 * Copyright (c) 2010-2017 by the respective copyright holders.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.te923.internal;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.concurrent.TimeUnit;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Wrapper around exec functions. Merge error and command line result
 *
 * @author Gabriel Klein - Initial contribution
 */
public class ExecUtil {

    /**
     * Execute some command
     *
     * @param string
     * @return
     */
    public static String exec(String cmd) {

        Logger logger = LoggerFactory.getLogger(ExecUtil.class);

        int COMMAND_TIMEOUT = 10000;

        Process proc = null;
        try {
            Runtime rt = Runtime.getRuntime();
            proc = rt.exec(cmd);
        } catch (Exception e) {
            logger.error("An exception occurred while executing '{}' : '{}'", new Object[] { cmd, e.getMessage() });
            return "";
        }

        StringBuilder outputBuilder = new StringBuilder();
        StringBuilder errorBuilder = new StringBuilder();

        try (InputStreamReader isr = new InputStreamReader(proc.getInputStream());
                BufferedReader br = new BufferedReader(isr);) {
            String line = null;
            while ((line = br.readLine()) != null) {
                outputBuilder.append(line).append("\n");
                logger.debug("Exec [{}]: '{}'", "OUTPUT", line);
            }
            isr.close();
        } catch (IOException e) {
            logger.error("An exception occurred while reading the stdout when executing '{}' : '{}'",
                    new Object[] { cmd, e.getMessage() });
        }

        try (InputStreamReader isr = new InputStreamReader(proc.getErrorStream());
                BufferedReader br = new BufferedReader(isr);) {
            String line = null;
            while ((line = br.readLine()) != null) {
                errorBuilder.append(line).append("\n");
                logger.debug("Exec [{}]: '{}'", "ERROR", line);
            }
            isr.close();
        } catch (IOException e) {
            logger.error("An exception occurred while reading the stderr when executing '{}' : '{}'",
                    new Object[] { cmd, e.getMessage() });
        }

        boolean exitVal = false;
        try {
            exitVal = proc.waitFor(COMMAND_TIMEOUT, TimeUnit.MILLISECONDS);
        } catch (InterruptedException e) {
            logger.error("An exception occurred while waiting for the process ('{}') to finish : '{}'",
                    new Object[] { cmd, e.getMessage() });
        }

        if (!exitVal) {
            logger.warn("Forcibly termininating the process ('{}') after a timeout of {} ms",
                    new Object[] { cmd, COMMAND_TIMEOUT });
            proc.destroyForcibly();
        }

        // updateState(RUN, OnOffType.OFF);
        // updateState(EXIT, new DecimalType(proc.exitValue()));

        outputBuilder.append(errorBuilder.toString());

        outputBuilder.append(errorBuilder.toString());

        String r = StringUtils.chomp(outputBuilder.toString());

        return r;

    }

}

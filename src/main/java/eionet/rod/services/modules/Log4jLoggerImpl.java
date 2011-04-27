/**
 * The contents of this file are subject to the Mozilla Public
 * License Version 1.1 (the "License"); you may not use this file
 * except in compliance with the License. You may obtain a copy of
 * the License at http://www.mozilla.org/MPL/
 *
 * Software distributed under the License is distributed on an "AS
 * IS" basis, WITHOUT WARRANTY OF ANY KIND, either express or
 * implied. See the License for the specific language governing
 * rights and limitations under the License.
 *
 * The Original Code is "WebROD".
 *
 * The Initial Developer of the Original Code is TietoEnator.
 * The Original Code code was developed for the European
 * Environment Agency (EEA) under the IDA/EINRC framework contract.
 *
 * Copyright (c) 2000-2002 by European Environment Agency.  All
 * Rights Reserved.
 *
 * Original Code: Rando Valt (TietoEnator)
 *
 * $Id: Log4jLoggerImpl.java 8264 2010-04-08 12:55:05Z altnyris $
 */

package eionet.rod.services.modules;

import org.apache.log4j.Category;
import org.apache.log4j.Priority;

import eionet.rod.services.LogServiceIF;

/**
 * Log4J specific LogServiceIF implementation.<BR><BR>
 *
 * Date:    26.09.01<BR>
 * Updates: <UL>15.02.02 log4j version</UL>
 *
 * @author  Rando Valt
 * @version $Revision: 1.5 $
 */

public class Log4jLoggerImpl implements LogServiceIF {

    public static final int DEBUG       = 5;
    public static final int INFO        = 4;
    public static final int WARNING     = 3;
    public static final int ERROR       = 2;
    public static final int EMERGENCY   = 1;

  Category logger;

/**
 * Package local method for initializing Logger.
 * will be initialized by the servlet container.
 */
  public Log4jLoggerImpl() {
    logger = Category.getInstance("webrod");
  }

  private Priority convSeverity(int severity) {
    switch (severity) {
      case EMERGENCY:
        return Priority.FATAL;
      case ERROR:
        return Priority.ERROR;
      case WARNING:
        return Priority.WARN;
      case INFO:
        return Priority.INFO;
      case DEBUG:
      default:
        return Priority.DEBUG;
    }
  }
/**
 * Guard function to decide, whether the message of the given level shoul;d be logged.<BR><BR>
 *
 * Log level values can be between 1 and 5: 1 is the most silent, 5 the most talkative.
 */
  public boolean enable(int level)  {
    return logger.isEnabledFor(convSeverity(level));
  }
/**
 * Logs debug level message.
 */
  public void debug(Object msg) {
    logger.debug(msg);
  }

  public void debug(Object msg, Throwable t)  {
    logger.debug(msg, t);
  }

/**
 * Logs info level message.
 */
  public void info(Object msg)  {
    logger.info(msg);
  }

  public void info(Object msg, Throwable t) {
    logger.info(msg, t);
  }

/**
 * Logs debug warning message.
 */
  public void warning(Object msg) {
    logger.warn(msg);
  }

  public void warning(Object msg, Throwable t)  {
    logger.warn(msg, t);
  }

/**
 * Logs error level message.
 */
  public void error(Object msg) {
    if (msg instanceof Throwable) {
        Throwable t = (Throwable) msg;
        logger.error(t.getMessage(),t);
    }else{
        logger.error(msg);
    }
  }

  public void error(Object msg, Throwable t)  {
    logger.error(msg,t);
  }

/**
 * Logs error level message.
 */
  public void fatal(Object msg) {
    if (msg instanceof Throwable) {
        Throwable t = (Throwable) msg;
        logger.fatal(t.getMessage(),t);
    }else{
        logger.fatal(msg, null);
    }
  }

  public void fatal(Object msg, Throwable t)  {
    logger.fatal(msg,t);
  }
}

/*
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
 * The Original Code is "WFTool".
 *
 * The Initial Developer of the Original Code is TietoEnator.
 * The Original Code code was developed for the European
 * Environment Agency (EEA) under the IDA/EINRC framework contract.
 *
 * Copyright (c) 2000-2002 by European Environment Agency.  All
 * Rights Reserved.
 *
 * Original Code: Rando Valt (TietoEnator)
 */


package eionet.rod.countrysrv.services;

import com.tee.xmlserver.Logger;

/**
 * Exception class for service layer error/exception situations.
 *
 * @author  Rando Valt
 * @version 1.0
 */
public class ServiceException extends java.lang.Exception {

/**
 * Creates new <code>ServiceException</code> without detail message.
 */
    public ServiceException() {
    }


/**
 * Constructs an <code>ServiceException</code> with the specified detail message.
 * @param msg the detail message.
 */
    public ServiceException(String msg) {
        super(msg);
        Logger.log("Service exception occured with reason <<" + msg + ">>");
    }
}



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
 * The Original Code is "EINRC-5 / WebROD Project".
 *
 * The Initial Developer of the Original Code is TietoEnator.
 * The Original Code code was developed for the European
 * Environment Agency (EEA) under the IDA/EINRC framework contract.
 *
 * Copyright (C) 2000-2002 by European Environment Agency.  All
 * Rights Reserved.
 *
 * Original Code: Ander Tenno (TietoEnator)
 */

//
// Opens edit help window
//
function openHelp(ID){
	var url = "help.jsv?helpID=" + ID;
	var name = "Help";
	var features = "location=no, menubar=no, width=485, height=480, top=100, left=200, scrollbars=no";
	var w = window.open(url,name,features);
	w.focus();
}

//
// Opens view help window
//
function openViewHelp(ID){
	var url = "viewhelp.jsv?helpID=" + ID;
	var name = "Help";
	var features = "location=no, menubar=no, width=470, height=345, top=100, left=200, scrollbars=no";
	var w = window.open(url,name,features);
	w.focus();
}

<?xml version="1.0"?>

<!--
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
 * The Original Code is "EINRC-4 / WebROD Project".
 *
 * The Initial Developer of the Original Code is TietoEnator.
 * The Original Code code was developed for the European
 * Environment Agency (EEA) under the IDA/EINRC framework contract.
 *
 * Copyright (C) 2000-2002 by European Environment Agency.  All
 * Rights Reserved.
 *
 * Original Code: Andre Karpistsenko (TietoEnator)
 * -->

<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0">
	<!--xsl:output indent="yes"/-->   
	<xsl:output indent="yes" doctype-public="-//W3C//DTD HTML 4.01 Transitional//EN" omit-xml-declaration="yes"/>   
	<!--xsl:output indent="yes" doctype-public="-//W3C//DTD HTML 4.01 Transitional//EN" doctype-system="http://www.w3.org/TR/html4/loose.dtd" omit-xml-declaration="yes"/-->   

	<xsl:include href="util.xsl"/>
            
	<xsl:template match="/">
		<html>
			<head>
				<title><xsl:call-template name="PageTitle"/></title>
				<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
				<META HTTP-EQUIV="Cache-Control" CONTENT="no-cache"/>
				<META HTTP-EQUIV="Expires" CONTENT="Tue, 01 Jan 1980 00:00:00 GMT"/>
				<link href="eionet.css" rel="stylesheet" type="text/css"/>
				<script language="JavaScript" src="script/util.js"></script>
				<script language="JavaScript">
					<![CDATA[
<!--
			/*
			* selects multilist values based on the text value
			* example: |a||c||d|, values a, c, d in multilist will be selected
			*/
			function selectMultilist(txtBox, lst) {
				var s = txtBox.value;
				for (i = 0; i < lst.length; ++i) {
					txt=lst.options[i].value;
					if(s.indexOf("|" + txt + "|")!=-1)
						lst.options[i].selected=true;
				}
			}
			function changeMulti(txt, lst) {
				var str="";
				for (i = 0; i < lst.length; ++i) {
					if (lst[i].selected) 
						str+="|" + lst.options[i].value+ "|";
				}				
				txt.value=str;

			}

			function addCl() {
				document.forms["f"].silent.value=1;
				save(null,true);	//silent save
				openAddClientWin();
				document.forms["f"].silent.value=0; 
				}

			function updIndicator(id, raId) {
				document.forms["f"].silent.value=1;
				save(null,true);
				openPopup('indicator.jsv', 'id=' + id + '&amp;aid=' + raId);
				document.forms["f"].silent.value=0;
				}

			function delIndicator(id, raId, srcId) {
				document.forms["f"].silent.value=1;
				save(null,true);
				openPopup('indicator.jsv', 'dom-update-mode=D&amp;id=' + id + '&amp;aid=' + raId + '&amp;srcid=' + srcId);
				document.forms["f"].silent.value=0; 
			} 

			function chkValue(chkBox, fldName) {
				chkValue(chkBox, fldName, false);
			}
			/**
			* changes fld value according to chkBox status
			* changes fld value to 0 or 1
			* if neg=true then the values are swapped
			*/
			function chkValue(chkBox, fldName, neg) {
				var posV = "1";
				var negV = "0";

				if (neg) {
					posV="0";
					negV="1";
				}
				if (chkBox.checked)
					fldName.value=posV;
				else
					fldName.value=negV;

				changed();
			}


function openAddClientWin() {
	window.open('addclient.jsp','sInfo','height=405,width=470,status=no,toolbar=no,scrollbars=no,resizable=yes,menubar=no,location=no');
}

var isChanged = false;

function checkStatus() {
	if (isChanged) {
		var mode = document.f.elements["dom-update-mode"];
		if (mode == 'D') mode = 'U';
		save("Save data?", false);
	}
}
var selects = new Array();
var compulsory = new Array();

function inclSelect(sel1, sel2) {
	var pair = new Array(2);
	pair[0] = sel1;
	pair[1] = sel2;
	selects[selects.length] = pair;
}

function compField(txt, fld) {
	var pair = new Array(2);
	pair[0] = txt;
	pair[1] = fld;
	compulsory[compulsory.length] = pair;
}

function del() {
	document.f.elements["dom-update-mode"].value="D";
	save(null,false);
}

function save(text,silent) {
	var i, j;
	//alert("save");
	var mode = document.f.elements["dom-update-mode"];
	var bDelete = (mode.value == 'D');

	for (i=0; i < compulsory.length; ++i) {
		obj = compulsory[i];
		if (obj[1].value.length == 0) {
			alert("Value for the " + obj[0] + " field is compulsory!");
			return false;
		}
	}
	
	if (text == null) {
		text = (mode.value != 'D' ? "Save data?" 	: "Delete record?");
		//text = "Do you want to " + text;
	}

	if (!silent)
		if (confirm(text) == false)
			return false;

	if (!bDelete) {
		// check once more all values before sending to server
		for (i = 0; i < document.f.length; ++i) {
			elem = document.f.elements[i];
			elem.disabled = false;

			if (elem.type == 'text' && elem.onchange != null) {
				var s = new String(elem.onchange);
				if (s.indexOf('checkDate') >= 0) {
					if (!checkDate(elem)) {
						return false;
					}
				}
				else if (s.indexOf('checkNumber') >= 0) {
					if (!checkNumber(elem)) {
						return false;
					}
				}
			}//end-if onchange
		}//end-for document.f.elements
	   // select all options of the specified selects
		for (i = 0; i < selects.length; ++i) {
			selValues(selects[i][0]);
		}
	}//end-if !bDelete


	document.f.submit();
	isChanged = false;

	return true;
}

function checkDate(field) {
	var s = field.value;
	
	isChanged = true;

	// empty strings are valid
	if (s.length == 0)
		return true; 

	var re = /([0-9]{2})\/([0-9]{2})\/([0-9]{4})/;
	
	if (s.length != 10 || !re.test(s)) {
		warn(field, 'Invalid date. Date needs to be in dd/mm/yyyy format.');
		return false;
	}


	var d = RegExp.$1;
	var m = RegExp.$2;
	var y = RegExp.$3;

	if (m < 1 || m > 12) {
		warn(field, 'Month should be between 1 and 12.');
		return false;
	}
	else if ((d < 1) || 
				((m == 1 || m == 3 || m == 5 || m == 7 || m == 8 || m == 10 || m == 12) && d > 31) ||
				((m == 4 || m == 6 || m == 9 || m == 11) && d > 30) ||
				(m == 2 && d > 29)) {
		warn(field, 'Invalid value for day.');
		return false;
	}

	return true;
}


function checkNumber(field) {
	isChanged = true;

	// empty strings are valid
	if (field.value.length == 0)
		return true; 

	if (isNaN(field.value)) {
		warn(field, 'Number is expected.');
		return false;
	}

	return true;
}

function changed() {
	isChanged = true;
}

function selValues(sel) {
	for (i = 0; i < sel.length; ++i) {
		sel[i].selected = true;
	}
}

var browser = document.all ? 'E' : 'N';
function mvValues(selFrom, selTo, unit) {
	var i, count = 0;
	var optsLen;
	var newVal, newText;
	
	isChanged = true;

	var selected = new Array();

	for (i = 0; i < selFrom.length; ++i) {
		if (selFrom[i].selected) {
			selected[count++] = i;

			newVal = selFrom[i].value;
			var pos = newVal.indexOf(':');
			if (pos > 0) {
				newVal = newVal.substr(0, pos);
			}
			
			newText = selFrom[i].text;
			pos = newText.indexOf('[');
			if (pos > 1) {
				newText = newText.substr(0, pos - 1); // strip leading space as well
			}
			if (unit != null && unit.text.length > 0) {
				newVal = newVal + ':' + unit.value;
				newText = newText + ' [' + unit.text + ']';
			}

			// add to
			if (browser == 'E') {
				var opt = document.createElement("option");
				selTo.add(opt);
				opt.text = newText;
				opt.value = newVal;
			}
			else if (browser == 'N') {
				var opt = new Option(newText, newVal, false, false);
				selTo[selTo.length] = opt;
			}
		}
	}
	
	// remove from	
	count = 0;
	for (i = selected.length-1; i >= 0; --i) {
		if (browser == 'E') {
			selFrom.remove(selected[i]);
		}
		else if (browser == 'N') {
			selFrom[selected[i]] = null;
		}
	}
}

/**
* KL
*/
function addFullValues(selFrom, selTo) {
	var i, j, count = 0;
	var optsLen;
	var newVal, newText;
	
	isChanged = true;

	var selected = new Array();

	for (i = 0; i < selFrom.length; ++i) {
		if (selFrom[i].selected) {
			var exists;
			exists = false;
			for (j = 0;j < selTo.length; ++j) {
				var s,x;
				s = new String(selTo[j].value);
				u = new String("");

/*				if (s.indexOf(":") > -1) {
					u = s.substr(s.indexOf(":")+1);
					s = s.substring(0,s.indexOf(":"));
				} */

					if (s.valueOf() == selFrom[i].value) {
						exists = true;
						break;
					}
			}
			if (!exists) {
				selected[count++] = i;
	
				newVal = selFrom[i].value;

				var pos = newVal.indexOf(':');
				/*if (pos > 0) {
					newVal = newVal.substr(0, pos);
				} */
				
				newText = selFrom[i].text;
				pos = newText.indexOf('[');
				if (pos > 1) {
					newText = newText.substr(0, pos - 1); // strip leading space as well
				}
				/*if (unit != null && unit.text.length > 0) {
					newVal = newVal + ':' + unit.value;
					newText = newText + ' [' + unit.text + ']';
				} */
	
				// add to
				if (browser == 'E') {
					var opt = document.createElement("option");
					selTo.add(opt);
					opt.text = newText;
					opt.value = newVal;
				}
				else if (browser == 'N') {
					var opt = new Option(newText, newVal, false, false);
					selTo[selTo.length] = opt;
				}
			}
		}
	}
}

function addValues(selFrom, selTo, unit) {
	var i, j, count = 0;
	var optsLen;
	var newVal, newText;
	
	isChanged = true;

	var selected = new Array();

	for (i = 0; i < selFrom.length; ++i) {
		if (selFrom[i].selected) {
			var exists;
			exists = false;
			for (j = 0;j < selTo.length; ++j) {
				var s,x;
				s = new String(selTo[j].value);
				u = new String("");
				if (s.indexOf(":") > -1) {
					u = s.substr(s.indexOf(":")+1);
					s = s.substring(0,s.indexOf(":"));
				}
				
				if (unit != null) {
					if ((s.valueOf() == selFrom[i].value) && (u.valueOf() == unit.value)) {
						exists = true;
						break;
					}
				}
				else
					if (s.valueOf() == selFrom[i].value) {
						exists = true;
						break;
					}
			}
			if (!exists) {
				selected[count++] = i;
	
				newVal = selFrom[i].value;
				var pos = newVal.indexOf(':');
				if (pos > 0) {
					newVal = newVal.substr(0, pos);
				}
				
				newText = selFrom[i].text;
				pos = newText.indexOf('[');
				if (pos > 1) {
					newText = newText.substr(0, pos - 1); // strip leading space as well
				}
				if (unit != null && unit.text.length > 0) {
					newVal = newVal + ':' + unit.value;
					newText = newText + ' [' + unit.text + ']';
				}
	
				// add to
				if (browser == 'E') {
					var opt = document.createElement("option");
					selTo.add(opt);
					opt.text = newText;
					opt.value = newVal;
				}
				else if (browser == 'N') {
					var opt = new Option(newText, newVal, false, false);
					selTo[selTo.length] = opt;
				}
			}
		}
	}
}


function delValues(selFrom) {
	for (i = selFrom.length-1; i >= 0; --i) {
	  if (selFrom[i].selected) {
		if (browser == 'E') {
			selFrom.remove(i);
		}
		else if (browser == 'N') {
			selFrom[i] = null;
		}
	  }
	}
}

var picklist = new Array();
var multilist = new Array();
var clist=new Array();

function fillPicklist(type,list,text) {
      var i,js;
	for (i = list.length; i > 0; --i)
		list.options[i] = null;
	list.options[0] = new Option("Choose a group","-1");
	j = 1;
	for (i = 0; i < picklist.length; i++) {
	  s = new String(picklist[i]);
	  pvalue = s.substring(0,s.indexOf(":"));
	  ptext = s.substring(s.indexOf(":")+1,s.lastIndexOf(":"));
	  ptype = s.substring(s.lastIndexOf(":")+1,s.lastIndexOf(":")+2);
	  if (ptype.valueOf() == type) {
	  	list.options[j] = new Option(ptext.valueOf(), pvalue.valueOf());
	  	j++;
	  }
	} 
	list.options[0] = null;
	list.options[0].selected = true;
}

function fillclist(type,list,text) {
      var i,js;
	for (i = list.length; i > 0; --i)
		list.options[i] = null;
	list.options[0] = new Option("Choose a group","-1");
	j = 1;
	for (i = 0; i < clist.length; i++) {
	  s = new String(clist[i]);
	  pvalue = s.substring(0,s.indexOf(":"));
	  ptext = s.substring(s.indexOf(":")+1,s.lastIndexOf(":"));
	  ptype = s.substring(s.lastIndexOf(":")+1,s.lastIndexOf(":")+2);
	  if (ptype.valueOf() == type) {
	  	list.options[j] = new Option(ptext.valueOf(), pvalue.valueOf());
	  	j++;
	  }
	} 
	list.options[0] = null;
	list.options[0].selected = true;
}


function fillMultilist(type,list,text) {
      var i,js;
	for (i = list.length; i > 0; --i)
		list.options[i] = null;
	j = 0;
	for (i = 0; i < multilist.length; i++) {
	  s = new String(multilist[i]);
	  pvalue = s.substring(0,s.indexOf(":"));
	  ptext = s.substring(s.indexOf(":")+1,s.lastIndexOf(":"));
	  li = s.length-s.lastIndexOf(":");
	  ptype = s.substring(s.lastIndexOf(":")+1,s.lastIndexOf(":")+li);
	  if (ptype.valueOf() == type) {
	  	list.options[j] = new Option(ptext.valueOf(), pvalue.valueOf());
	  	j++;
	  }
	} 
}

function warn(field, msg) {
	alert(msg);
	if (browser == 'N') {
		field.select();
	}
	field.focus();
}

function changedReporting(first, freq, next, to, terminate, next2) {
	changed();

	var utcMonth;
	next.value = "";
	next2.value = "";
	terminate.value = "N";
	
	fiDate = checkDateSimple(first);
	toDate = checkDateSimple(to);
	fx = checkNumberSimple(freq);

	// Provide default if to value not given
	//
	if(fiDate != false && toDate == false)
		to.value = "31/12/9999";			

	// Check that date fields have valid values
	//
	if(fiDate == false || toDate == false)
		return;

	// Calculate value for TERMINATE
	//
	var currDate = new Date();
	currDate.setUTCMinutes(currDate.getUTCMinutes() - currDate.getTimezoneOffset());
	var toDat = new Date(toDate.yx, parseInt(toDate.mx, 10)-1, toDate.dx, 23, 59, 59);
	toDat.setUTCMinutes(toDat.getUTCMinutes() - toDat.getTimezoneOffset());
	if(currDate.getTime() > toDat.getTime())
		terminate.value = "Y";

	// Check that frequency field has valid value
	//
	if(fx == -1)
		return;
	
	// If non-repeating, use First Reporting for Next Due Date
	//
	if(fx == 0) {
		next.value = first.value;
		return;
	}
	
	// Repeating date
	//
	currDate.setUTCDate(currDate.getUTCDate() - (fx * 3));  // Buffer
	var repDate = new Date(fiDate.yx, parseInt(fiDate.mx, 10)-1, fiDate.dx, 20);
	repDate.setUTCMinutes(repDate.getUTCMinutes() - repDate.getTimezoneOffset());
	if(fiDate.dx < 28) {
		while(repDate.getTime() < currDate.getTime() && repDate.getTime() < toDat.getTime())
			repDate.setUTCMonth(repDate.getUTCMonth() + fx);
		if(repDate.getTime() > toDat.getTime())
			repDate.setUTCMonth(repDate.getUTCMonth() - fx);
	}
	else {
		repDate.setUTCDate(repDate.getUTCDate() - 3);
		while(repDate.getTime() < currDate.getTime() && repDate.getTime() < toDat.getTime())
			repDate.setUTCMonth(repDate.getUTCMonth() + fx);
		if(repDate.getTime() > toDat.getTime())
			repDate.setUTCMonth(repDate.getUTCMonth() - fx);
		var rewindDate = new Date(repDate.getTime()); // Save for check below
		utcMonth = repDate.getUTCMonth();
		while(utcMonth == repDate.getUTCMonth())
			repDate.setUTCDate(repDate.getUTCDate() + 1);
		repDate.setUTCDate(repDate.getUTCDate() - 1);
		// If we went over Valid To date, rewind and repeat
		//
		if(repDate.getTime() > toDat.getTime()) {
			repDate.setTime(rewindDate.getTime());
			repDate.setUTCMonth(repDate.getUTCMonth() - fx);
			utcMonth = repDate.getUTCMonth();
			while(utcMonth == repDate.getUTCMonth())
				repDate.setUTCDate(repDate.getUTCDate() + 1);
			repDate.setUTCDate(repDate.getUTCDate() - 1);
		}
	}
	next.value = ddmmyyyyDate(repDate);
	
	// Calculate value for TERMINATE
	//
	if(repDate.getTime() < currDate.getTime())
		terminate.value = "Y";

	// Deadline after the next
	//
	if(repDate.getUTCDate() < 28)
		repDate.setUTCMonth(repDate.getUTCMonth() + fx);
	else {
		repDate.setUTCDate(repDate.getUTCDate() - 3);
		repDate.setUTCMonth(repDate.getUTCMonth() + fx);
		utcMonth = repDate.getUTCMonth();
		while(utcMonth == repDate.getUTCMonth())
			repDate.setUTCDate(repDate.getUTCDate() + 1);
		repDate.setUTCDate(repDate.getUTCDate() - 1);
	}
	if(repDate.getTime() < toDat.getTime())
		next2.value = ddmmyyyyDate(repDate);
}

function checkDateSimple(field) {
	rVal = new Object;
	var s = field.value;
	
	if(s.length == 0)
		return false; 

	var re = /([0-9]{2})\/([0-9]{2})\/([0-9]{4})/;
	
	if(s.length != 10 || !re.test(s)) {
		alert("Invalid date format. Date needs to be in dd/mm/yyyy format.");
		return false;
	}

	rVal.dx = RegExp.$1;
	rVal.mx = RegExp.$2;
	rVal.yx = RegExp.$3;

	if(rVal.mx < 1 || rVal.mx > 12) {
		alert("Invalid date.");
		return false;
	}
	else if ((rVal.dx < 1) || 
				((rVal.mx == 1 || rVal.mx == 3 || rVal.mx == 5 || rVal.mx == 7 || rVal.mx == 8 || rVal.mx == 10 || rVal.mx == 12) && rVal.dx > 31) ||
				((rVal.mx == 4 || rVal.mx == 6 || rVal.mx == 9 || rVal.mx == 11) && rVal.dx > 30) ||
				(rVal.mx == 2 && rVal.dx > 29)) {
		alert("Invalid date.");
		return false;
	}

	return rVal;
}

function checkNumberSimple(field) {
	var s = field.value;
	
	if(s.length == 0)
		return -1; 
	
	var fx = parseInt(field.value);
	if(isNaN(fx) || fx < 0) {
		alert("Invalid reporting frequency. Positive, whole number expected.");
		return -1;
	}

	return fx;
}

function ddmmyyyyDate(dat) {
	var s;
	
	if(dat.getUTCDate() >= 10)
		s = dat.getUTCDate();
	else
		s = "0" + dat.getUTCDate();

	s += "/";
	if(dat.getUTCMonth() >= 9)
		s += parseInt(dat.getUTCMonth() + 1);
	else
		s += "0" + parseInt(dat.getUTCMonth() + 1);
		
	s += "/" + dat.getUTCFullYear();
	
	return s;
}

function checkAndSave(first, freq, next, textrep, to, terminate) {
	if(textrep.value.length == 0 && (first.value.length == 0 || to.value.length == 0 || freq.value.length == 0)) {
		alert("Both Reporting Date (Text Format) and one or more of normal reporting date fields (Baseline Reporting Date, Valid To, Reporting Frequency) are empty. One of them must be used. If you are entering reporting date using date and frequency fields, please leave Reporting Date (Text Format) field empty. If you would like to use the text-based field, leave Reporting Frequency field empty.");
		return;
	}
	else if(textrep.value.length != 0 && next.value.length != 0) {
		alert("Both Reporting Date (Text Format) and normal reporting date fields (Baseline Reporting Date, Valid To, Reporting Frequency) are used. If you are entering reporting date using date and frequency fields, please leave Reporting Date (Text Format) field empty. If you would like to use the text-based field, leave Reporting Frequency field empty.");
		return;
	}
	else if(next.value.length == 0 && first.value.length != 0 && to.value.length != 0 && freq.value.length != 0) {
		alert("Unable to calculate next due date. Please make sure you have entered valid date (dd/mm/yyyy format) in Baseline Reporting Date field and whole number (0 for non-repeating reporting) in Reporting Frequency in Months field.");
		return;
	}
	
	// Provide default if to value not given
	//
	if(to.value.length == 0) {
		terminate.value = "N";
		to.value = "31/12/9999";
	}		

	next.disabled = false;
	save(null,false);
}

//-->
					]]>
			</script>
		</head>
		<body bgcolor="#f0f0f0" topmargin="0" leftmargin="0" marginwidth="0" marginheight="0"
			background="images/eionet_background.jpg" onUnload="checkStatus()">

			<!-- MAIN table -->
			<table border="0" cellpadding="0" cellspacing="0">
			<tr>
			  <td width="130" valign="top"><img src="images/top1.jpg" height="113" width="130" alt=""/></td>
			  <td width="20" valign="top"><img height="113" width="20" src="images/top2.jpg" alt=""/></td>
			  <td width="621" valign="top"> 
			    <table border="0" cellpadding="0" cellspacing="0">
			    <tr>
			      <td><img src="images/top3.jpg" width="92" height="35" alt=""/></td>
			    </tr>
			    <tr>
			      <td><table border="0" width="621">
			          <tr>
			            <td width="648">
								<div class="sitetitle"><xsl:call-template name="FirstHeading"/></div>
								<div class="sitetagline"><xsl:call-template name="SecondHeading"/></div>
							</td>
			            <td width="20">&#160;</td>
			            <td><a href="/" target="_blank"><img src="images/logo.jpg" alt="" height="62" width="66" border="0"/></a></td>
			          </tr>
			          </table>
			          </td>
			        </tr>
			      </table>
			    </td>
			  </tr>
			</table>

			<table border="0">
				<tr valign="top" width="95%"><td width="125" nowrap="nowrap">
					<!-- Toolbar -->
					<xsl:call-template name="LeftToolbar"><xsl:with-param name="admin">false<!--xsl:value-of select="$admin"/--></xsl:with-param></xsl:call-template>
				</td>
				<td width="15" nowrap="nowrap">&#160;</td>
				<td>
					<xsl:apply-templates select="XmlData"/>
				</td>
			</tr></table>
			</body>
   	 </html>
	</xsl:template>  
</xsl:stylesheet>

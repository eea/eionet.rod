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
	<xsl:output indent="yes"/>   
            
	<xsl:template match="/">
   	<html>
			<head>
				<title><xsl:call-template name="PageTitle"/></title>
				<META HTTP-EQUIV="Content-Type" CONTENT="text/html; CHARSET=ISO-8859-1"/>
				<META HTTP-EQUIV="Cache-Control" CONTENT="no-cache"/>
				<META HTTP-EQUIV="Expires" CONTENT="Tue, 01 Jan 1980 00:00:00 GMT"/>
				<link href="eionet.css" rel="stylesheet" type="text/css"/>
				<script language="JavaScript">
					<![CDATA[
<!--
Net=1;
if ((navigator.appName.substring(0,5) == "Netsc"
  && navigator.appVersion.charAt(0) > 2)
  || (navigator.appName.substring(0,5) == "Micro"
  && navigator.appVersion.charAt(0) > 3)) {
 Net=0;

 over = new Image;
 out = new Image;
 gammel = new Image;

 over.src = "images/on.gif";
 out.src = "images/off.gif";
 
 gTarget = 'img1';
}

function Click(Target) {
 if (Net != 1){
  if (Target != gTarget) {
   document[Target].src = over.src;
   document[gTarget].src = out.src;
   gTarget = Target;
   gammel.src = document[Target].src;
  }
 }
}

function Over(Target) {
 if (Net != 1){
  gammel.src = document[Target].src;
  document[Target].src = over.src;
 }
}

function Out(Target) {
 if (Net != 1){
  document[Target].src = gammel.src;
 }
}
var browser = document.all ? 'E' : 'N';
var isChanged = false;

function checkStatus() {
	if (isChanged) {
		var mode = document.f.elements["dom-update-mode"];
		if (mode == 'D') mode = 'U';
		save("Do you want to save changes?");
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

function save() {
	return save(null);
}

function del() {
	document.f.elements["dom-update-mode"].value="D";
	save();
}

function save(text) {
	var i, j;
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
		text = (mode.value == 'U') ? "save modified data?" 
											: ( (mode.value == 'A') ? "insert a new record?" 
																			: "delete the current record (with all links)?");
		text = "Do you want to " + text;
	}

	if (confirm(text) == false)
		return false;

	if (!bDelete) {
		// check once more all values before sending to server
		for (i = 0; i < document.f.length; ++i) {
			elem = document.f.elements[i];

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
			}//end-if onChange
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
	
	if (!re.test(s)) {
		warn(field, 'Date in format dd/mm/yyyy is expected.');
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
	
/*	if (unit != null) {
		unit.value = '';
	}*/

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
//-->
					]]>
			</script>
		</head>
		<body bgcolor="#f0f0f0" topmargin="0" leftmargin="0" marginwidth="0" marginheight="0"
			background="images/eionet_background.jpg" onUnload="checkStatus()">

			<!-- MAIN table -->
			<table border="0" cellpadding="0" cellspacing="0">
			<tr>
			  <td width="130" bgcolor="#FFB655" valign="top"><img src="images/eionet1.jpg" height="113" width="130" alt=""/></td>
			  <td width="20" valign="top"><img height="113" width="20" src="images/eionet2.jpg" alt=""/></td>
			  <td width="621" valign="top"> 
			    <table border="0" cellpadding="0" cellspacing="0">
			    <tr>
			      <td><img src="images/eionet3.jpg" width="92" height="35" alt=""/></td>
			    </tr>
			    <tr>
			      <td><table border="0" width="621">
			          <tr>
			            <td width="618">
								<font color="#006666" size="5" face="Arial"><strong><span class="head2">
									<xsl:call-template name="FirstHeading"/>
								</span></strong></font>
								<br/>
								<font color="#006666" face="Arial" size="2"><strong><span class="head0">
									<xsl:call-template name="SecondHeading"/>
								</span></strong></font>
							</td>
			            <td width="50">&#160;</td>
			            <td><img src="images/logo.jpg" alt="" height="62" width="66" border="0"/></td>
			          </tr>
			          </table>
			          </td>
			        </tr>
			      </table>
			    </td>
			  </tr>
			</table>

			<table border="0">
				<tr valign="top" width="95%"><td width="125" nowrap="true">
					<!-- Toolbar -->
					<p><center>
						<table border="0" cellpadding="0" cellspacing="0">
						<tr><td align="center"><span class="head0">Contents</span></td></tr>
<!--
						<tr><td align="right">
							<a>	<xsl:attribute name="href">
									show.jsv?id=<xsl:call-template name="DB_Legal_Root_ID"/>&amp;mode=C
								</xsl:attribute>
								<xsl:attribute name="onMouseOver">
									Over('img0')
								</xsl:attribute>
								<xsl:attribute name="onMouseOut">
									Out('img0')
								</xsl:attribute>
								<img name="img0" src="images/off.gif" border="0" alt=""/>
								<img src="images/button_legislation.gif" border="0" width="84" height="13" alt="Legislation"/>
							</a>
						</td></tr>
-->
						<tr><td align="right">
							<a href="rorabrowse.jsv?mode=R" onMouseOver="Over('img1')" onMouseOut="Out('img1')" onClick="Click('img1')">
								<img name="img1" src="images/off.gif" border="0" alt=""/>
								<img src="images/button_obligations.gif" border="0" width="84" height="13" alt="Reporting Obligations"/>
							</a>
						</td></tr>
						<tr><td align="right">
							<a href="rorabrowse.jsv?mode=A" onMouseOver="Over('img2')" onMouseOut="Out('img2')" onClick="Click('img2')">
								<img name="img2" src="images/off.gif" border="0" alt=""/>
								<img src="images/button_activities.gif" border="0" width="84" height="13" alt="Reporting Activities"/>
							</a>
						</td></tr>
						<tr><td align="right">
							<a href="rorabrowse.jsv?mode=R&amp;type=14,25,2,3,4,5,6,7,8,9,10,11,12,13:EU%20legislation%20obligations" onMouseOver="Over('img3')" onMouseOut="Out('img3')" onClick="Click('img3')">
								<img name="img3" src="images/off.gif" border="0" alt=""/>
								<img src="images/button_eulegislation.gif" border="0" width="84" height="13" alt="EU legislation obligations"/>
							</a>
						</td></tr>
						<tr><td align="right">
							<a href="rorabrowse.jsv?mode=R&amp;type=15:Conventions'%20obligations" onMouseOver="Over('img4')" onMouseOut="Out('img4')" onClick="Click('img4')">
								<img name="img4" src="images/off.gif" border="0" alt=""/>
								<img src="images/button_conventions.gif" border="0" width="84" height="13" alt="Conventions obligations"/>
							</a>
						</td></tr>
						<tr><td align="right">
							<a href="rorabrowse.jsv?mode=R&amp;type=21:EEA%20requests" onMouseOver="Over('img5')" onMouseOut="Out('img5')" onClick="Click('img5')">
								<img name="img5" src="images/off.gif" border="0" alt=""/>
								<img src="images/button_eearequests.gif" border="0" width="84" height="13" alt="EEA requests"/>
							</a>
						</td></tr>
						<tr><td align="right">
							<a href="rorabrowse.jsv?mode=R&amp;type=22:Eurostat%20requests" onMouseOver="Over('img6')" onMouseOut="Out('img6')" onClick="Click('img6')">
								<img name="img6" src="images/off.gif" border="0" alt=""/>
								<img src="images/button_eurostatrequests.gif" border="0" width="84" height="13" alt="Eurostat requests"/>
							</a>
						</td></tr>
						<tr><td align="right">
							<a href="rorabrowse.jsv?mode=R&amp;type=23:Other%20requests" onMouseOver="Over('img7')" onMouseOut="Out('img7')" onClick="Click('img7')">
								<img name="img7" src="images/off.gif" border="0" alt=""/>
								<img src="images/button_otherrequests.gif" border="0" width="84" height="13" alt="Other requests"/>
							</a>
						</td></tr>
<!--
						<tr><td align="right">
							<a href="/countrysrv/public/csindex" onMouseOver="Over('img8')" onMouseOut="Out('img8')" onClick="Click('img8')">
								<img name="img8" src="images/off.gif" border="0" alt=""/>
								<img src="images/button_cs.gif" border="0" width="84" height="13" alt="Deliveries"/>
							</a>
						</td></tr>
-->
						</table>
				</center></p>
				</td>
				<td width="15" nowrap="true">&#160;</td>
				<td>
					<xsl:apply-templates select="XmlData"/>
				</td>
			</tr></table>
			</body>
   	 </html>
	</xsl:template>  

	<xsl:include href="static.xsl"/>
</xsl:stylesheet>
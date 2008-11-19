<?xml version="1.0" encoding="UTF-8"?>

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

<xsl:stylesheet
	exclude-result-prefixes="java"
	xmlns:java="http://xml.apache.org/xslt/java"
	xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0">
	<!--xsl:output indent="yes"/-->   
	<xsl:output indent="yes" doctype-public="-//W3C//DTD HTML 4.01 Transitional//EN" omit-xml-declaration="yes" encoding="UTF-8"/>   
	<!--xsl:output indent="yes" doctype-public="-//W3C//DTD HTML 4.01 Transitional//EN" doctype-system="http://www.w3.org/TR/html4/loose.dtd" omit-xml-declaration="yes"/-->   

	<xsl:variable name="admin">
		<xsl:value-of select="//RowSet[position()=1]/@auth"/>
	</xsl:variable>
	<xsl:variable name="username">
		<xsl:value-of select="//RowSet[position()=1]/@username"/>
	</xsl:variable>

	<xsl:include href="util.xsl"/>
	<xsl:include href="dropdownmenus.xsl"/>
	
	<xsl:variable name="permissions">
		<xsl:value-of select="/XmlData/RowSet/@permissions"/>
	</xsl:variable>
            
	<xsl:template match="/">
		<html xml:lang="en">
			<head>
				<meta name="title" content="EEA - Reporting Obligations Database" />
				<meta name="description" content="The EEA's reporting obligations database (ROD) contains information describing environmental reporting obligations that countries have towards international organisations." />
				<meta name="keywords" content="reporting obligations, environmental legislation, environmental reporting, environmental dataflows, European Environment Agency, EEA, European, Environmental information, Environmental portal, Eionet, Reportnet, air, waste, water, biodiversity" />
				<meta name="Publisher" content="EEA, The European Environment Agency" />
				<meta name="Rights" content="Copyright EEA Copenhagen 2003" />

				<title><xsl:call-template name="PageTitle"/></title>
				<link rel="stylesheet" type="text/css" href="http://www.eionet.europa.eu/styles/eionet2007/print.css" media="print" />
				<link rel="stylesheet" type="text/css" href="http://www.eionet.europa.eu/styles/eionet2007/handheld.css" media="handheld" />		
				<link rel="stylesheet" type="text/css" href="http://www.eionet.europa.eu/styles/eionet2007/screen.css" media="screen" title="Eionet 2007 style" />
				<link rel="stylesheet" type="text/css" href="eionet2007.css" media="screen" title="Eionet 2007 style"/>
				<link rel="alternate" type="application/rdf+xml" title="All Obligations" href="http://rod.eionet.europa.eu/obligations"/>
				<link rel="alternate" type="application/rdf+xml" title="All Localities" href="http://rod.eionet.europa.eu/countries"/>
				<link rel="alternate" type="application/rdf+xml" title="All Legal instruments" href="http://rod.eionet.europa.eu/instruments"/>
				<link rel="alternate" type="application/rss+xml" title="Obligation deadlines" href="http://rod.eionet.europa.eu/events.rss"/>
				<link rel="shortcut icon" href="favicon.ico" type="image/x-icon" />
				<script type="text/javascript" src="script/util.js"></script>
				<script type="text/javascript" src="script/pageops.js"></script>
                                <script type="text/javascript" src="script/mark_special_links.js"></script>
				<script type="text/javascript">
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
					
					function dpsirChkValue(chkBox, fldName) {
						var posV = "yes";
						var negV = "no";

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
							var mode = document.forms["f"].elements["dom-update-mode"];
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
						document.forms["f"].elements["dom-update-mode"].value="D";
						save(null,false);
					}

					function save(text,silent) {
						var i, j;
						//alert("save");
						var mode = document.forms["f"].elements["dom-update-mode"];
						var bDelete = (mode.value == 'D');

						for (i=0; i < compulsory.length; ++i) {
							obj = compulsory[i];
							if (obj[1].value.length == 0) {
								alert("Value for the " + obj[0] + " field is compulsory!");
								return false;
							}
						}
						
						//if (text == null) {
						//	text = (mode.value != 'D' ? "Save data?" 	: "Delete record?");
							//text = "Do you want to " + text;
						//}

						if (!silent)
							if(text != null && text != "")
								if (confirm(text) == false)
									return false;

						if (!bDelete) {
							// check once more all values before sending to server
							for (i = 0; i < document.forms["f"].length; ++i) {
								elem = document.forms["f"].elements[i];
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
							}//end-for document.forms["f"].elements
						   // select all options of the specified selects
							for (i = 0; i < selects.length; ++i) {
								selValues(selects[i][0]);
							}
						}//end-if !bDelete

						
						document.forms["f"].submit();
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

								var opt = document.createElement("option");
								selTo.add(opt);
								opt.text = newText;
								opt.value = newVal;
							}
						}
						
						// remove from	
						count = 0;
						for (i = selected.length-1; i >= 0; --i) {
							selFrom[selected[i]] = null;
						}
					}

					/**
					* KL
					*/
					function addFullValues(selFrom, selTo,clist,forSelTo) {
						var i, j, count = 0;
						var optsLen;
						var newVal, newText;
						
						isChanged = true;

						var selected = new Array();
						
						eu25 = { AT:1,BE:1,CY:1,CZ:1,
							 DE:1,DK:1,EE:1,ES:1,FI:1,FR:1,GB:1,GR:1,
							 HU:1,IE:1,IT:1,LT:1,LU:1,LV:1,MT:1,NL:1,
							 PL:1,PT:1,SE:1,SI:1,SK:1 }
						
						for (k = 0; k < selFrom.length; ++k) {
							if (selFrom[k].selected) {
								var str,u;
								u = null;
								str = new String(selFrom[k].value);
								if (str.indexOf(":") > -1) {
									u = str.substr(str.indexOf(":")+1);
									str2 = str.substring(0,str.indexOf(":"));
								}
								if(u != null && u.valueOf() == '0'){
									if(clist != null){
										for (z = 0; z < clist.length; z++) {
											s = clist[z].split(":");
											pvalue = s[0];
											ptext = s[1];
											ptype = s[2];
											ptwoletter = s[3];
											if (ptype.valueOf() == 'C' && ptwoletter.valueOf() in eu25) {
												val = str2.valueOf() +":"+ pvalue.valueOf();
												for (q = 0; q < selFrom.length; ++q) {
													if(selFrom[q].value == val.valueOf()){
														if (!selFrom[q].selected) {
															selFrom[q].selected = true;
														}
													}
												}
											}
										}
									}
									selFrom[k].selected = false;
								}
							}
						}

						for (i = 0; i < selFrom.length; ++i) {
							if (selFrom[i].selected) {
								var st,id;
								st = new String(selFrom[i].value);
								id = new String("");
								if (st.indexOf(":") > -1) {
									id = st.substr(st.indexOf(":")+1);
								} 
								
								var exists;
								exists = false;
								existsFormally = false;
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
								for (z = 0;z < forSelTo.length; ++z) {
									if (forSelTo[z].value == id.valueOf()) {
										existsFormally = true;
										break;
									}
								}
								if (!exists) {
									if(!existsFormally){
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
										var opt = document.createElement("option");
										selTo.add(opt);
										opt.text = newText;
										opt.value = newVal;
									} else {
										alert("       No country can be participating both formally and voluntarily." + '\n' + "To report it voluntarily you must first remove it from formally reporting list-box.")
									}
								}
							}
						}
					}

					function addClientValues(selFrom, selTo) {
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
									var opt = document.createElement("option");
									selTo.add(opt);
									opt.text = newText;
									opt.value = newVal;
								}
							}
						}
					}

					function addValues(selFrom, selTo, unit,clist,volSelTo) {
						var i, j, count = 0;
						var optsLen;
						var newVal, newText;
						
						isChanged = true;

						var selected = new Array();
						
						eu25 = { AT:1,BE:1,CY:1,CZ:1,
							 DE:1,DK:1,EE:1,ES:1,FI:1,FR:1,GB:1,GR:1,
							 HU:1,IE:1,IT:1,LT:1,LU:1,LV:1,MT:1,NL:1,
							 PL:1,PT:1,SE:1,SI:1,SK:1 }
						
						for (k = 0; k < selFrom.length; ++k) {
							if (selFrom[k].selected) {
								if(selFrom[k].value == '0'){
									if(clist != null){
										for (z = 0; z < clist.length; z++) {
											s = clist[z].split(":");
											pvalue = s[0];
											ptext = s[1];
											ptype = s[2];
											ptwoletter = s[3];
											if (ptype.valueOf() == 'C' && ptwoletter.valueOf() in eu25) {
												for (q = 0; q < selFrom.length; ++q) {
													if(selFrom[q].value == pvalue.valueOf()){
														if (!selFrom[q].selected) {
															selFrom[q].selected = true;
														}
													}
												}
											}
										}
									}
									selFrom[k].selected = false;
								}
							}
						}
						
						for (i = 0; i < selFrom.length; ++i) {
							if (selFrom[i].selected) {
								var exists;
								exists = false;
								existsVolunt = false;
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
								for (z = 0;z < volSelTo.length; ++z) {
									var st,id;
									st = new String(volSelTo[z].value);
									id = new String("");

									if (st.indexOf(":") > -1) {
										id = st.substr(st.indexOf(":")+1);
									} 

									if (id.valueOf() == selFrom[i].value) {
										existsVolunt = true;
										break;
									}
								}
								if (!exists) {
									if (!existsVolunt) {
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
										var opt = document.createElement("option");
										selTo.add(opt);
										opt.text = newText;
										opt.value = newVal;
									} else {
										alert("       No country can be participating both formally and voluntarily." + '\n' + "To report it formally you must first remove it from voluntarily reporting list-box.")
									}
								}
							}
						}
					}

					function addValuesEnv(selFrom, selTo, unit) {
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
									var opt = document.createElement("option");
									selTo.add(opt);
									opt.text = newText;
									opt.value = newVal;
								}
							}
						}
					}

					function delValues(selFrom) {
						for (i = selFrom.length-1; i >= 0; --i) {
						  if (selFrom[i].selected) {
							selFrom[i] = null;
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
						var opt = document.createElement("option");
						opt.text = "Choose a group";
						opt.value = "-1";
						list.options.add(opt);
						
						for (i = 0; i < picklist.length; i++) {
						  s = new String(picklist[i]);
						  pvalue = s.substring(0,s.indexOf(":"));
						  ptext = s.substring(s.indexOf(":")+1,s.lastIndexOf(":"));
						  ptype = s.substring(s.lastIndexOf(":")+1,s.lastIndexOf(":")+2);
						  if (ptype.valueOf() == type) {
							opt = document.createElement("option");
							opt.text = ptext.valueOf();
							opt.value = pvalue.valueOf();
							list.options.add(otp);
						  }
						} 
						list.options[0] = null;
						list.options[0].selected = true;
					}

					function fillclist(type,list,text) {
					      var i,js;
						for (i = list.length; i > 0; --i)
							list.options[i] = null;
						
						var opt = document.createElement("option");
						opt.text = "Choose a group";
						opt.value = "-1";
						list.options.add(opt);
						
						j = 1;
						for (i = 0; i < clist.length; i++) {
						  s = clist[i].split(":");
						  pvalue = s[0];
						  ptext = s[1];
						  ptype = s[2];
						  pismember = s[3];
						  if (ptype.valueOf() == type) {
							opt = document.createElement("option");
							opt.text = ptext.valueOf();
							opt.value = pvalue.valueOf();
							list.options.add(otp);
						  }
						} 
						list.options[0] = null;
						list.options[0].selected = true;
					}


					function fillMultilist(type,list,text) {
					      var i,js;
					      var opt;
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
							opt = document.createElement("option");
							opt.text = ptext.valueOf();
							opt.value = pvalue.valueOf();
							list.options.add(otp);
						  }
						} 
					}

					function warn(field, msg) {
						alert(msg);
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

					function checkAndSave(first, freq, next, textrep, to, terminate, client) {

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
						//EK 050215
						if (client.value== 0 || client.value.length==0){
							alert("'Report To' field is empty. Please make sure that the obligation is linked to at least one organisation.");
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
		<body onunload="checkStatus()">
			<div id="container">
				<div id="toolribbon">
				    <div id="lefttools">
				      <a id="eealink" href="http://www.eea.europa.eu/">EEA</a>
				      <a id="ewlink" href="http://ew.eea.europa.eu/">EnviroWindows</a>
				    </div>
				    <div id="righttools">    
						<xsl:choose>
							<xsl:when test="contains($admin,'true')='true'">
								<a id="logoutlink" href="logout_servlet" title="Log out">Logout <xsl:value-of select="$username"/></a>
							</xsl:when>
							<xsl:otherwise>
								<a>
									<xsl:attribute name="href">
										<xsl:value-of select="java:eionet.rod.EionetCASFilter.getCASLoginURL($req,false())"/>
									</xsl:attribute>
									<xsl:attribute name="title">Login</xsl:attribute>
									<xsl:attribute name="id">loginlink</xsl:attribute>
									Login
								</a>					    
							</xsl:otherwise>
						</xsl:choose>
						<xsl:call-template name="PageHelp"/>
						<a id="printlink" title="Print this page" href="javascript:this.print();"><span>Print</span></a>
						<a id="fullscreenlink" href="javascript:toggleFullScreenMode()" title="Switch to/from full screen mode"><span>Switch to/from full screen mode</span></a>
						<a id="acronymlink" href="http://www.eionet.europa.eu/acronyms" title="Look up acronyms"><span>Acronyms</span></a>
						<form action="http://search.eionet.europa.eu/search.jsp" method="get">
							<div id="freesrchform"><label for="freesrchfld">Search</label>
								<input type="text" id="freesrchfld" name="query"/>
								<input id="freesrchbtn" type="image" src="images/button_go.gif" alt="Go"/>
							</div>
						</form>
				    </div>
				</div> <!-- toolribbon -->

				<div id="pagehead">
				    <a href="/"><img src="images/eea-print-logo.gif" alt="Logo" id="logo" /></a>
				    <div id="networktitle">Eionet</div>
				    <div id="sitetitle">Reporting Obligations Database (ROD)</div>
				    <div id="sitetagline">This service is part of Reportnet</div>
				</div> <!-- pagehead -->


				<div id="menuribbon">
					<xsl:call-template name="DropDownMenu"/>
				</div>
				<xsl:call-template name="breadcrumbs"/>
				<xsl:call-template name="LeftToolbar">
					<xsl:with-param name="admin"><xsl:value-of select="$admin"/></xsl:with-param>
					<xsl:with-param name="username"><xsl:value-of select="$username"/></xsl:with-param>
					<xsl:with-param name="perm"><xsl:value-of select="$permissions"/></xsl:with-param>
				</xsl:call-template>
				<xsl:apply-templates select="XmlData"/>
			</div>
		</body>
   	 </html>
	</xsl:template>  
</xsl:stylesheet>

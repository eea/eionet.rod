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

/**
* Opens deliveries window
*/

function openDeliveries(ACT_ID, COUNTRY_ID){

	var url = "csdeliveries?ACT_DETAILS_ID=" + ACT_ID ; // + "&#038;mi6";
	url = url + "&COUNTRY_ID=" + escape(COUNTRY_ID);
	var name = "CSDeliveries";
	var features = "location=no, menubar=no, width=950, height=500, top=50, left=30, scrollbars=yes";
	var w = window.open( url, name, features);
	w.focus();
}


/**
* Link to Circa
*/
function openCirca(url){
	var name = "CSCIRCA";
	var features = "location=yes, menubar=yes, width=750, height=600, top=30, left=30, resizable=yes, SCROLLBARS=YES";
	var w = window.open( url, name, features);
	w.focus();
}


/**
*/
	function getRequestParameter(name) {
		var url = document.URL;
		var value="";

		i = url.indexOf(name + '=');
		len = name.length + 1;

		if (i > 0) {
			beg = url.substring(i+len);

			sStr= url.substring(i);
			j = sStr.indexOf('&');

			if (j > 0)
				value = url.substring(i+len,j);
			else
				value = url.substring(i+len);

		}
		else
			value="";
	
		
		//alert(value);
		return value;

	}
	function changeParamInUrl(sName, sValue){
		var sUrl, i, j,  sBeg, sEnd, sStr;
	
		sUrl = document.URL;
		i = sUrl.indexOf(sName + '=');
		if (i > 0) {
			sBeg=sUrl.substr(0, i); 
			sStr=sUrl.substr(i);
			j = sStr.indexOf('&');
			if (j > 0)
			   sEnd = sStr.substr(j);
			else
			   sEnd= '';

			sUrl=sBeg + sName + '=' + sValue + sEnd ;

			}
		else
			{

			j = sUrl.indexOf('?');
			if (j>0)
				sUrl = sUrl + '&' + sName + '=' + sValue;
			else
				sUrl = sUrl + '?' + sName + '=' + sValue;
			}
		return sUrl ;
		}


function openPrintable() {
		
	//	var mode = getRequestParameter("mode");
	var url;
	var name ="PrintROD";
	
	/*		
		if (mode != '')
			mode = "P" + mode ;
		else
			mode = "P";
	*/

	url = changeParamInUrl("printmode",  "Y");

	var features = "location=no, menubar=yes, width=700, height=600, top=50, left=100, scrollbars=yes, resizable=yes";
	var w = window.open(url,name,features);
	w.focus();

	//	window.open(url);

}

	function changeParamInString(sUrl, sName, sValue){
		var  i, j,  sBeg, sEnd, sStr;
		
		i = sUrl.indexOf(sName + '=');
		if (i > 0) {
			sBeg=sUrl.substr(0, i); 
			sStr=sUrl.substr(i);
			j = sStr.indexOf('&');
			if (j > 0)
			   sEnd = sStr.substr(j);
			else
			   sEnd= '';

			sUrl=sBeg + sName + '=' + sValue + sEnd ;

			}
		else
			{

			j = sUrl.indexOf('?');
			if (j>0)
				sUrl = sUrl + '&' + sName + '=' + sValue;
			else
				sUrl = sUrl + '?' + sName + '=' + sValue;
			}
		redirect(sUrl);
		}

	function redirect(url){
		document.location=url;
	}
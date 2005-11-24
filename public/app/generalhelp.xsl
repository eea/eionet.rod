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
 * The Original Code is "EINRC-5 / ROD Project".
 *
 * The Initial Developer of the Original Code is TietoEnator.
 * The Original Code code was developed for the European
 * Environment Agency (EEA) under the IDA/EINRC framework contract.
 *
 * Copyright (C) 2000-2002 by European Environment Agency.  All
 * Rights Reserved.
 *
 * Original Code: Ander Tenno (TietoEnator)
 * -->

<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0">
	<xsl:variable name="pagetitle">
		General Help
	</xsl:variable>
	<xsl:include href="ncommon.xsl"/>

<xsl:template name="breadcrumbs">
<div class="breadcrumbtrail">
 <div class="breadcrumbhead">You are here:</div>
 <div class="breadcrumbitem"><a href="http://www.eionet.eu.int">EIONET</a></div>
 <div class="breadcrumbitem"><a href="index.html">ROD</a></div>
 <div class="breadcrumbitemlast">General Help</div>
 <div class="breadcrumbtail">&#160;</div>
</div>
</xsl:template>

	<xsl:template match="XmlData">
		<div id="workarea">
		<xsl:value-of select="//HLP_AREA[AREA_ID='Help']/HTML" disable-output-escaping="yes"/>
<!--table cellspacing="0" cellpadding="0" width="590" border="0">
<tr><td>
<div class="head1">What is ROD?</div>
<p>ROD is the EEA’s reporting obligations database. It contains records describing environmental reporting obligations that countries have towards international organisations. ROD is part of Reportnet. Reportnet is group of web applications and processes developed by the EEA to support international environmental reporting.</p>

<div class="head1">What are reporting obligations?</div>
<p>Reporting obligations are requirement to provide information agreed between countries and international bodies such as the EEA or international conventions. Reporting obligations provide the basis for most environmental information flows.</p>
<p>A reporting obligation is taken to mean any requirement to provide specified information to a secretariat, coordinating body or governing body of a legal instrument, or their representatives, whether or not there is a prescribed format or fixed frequency, or whether or not the word "report" is used. Reporting obligations can be “compulsory”, “expected” or “voluntary” depending on whether information must be submitted in order to be in compliance with the legal instrument or is just submitted by countries at their own volition.</p>

<div class="head1">What does ROD cover?</div> 
<p>ROD includes all environmental reporting obligations that EEA member countries have towards DG environment, European marine conventions, Eurostat, OECD, UN, Unece, As well as the EEA itself.</p>
<p>Excluded from ROD are non-environmental reporting, requirements for proposals or the seeking of permits, requirements for specific actions to be taken (as opposed to reporting on actions taken), and administrative processes such as notifications of contact points, instances of issuance/withdrawal of permits (rather than reports that summarise permits issued), notifications of legal transposition and implementation measures.</p>

<div class="head1">Why is ROD needed?</div>
<p>Reporting obligations agreed between EU and international bodies and countries provide the framework for most environmental information flows. This framework has grown in Europe as reporting requirements for separate laws or sectors have been agreed to, often independently from one another. Duplication of efforts, together with a lack of a transparent, need-based approach to information flows, have contributed to poor response rates to many reporting obligations.
ROD provides an overview inventory of reporting obligations. The database can be used to:</p>
<ul>
	<li>Assist in the analysis of member countries reporting obligation's burden;</li>
	<li>Support member countries in planning and fulfilling reporting obligations;</li>
	<li>Assist in streamlining the flow of data to the EEA and other international organisations.</li>
</ul>

<div class="head1">Who are ROD users?</div>
<p>Rod is targeted towards three kinds of users:</p>
<ul>
	<li>People involved in environmental policy development who are gathering information about an environmental issue (what information is reported where)</li>
	<li>Country representatives who are involved in environmental reporting (when is reporting due and what do I need to do?)</li>
	<li>People involved in environmental reporting from the perspective of the international organisations receiving and making use of the data.</li>
</ul>

<div class="head1">How is the information structured?</div>
<p>The ROD database contains records at two levels. The upper level is the legislative instrument (LI) level. The lower level is the reporting obligation (RO) level. A legislative instrument record is the legal basis for one or more reporting obligations.</p>

<div class="head1">How can I use ROD?</div>
<p>Here are some examples of the kind of questions that ROD can help answer and how to find the information.</p>
<p>Question: When does Italy next have to report waste statistics to Eurostat?</p>
<p>On the ROD Front page, click Deadlines; click Italy; click Advanced search; select “waste” and “Eurostat”</p>
<p>Or</p>
<p>On the ROD Front page: select “Italy” from the countries list, select “Waste” from the issues list and “Eurostat” from the reporting to list. Click on the “Go” icon. Select the reporting obligation from the list.</p>
<p>Question: What does Helcom collect information on?</p>
<p>On the ROD Front page: select “Helcom” from the “reporting to” list. Click on the “Go” icon.</p>
<p>Question: What do countries have to report on GMOs?</p>
<p>On the ROD Front page: select “Various other issues” from the “issues” list. Click on the “Go” icon. Select links to the reporting obligations on GMOs from the list that appears.</p>
<p>Or</p>
<p>Search the contents of ROD using Google for “GMO”.</p>
<p>Question: What are the current reporting guidelines for the UNFCCC Greenhouse gas inventories?</p>
<p>On the ROD Front page: select “climate change” form the issues list, select “UNFCCC” from the “reporting to” list. Click on the “Go” icon. On the resulting list, click on the “Greenhouse gas inventories” reporting obligation to open the reporting obligation record page. Check that the record is valid in the footer. Select the link to the reporting guidelines.</p>

<div class="head1">Dates in ROD</div>
<p>There are two ways to specify reporting due dates in ROD: text based ("ASAP", etc.) and numeric date based.</p>
<p>Where possible numeric dates are used. This allows the deadlines to be sorted and used for notifications. If needed additional comments are added in the "dates comments" field. In some instances numeric dates cannot be used, so a text deadline is used.</p>
<p>For the numeric dates, the following guidelines are adhered to:</p>
<p>Reporting that is due in a year with no month or date specified is set as being due at on 31 December of the year. If the month is specified, then the reporting is set as being due on the last day in the month. The "dates comments" field may be used to indicate this with text such as "year only specified" or "Year and month only specified" as appropriate.</p>
<p>If reporting is due in time for a regular event, such as a 3 yearly conference of the parties (COP) to a convention, then a numeric date for the COP is normally be entered in the baseline date field, along with a frequency. A note in the date comments field will indicate something similar to "Reporting submitted prior to three yearly COP depending on exact dates of COP".</p>
<p>Some calendar rounding is automatically done in this calculation for leap years and so on. Deadlines that have just passed are also shown for a brief period following their expiry. The ROD application recalculates the "Next due date" fields for all obligations every day, so they are always up to date.</p>

<div class="head1">Where did the drive to create ROD come from?</div>
<p>The drive to create ROD came from four directions. The European Parliament and member country representatives to the EEA requested an overview of environmental reporting obligations.</p>
<p>The EU's Sixth Environmental Action Programme (6EAP) recognized the need to revise the reporting system in order to achieve a clearer specification of policy-relevant information needs to increase transparency and to remove overlaps.</p>
<p>The programme highlighted that solutions enabling a more transparent and efficient flow should be enabled, and that suitable IT developments should be exploited.
Lastly the EEA itself makes use of ROD to identify and gain access to suitable datasets for use in its assessments.</p>

<p><a href="text.jsv?mode=R">RSS XML/RPC data extraction help</a></p>
</td></tr>
</table-->

		</div>
		<xsl:call-template name="CommonFooter"/>
	</xsl:template>

</xsl:stylesheet>

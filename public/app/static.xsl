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

	<xsl:template name="PageTitle">
		EEA - Reporting Obligations Database
	</xsl:template>

	<xsl:template name="FirstHeading">
		Reporting Obligations Database (ROD)
	</xsl:template>

	<xsl:template name="SecondHeading">
		This service is part of Reportnet
<!--		Tracking obligations and performance for each country in Reporting -->
	</xsl:template>

	<xsl:template name="IntroductoryText">
		This database contains records describing environmental reporting obligations that countries 
		have towards the EEA, European Commission and international conventions. The reporting obligations arise from 
		numerous international legislative instruments and agreements. 
		ROD is part of the EEA's Reportnet suite of tools to streamline reporting obligations and facilitate 
		its work collecting, handling and presenting environmental information.	
	</xsl:template>

	<xsl:template name="IntroductoryTitle">
	Reporting obligations database (ROD)
	</xsl:template>

	<xsl:template name="Disclaimer">
	Disclaimer:  EEA does not guarantee that all possible obligations are shown nor that recorded obligations totally cover all aspects a country has to comply with regarding a specific obligation. If you find any inconsistencies in the database, please <a><xsl:attribute name="href">mailto:eea@eea.eu.int</xsl:attribute>report back</a> to EEA.
	</xsl:template>

	<xsl:template name="DB_RecognizedDefault">
	Recognized
	</xsl:template>
	
<!-- 
	**********
	From this point further you may find database content sensitive parameters.
	
		The first ID is needed to parse legal instruments hierarchy from root;
		all others are needed to list different obligations by type  on index page.
	
	**********
-->
	
	<xsl:template name="DB_Legal_Root_ID">1</xsl:template>

	<xsl:template name="DB_EUObligation_ID">14,25,2,3,4,5,6,7,8,9,10,11,12,13</xsl:template>

	<xsl:template name="DB_ConventionObligation_ID">15</xsl:template>

	<xsl:template name="DB_EEARequest_ID">21</xsl:template>

	<xsl:template name="DB_EurostatRequest_ID">22</xsl:template>
		
	<xsl:template name="DB_OtherRequest_ID">23</xsl:template>

	<xsl:template name="Feedback_URL">http://www.eionet.eu.int/software/rod/issues/add_issue_html</xsl:template>

	<xsl:template name="Disclaimer_URL">disclaimer.jsv</xsl:template>

</xsl:stylesheet>
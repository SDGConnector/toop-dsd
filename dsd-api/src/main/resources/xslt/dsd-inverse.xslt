<?xml version="1.0" encoding="UTF-8" standalone="no"?>
<!--

    Copyright (C) 2018-2020 toop.eu

    Licensed under the Apache License, Version 2.0 (the "License");
    you may not use this file except in compliance with the License.
    You may obtain a copy of the License at

            http://www.apache.org/licenses/LICENSE-2.0

    Unless required by applicable law or agreed to in writing, software
    distributed under the License is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
    See the License for the specific language governing permissions and
    limitations under the License.

-->
<xsl:stylesheet
    version="2.0"
    xmlns:xsl="http://www.w3.org/1999/XSL/Transform">

  <!--PROLOG-->
  <xsl:output indent="yes" method="xml" omit-xml-declaration="no" standalone="yes" xalan:indent-amount="2"
              xmlns:xalan="http://xml.apache.org/xslt"/>

  <xsl:template match="/">

    <resultlist version="1.0" result-page-count="1000" first-result-index="0"
                query-terms="identifierScheme=DataSubjectIdentifierScheme"
                result-page-index="0">

      <xsl:attribute name="creation-dt">
        <xsl:value-of select="current-dateTime()"/>
      </xsl:attribute>

      <xsl:attribute name="total-result-count">
        <xsl:value-of select="//@totalResultCount"/>
      </xsl:attribute>

      <xsl:attribute name="used-result-count">
        <xsl:value-of select="//@totalResultCount"/>
      </xsl:attribute>

      <xsl:attribute name="last-result-index">
        <xsl:value-of select="//@totalResultCount"/>
      </xsl:attribute>

      <xsl:for-each select="//*:RegistryObject">
        <xsl:variable name="dataset" select="./*:Slot/*:SlotValue/*:dataset"/>
        <match>
          <!-- dct:publisher/cbc:id schemeID="9999">elonia</cbc:id> -->
          <participantID scheme="iso6523-actorid-upis">
            <xsl:attribute name="scheme" select="$dataset/*:publisher/*:id/@schemeID"/>
            <xsl:value-of select="$dataset/*:publisher/*:id"/>
          </participantID>

          <!-- <dcat:distribution>/<dcat:accessService>/<dct:identifier> -->
          <docTypeID scheme="toop-doctypeid-qns">
            <xsl:value-of select="normalize-space($dataset/*:distribution/*:accessService/*:identifier)"/>
          </docTypeID>

          <entity>
            <name>
              <xsl:value-of select="$dataset/*:publisher/*:prefLabel"/>
            </name>
            <countryCode>
              <xsl:value-of select="$dataset/*:publisher/*:location/*:address/*:adminUnitLevel1"/>
            </countryCode>

            <!-- dct:publisher/org:classification -->
            <!-- NOTE: cannot derive identifier. Because we are losing the value -->
            <!--
            <identifier>
              <xsl:attribute name="scheme">
                <xsl:value-of select="$dataset/dct:publisher/org:classification"/>
              </xsl:attribute>
              ?madeupvalue?
            </identifier>-->

            <!-- NOTE: cannot derive the additional info like full address -->
          </entity>
        </match>
      </xsl:for-each>
    </resultlist>
  </xsl:template>
</xsl:stylesheet>

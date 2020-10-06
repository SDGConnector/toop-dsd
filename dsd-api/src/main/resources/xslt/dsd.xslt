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
    xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
    xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
    xmlns:xs="http://www.w3.org/2001/XMLSchema"
    xmlns:xalan="http://xml.apache.org/xslt"
    xmlns:xls="http://www.w3.org/1999/XSL/Transform"
    xmlns:org="http://www.w3.org/ns/org#">

  <!-- PARAMETERS -->
  <xsl:param name="datasetType"/>
  <xsl:param name="countryCode"/>
  <xsl:param name="dpType"/>

  <xsl:variable name="fictiveUrl">
    <xsl:value-of select="concat('https://smp.elonia.toop.eu/9999::Elonia/services/', $datasetType)"/>
  </xsl:variable>

  <!--PROLOG-->
  <xsl:output indent="yes" method="xml" omit-xml-declaration="no" standalone="yes" xalan:indent-amount="2"/>

  <!-- ROOT TEMPLATE -->
  <xsl:template match="/"
                xmlns:lcm="urn:oasis:names:tc:ebxml-regrep:xsd:lcm:4.0"
                xmlns:query="urn:oasis:names:tc:ebxml-regrep:xsd:query:4.0"
                xmlns:rim="urn:oasis:names:tc:ebxml-regrep:xsd:rim:4.0"
                xmlns:rs="urn:oasis:names:tc:ebxml-regrep:xsd:rs:4.0">
    <!-- Phase 1, prepare dataset -->
    <xsl:variable name="registryObjects">
      <rim:RegistryObjectList>
        <xsl:for-each select="resultlist/match">
          <!-- keep the current match in a variable -->
          <xsl:variable name="match" select="."/>

          <!-- One registry object per dataset / perdoctype -->
          <xsl:for-each select="docTypeID">
            <xsl:variable name="docTypeID">
              <!-- derive from doctype -->
              <xsl:choose>
                <xsl:when test="starts-with(normalize-space(.), 'toop-doctypeid-qns::')">
                  <xls:value-of select="substring-after(., 'toop-doctypeid-qns::')"/>
                  <xls:value-of select="position()"/>
                  Yes
                </xsl:when>
                <xsl:otherwise>
                  <xls:value-of select="normalize-space(.)"/>
                </xsl:otherwise>
              </xsl:choose>
            </xsl:variable>

            <!-- Filter doctypes based on dataset type-->
            <xsl:if test="contains($docTypeID, $datasetType)">
              <xsl:for-each select="$match/entity">
                <xsl:variable name="entity" select="."/>
                <xsl:if test="contains($entity, $countryCode)">
                  <xsl:variable name="nodeId">
                    <xsl:value-of select="generate-id()"/>
                  </xsl:variable>

                  <xsl:variable name="distributionFormat">
                    <!-- TODO: derive from doctype -->
                    <xsl:value-of select="concat('distributionFormat', 'distributionFormat')"/>
                  </xsl:variable>

                  <xsl:variable name="dataSetIdentifier">
                    <!-- TODO: derive from doctype -->
                    <xsl:value-of select="concat('dataSetIdentifier', 'dataSetIdentifier')"/>
                  </xsl:variable>


                  <rim:RegistryObject>

                    <xsl:attribute name="id">
                      <xsl:value-of select="$nodeId"/>
                    </xsl:attribute>

                    <rim:Slot name="Dataset">
                      <rim:SlotValue xsi:type="rim:AnyValueType">

                        <dcat:dataset xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
                                      xmlns:cagv="https://semic.org/sa/cv/cagv/agent-2.0.0#"
                                      xmlns:dct="http://purl.org/dc/terms/"
                                      xmlns:cbc="https://semic.org/sa/cv/common/cbc-2.0.0#"
                                      xmlns:skos="http://www.w3.org/2004/02/skos/core#"
                                      xmlns:dcat="http://data.europa.eu/r5r/"
                                      xmlns:locn="http://www.w3.org/ns/locn#">
                          <dct:description>
                            <xsl:value-of select="normalize-space(concat('A dataset about ', $datasetType))"/>
                          </dct:description>
                          <dct:title>?Companies registry?</dct:title>

                          <!-- Distribution Information -->
                          <xsl:comment>Distribution Information</xsl:comment>
                          <dcat:distribution>
                            <!--
                            @Sander:
                            The dataset/distribution/conformsTo property only needs to be used for STRUCTURED distribution,
                            i.e. XML document based on a pre-defined schema. I think we don’t use these kind of distributions,
                            so this element can be left out from the response.
                            -->
                            <!-- <dct:conformsTo>RegRepv4-EDMv2</dct:conformsTo> -->


                            <!--
                              @Sander:
                              I guess you can set a fictive SMP URL here for use in the pilots as the TOOP Connector
                              will do a BDXL lookup anyway, so there is no need to set the correct SMP URL here.
                            -->
                            <dcat:accessURL>
                              <xsl:value-of select="$fictiveUrl"/>
                            </dcat:accessURL>

                            <dct:description>
                              <xsl:value-of
                                  select="normalize-space(concat('?This is a pdf distribution of the ', $datasetType, '?'))"/>
                            </dct:description>


                            <!--
                              Deriving the value from doctype
                            -->
                            <dct:format>
                              <xsl:value-of select="$distributionFormat"/>
                            </dct:format>

                            <dcat:accessService>
                              <!-- Doctype -->
                              <dct:identifier>
                                <xsl:value-of select="$docTypeID"/>
                              </dct:identifier>

                              <dct:title>?Access Service Title?</dct:title>
                              <!--
                                @Sander:
                                I guess you can set a fictive SMP URL here for use in the pilots as the TOOP Connector
                                will do a BDXL lookup anyway, so there is no need to set the correct SMP URL here.
                              -->
                              <dcat:endpointURL>
                                <xsl:value-of select="$fictiveUrl"/>
                              </dcat:endpointURL>
                            </dcat:accessService>

                            <!--
                            @Sander:
                            This indeed is probably an information element only available in a full DSD implementation.
                            I see it is currently mandatory, but I would think this is only needed for non concept based
                            distributions, what do you think @Jerry, @Dimitrios Zeginis?
                            -->
                            <dcat:mediaType>?application/pdf?</dcat:mediaType>
                          </dcat:distribution>

                          <!--
                          @Sander:
                          I don’t think this value is used in the pilots, so you can set it to any URL.
                          -->
                          <dct:conformsTo>
                            <xsl:value-of select="concat('https://semantic-repository.toop.eu/ontology/', $datasetType)"/>
                          </dct:conformsTo>

                          <!-- same value as the id of the registry object -->
                          <dct:identifier>
                            <xsl:value-of select="$dataSetIdentifier"/>
                          </dct:identifier>

                          <!-- Publisher Information -->
                          <xsl:comment>Publisher Information</xsl:comment>

                          <dct:publisher xsi:type="cagv:PublicOrganizationType">

                            <!-- shouldbe equal to dptype -->
                            <org:classification>
                              <xsl:value-of select="$entity/identifier/@scheme"/>
                            </org:classification>

                            <!-- The Data Provider Information -->
                            <cbc:id>
                              <xsl:attribute name="schemeID">
                                <xsl:value-of select="substring-before($match/participantID, ':')"/>
                              </xsl:attribute>
                              <xsl:value-of select="substring-after($match/participantID, ':')"/>
                            </cbc:id>
                            <cagv:location>
                              <cagv:address>
                                <!--
                                  Unfortunately, There is no explicit address information in the directory results.
                                  So  temporarily including the entire entity as text
                                  -->
                                <locn:fullAddress>
                                  <xsl:value-of select="normalize-space($entity)"/>
                                </locn:fullAddress>
                                <!-- country code -->
                                <locn:adminUnitLevel1>
                                  <xsl:value-of select="$entity/countryCode"/>
                                </locn:adminUnitLevel1>

                              </cagv:address>
                            </cagv:location>
                            <!-- The Data Provider Information -->
                            <!-- the label is currently the Entity Name  (i.e. Elonia DEV) -->
                            <skos:prefLabel>
                              <xsl:value-of select="$entity/name"/>
                            </skos:prefLabel>
                          </dct:publisher>
                          <dct:type>
                            <xsl:value-of select="$datasetType"/>
                          </dct:type>
                          <dcat:qualifiedRelation>
                            <dct:relation>urn:oasis:names:tc:ebcore:partyid-type:iso6523:XXXX</dct:relation>
                            <dcat:hadRole>https://toop.eu/dataset/supportedIdScheme</dcat:hadRole>
                          </dcat:qualifiedRelation>
                        </dcat:dataset>
                      </rim:SlotValue>
                    </rim:Slot>
                  </rim:RegistryObject>
                </xsl:if> <!-- if test="contains($entity, $countryCode)"> -->
              </xsl:for-each> <!-- select="$match/entity"> -->
            </xsl:if> <!-- if test="contains($docTypeID, $datasetType)" -->
          </xsl:for-each> <!-- for-each select docTypeId -->
        </xsl:for-each> <!-- for-each select=resultlist/match" -->
      </rim:RegistryObjectList>
    </xsl:variable>
    <!-- end Phase 1 -->

    <!-- Phase 2 for updating the result count -->
    <query:QueryResponse xmlns="urn:oasis:names:tc:ebxml-regrep:xsd:lcm:4.0"
                         xmlns:lcm="urn:oasis:names:tc:ebxml-regrep:xsd:lcm:4.0"
                         xmlns:query="urn:oasis:names:tc:ebxml-regrep:xsd:query:4.0"
                         xmlns:rim="urn:oasis:names:tc:ebxml-regrep:xsd:rim:4.0"
                         xmlns:rs="urn:oasis:names:tc:ebxml-regrep:xsd:rs:4.0"
                         xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
                         xsi:schemaLocation="urn:oasis:names:tc:ebxml-regrep:xsd:lcm:4.0"
                         startIndex="0"
                         status="urn:oasis:names:tc:ebxml-regrep:ResponseStatusType:Success">

      <xsl:attribute name="totalResultCount">
        <xsl:value-of select="count($registryObjects/*/*)"/>
      </xsl:attribute>

      <xsl:copy-of select="$registryObjects"/>
    </query:QueryResponse>
    <!-- end Phase 2 -->

  </xsl:template>
</xsl:stylesheet>

<?xml version="1.0" encoding="UTF-8" standalone="no"?>
<!--

    Copyright (C) 2018-2021 toop.eu. All rights reserved.

    This project is dual licensed under Apache License, Version 2.0
    and the EUPL 1.2.

     = = = = = = = = = = = = = = = = = = = = = = = = = = = = = = = = = =

    Licensed under the Apache License, Version 2.0 (the "License");
    you may not use this file except in compliance with the License.
    You may obtain a copy of the License at

            http://www.apache.org/licenses/LICENSE-2.0

    Unless required by applicable law or agreed to in writing, software
    distributed under the License is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
    See the License for the specific language governing permissions and
    limitations under the License.

     = = = = = = = = = = = = = = = = = = = = = = = = = = = = = = = = = =

    Licensed under the EUPL, Version 1.2 or – as soon they will be approved
    by the European Commission - subsequent versions of the EUPL
    (the "Licence");
    You may not use this work except in compliance with the Licence.
    You may obtain a copy of the Licence at:

            https://joinup.ec.europa.eu/software/page/eupl

-->
<xsl:stylesheet
    version="2.0"
    xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
    xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
    xmlns:xs="http://www.w3.org/2001/XMLSchema"
    xmlns:xalan="http://xml.apache.org/xslt"
    xmlns:xls="http://www.w3.org/1999/XSL/Transform"
    xmlns:org="http://www.w3.org/ns/org#"
    xmlns:dsd="http://toop4eu/dsd" xmlns:x="http://www.w3.org/1999/XSL/Transform">

  <!-- PARAMETERS -->
  <xsl:param name="datasetType"/>
  <xsl:param name="countryCode"/>
  <xsl:param name="dpType"/>

  <xsl:variable name="fictiveUrl">
    <xsl:value-of select="concat('https://smp.elonia.toop.eu/9999::Elonia/services/', $datasetType)"/>
  </xsl:variable>


  <xsl:function name="dsd:docTypeV1">
    <xsl:param name="tokens"/>

    <xsl:variable name="namespaceURI" select="$tokens[1]"/>
    <xsl:variable name="token2" select="$tokens[2]"/>
    <xsl:if test="contains($token2, '##')=false()">
      <xsl:message terminate="yes">
        Invalid doctype
      </xsl:message>
    </xsl:if>

    <xsl:variable name="localElementName" select="substring-before($token2, '##')"/>
    <xsl:variable name="customizationId" select="substring-after($token2, '##')"/>
    <xsl:variable name="v1VersionField" select="$tokens[3]"/>

    <xsl:sequence>
      <docTypeParts>
        <dataSetIdentifier>
          <xsl:value-of select="concat($namespaceURI, '::', $localElementName)"/>
        </dataSetIdentifier>
        <datasetType>
          <xsl:value-of select="$namespaceURI"/>
        </datasetType>
        <distributionFormat>
          <xsl:value-of select="$localElementName"/>
        </distributionFormat>
        <distributionConformsTo>
          <xsl:value-of select="$customizationId"/>
        </distributionConformsTo>
        <conformsTo>
          <xsl:value-of select="$v1VersionField"/>
        </conformsTo>
      </docTypeParts>
    </xsl:sequence>
  </xsl:function>

  <xsl:function name="dsd:docTypeV2">
    <xsl:param name="tokens"/>

    <xsl:variable name="datasetIdentifier" select="$tokens[1]"/>
    <xsl:variable name="datasetType" select="$tokens[2]"/>
    <xsl:variable name="token3" select="$tokens[3]"/>

    <xsl:variable name="distributionFormat" select="if(contains($token3, '##')) then substring-before($token3, '##') else $token3"/>
    <xsl:variable name="distConformsTo" select="if(contains($token3, '##')) then substring-after($token3, '##') else ()"/>
    <xsl:variable name="conformsTo" select="$tokens[4]"/>

    <xsl:sequence>
      <docTypeParts>
        <dataSetIdentifier>
          <xsl:value-of select="$datasetIdentifier"/>
        </dataSetIdentifier>
        <datasetType>
          <xsl:value-of select="$datasetType"/>
        </datasetType>
        <distributionFormat>
          <xsl:value-of select="$distributionFormat"/>
        </distributionFormat>
        <distributionConformsTo>
          <xsl:value-of select="$distConformsTo"/>
        </distributionConformsTo>
        <conformsTo>
          <xsl:value-of select="$conformsTo"/>
        </conformsTo>
      </docTypeParts>
    </xsl:sequence>
  </xsl:function>

  <xsl:function name="dsd:getDocTypeParts">
    <xsl:param name="docType"/>
    <xsl:variable name="stripped"
                  select="if(starts-with($docType, 'toop-doctypeid-qns::')) then
                              substring-after($docType, 'toop-doctypeid-qns::')
                          else
                              $docType"/>
    <xsl:variable name="tokens" select="tokenize($stripped, '::')"/>
    <xsl:variable name="count" select="count($tokens)"/>
    <xsl:sequence select="if($count = 3) then dsd:docTypeV1($tokens) else dsd:docTypeV2($tokens)"/>
  </xsl:function>

  <!--PROLOG-->
  <xsl:output indent="yes" method="xml" omit-xml-declaration="no" standalone="yes" xalan:indent-amount="2"/>

  <!-- ROOT TEMPLATE -->
  <xsl:template match="/"
                xmlns:lcm="urn:oasis:names:tc:ebxml-regrep:xsd:lcm:4.0"
                xmlns:query="urn:oasis:names:tc:ebxml-regrep:xsd:query:4.0"
                xmlns:rim="urn:oasis:names:tc:ebxml-regrep:xsd:rim:4.0"
                xmlns:rs="urn:oasis:names:tc:ebxml-regrep:xsd:rs:4.0">

    <x:message>Param DpType:
      <xsl:value-of select="$dpType"/>
    </x:message>
    <x:message>Param Country Code:
      <xsl:value-of select="$countryCode"/>
    </x:message>
    <!-- Phase 1, prepare dataset -->
    <xsl:variable name="registryObjects">
      <rim:RegistryObjectList>
        <xsl:for-each select="resultlist/match">
          <!-- keep the current match in a variable -->
          <xsl:variable name="match" select="."/>

          <!-- One registry object per dataset / perdoctype -->
          <xsl:for-each select="docTypeID">
            <xsl:variable name="docTypeID" select="normalize-space(.)"/>
            <xsl:variable name="docTypeScheme" select="./@scheme"/>

            <!-- Filter doctypes based on dataset type-->
            <xsl:if test="contains($docTypeID, $datasetType)">
              <xsl:for-each select="$match/entity">
                <xsl:variable name="entity" select="."/>
                <xsl:if test="contains($entity/countryCode, $countryCode)">
                  <xsl:variable name="currentDataProviderType" select="$entity/identifier[normalize-space(@scheme)='DataProviderType']"/>
                  <xsl:if test="contains(string-join($currentDataProviderType/text(), ' '), $dpType)">
                    <xsl:variable name="nodeId">
                      <xsl:value-of select="generate-id()"/>
                    </xsl:variable>

                    <xsl:variable name="docTypeParts" select="dsd:getDocTypeParts($docTypeID)"/>
                    <xsl:variable name="accessServiceConformsTo" select="$docTypeParts//conformsTo"/>
                    <xsl:variable name="distributionFormat" select="$docTypeParts//distributionFormat"/>
                    <xsl:variable name="dataSetIdentifier" select="$docTypeParts//dataSetIdentifier"/>

                    <rim:RegistryObject>
                      <xsl:attribute name="id">
                        <xsl:value-of select="$nodeId"/>
                      </xsl:attribute>

                      <rim:Slot name="Dataset">
                        <rim:SlotValue xsi:type="rim:AnyValueType">

                          <dcat:dataset xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
                                        xmlns:cagv="https://semic.org/sa/cv/cagv/agent-2.0.0#"
                                        xmlns:dct="http://purl.org/dc/terms/"
                                        xmlns:cbc="https://data.europe.eu/semanticassets/ns/cv/common/cbc_v2.0.0#"
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
                                  <xsl:value-of select="concat($docTypeScheme, '::', $docTypeID)"/>
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
                                <dct:conformsTo>
                                  <xsl:value-of select="$accessServiceConformsTo"/>
                                </dct:conformsTo>
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
                              <!-- The Data Provider Information -->
                              <cbc:id>
                                <xsl:attribute name="schemeID">
                                  <xsl:value-of select="$match/participantID/@scheme"/>
                                </xsl:attribute>
                                <xsl:value-of select="$match/participantID"/>
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
                              <xsl:for-each select="$currentDataProviderType">
                                <org:classification>
                                  <skos:name>
                                    <xsl:value-of select="normalize-space(.)"/>
                                  </skos:name>
                                </org:classification>
                              </xsl:for-each>
                            </dct:publisher>
                            <dct:type>
                              <xsl:value-of select="$datasetType"/>
                            </dct:type>
                            <xsl:for-each select="$entity/identifier[normalize-space(@scheme)='DataSubjectIdentifierScheme']">
                              <dcat:qualifiedRelation>
                                <dct:relation>
                                  <xsl:value-of select="normalize-space(.)"/>
                                </dct:relation>
                                <dcat:hadRole>https://toop.eu/dataset/supportedIdScheme</dcat:hadRole>
                              </dcat:qualifiedRelation>
                            </xsl:for-each>

                          </dcat:dataset>

                        </rim:SlotValue>
                      </rim:Slot>
                    </rim:RegistryObject>

                  </xsl:if> <!-- if test="contains(schemes, dpType)"> -->
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

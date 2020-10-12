<%--
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


--%>

<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html>
    <head>
        <title>TOOP DSD</title>
        <link rel="stylesheet" href="css/bootstrap.min.css"/>
        <script src="js/jquery-3.5.1.min.js"></script>
    </head>
    <body>

        <div class="container">
            <div class="row">
                <div class="col">

                    <br/>
                    <h2 class="bd-title">Toop Data Services Directory</h2>

                    <h3>Sample Query</h3>

                    <form method="GET" action="rest/search">

                        <div class="form-group">
                            <label for="queryId">QueryId</label>
                            <select class="form-control" id="queryId" name="queryId" aria-describedby="queryIdHelp">
                                <option label="ByDatasetTypeAndLocation">urn:toop:dsd:ebxml-regrem:queries:ByDatasetTypeAndLocation</option>
                                <option label="ByDatasetTypeAndDPType">urn:toop:dsd:ebxml-regrem:queries:ByDatasetTypeAndDPType</option>
                            </select>
                        </div>

                        <div class="form-group">
                            <label for="dataSetType">DataSetType</label>
                            <input type="text" class="form-control" id="dataSetType" name="dataSetType" aria-describedby="DataSetTypeHelp"
                                   value="REGISTERED_ORGANIZATION_TYPE">
                            <small id="DataSetTypeHelp" class="form-text text-muted">This field is required</small>
                        </div>

                        <div class="form-group" id="countryGroup">
                            <label for="countryCode">Country Code</label>
                            <input type="text" class="form-control" id="countryCode" name="countryCode" aria-describedby="countryCodeHelp"
                                   value="SV">
                        </div>


                        <div class="form-group" id="dataProviderTypeGroup" style="display: none">
                            <label for="dataProviderType">Data Provider Type</label>
                            <input type="text" class="form-control" id="dataProviderType" aria-describedby="dataProviderTypeHelp"
                                   value="DataSubjectIdentifierScheme">
                        </div>

                        <button class="btn btn-primary" type="submit">Query</button>
                    </form>


                    <br/><br/>
                    <h3>Parameters</h3>
                    <b>Toop Directory Address: </b> <span> <%= eu.toop.dsd.config.DSDConfig.getToopDirUrl() %> </span> </br></br>
                    <p class="small">
                        <b>Version: </b> <span> <%= eu.toop.dsd.config.DSDConfig.getDsdVersion() %></span> </br>
                        <b>Build Date</b> <span><%= eu.toop.dsd.config.DSDConfig.getBuildDate() %> </span> </br>
                    </p>
                </div>
            </div>
        </div>
        <script>
          $(function () {
            $("#queryId").change(function () {
              console.log(this.value)

              if ("urn:toop:dsd:ebxml-regrem:queries:ByDatasetTypeAndLocation" === this.value) {
                $("#dataProviderType").removeAttr('name');
                $("#countryCode").attr('name', 'countryCode');
                $("#countryGroup").show();
                $("#dataProviderTypeGroup").hide();
              } else {
                $("#dataProviderType").attr('name', 'dataProviderType');
                $("#countryCode").removeAttr('name', 'countryCode');
                $("#countryGroup").hide();
                $("#dataProviderTypeGroup").show();
              }
            })
          })
        </script>
    </body>
</html>
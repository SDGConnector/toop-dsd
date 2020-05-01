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
                    <input type="text" class="form-control" id="queryId" name="queryId" aria-describedby="queryIdHelp"
                           value="urn:toop:dsd:ebxml-regrep:queries:DataSetRequest" readonly="readonly">
                    <small id="queryIdHelp" class="form-text text-muted">This field is readonly</small>
                </div>

                <div class="form-group">
                    <label for="dataSetType">DataSetType</label>
                    <input type="text" class="form-control" id="dataSetType" name="dataSetType" aria-describedby="DataSetTypeHelp"
                           value="REGISTERED_ORGANIZATION">
                    <small id="DataSetTypeHelp" class="form-text text-muted">This field is required</small>
                </div>

                <div class="form-group">
                    <label for="countryCode">Country Code</label>
                    <input type="text" class="form-control" id="countryCode" name="countryCode" aria-describedby="countryCodeHelp"
                           value="GQ">
                    <small id="countryCodeHelp" class="form-text text-muted">This field is optional</small>
                </div>

                <button class="btn btn-primary" type="submit">Query</button>
            </form>


            <br/><br/>
            <h3>Parameters</h3>
            <b>Toop Directory Address: </b> <span> <%= eu.toop.dsd.config.DSDConfig.getToopDirUrl() %> </span>
        </div>
    </div>
</div>
</body>
</html>
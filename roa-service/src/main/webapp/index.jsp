<%@ page import="eu.toop.roa.config.ROAConfig" %>
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
    <title>TOOP Registry of Authorities PoC</title>
    <link rel="stylesheet" href="css/bootstrap.min.css"/>
</head>
<body>

<div class="container">
    <div class="row">
        <div class="col">

            <br/>
            <h2 class="bd-title">Toop Registy of Authorities Proof of Concept</h2>

            <h3>Sample Query</h3>

            <form method="GET" action="rest/search">

                <div class="form-group">
                    <label for="queryId">QueryId</label>
                    <input type="text" class="form-control" id="queryId" name="queryId" aria-describedby="queryIdHelp"
                           value="urn:toop:roa:ebxml-regrep:queries:DataConsumerByProcedure" readonly="readonly">
                    <small id="queryIdHelp" class="form-text text-muted">This field is readonly</small>
                </div>

                <div class="form-group">
                    <label for="DataConsumerId">DataConsumerId</label>
                    <input type="text" class="form-control" id="DataConsumerId" name="DataConsumerId" aria-describedby="DataSetTypeHelp"
                           value="RE238912378">
                    <small id="DataConsumerIdHelp" class="form-text text-muted">This field is required</small>
                </div>

                <div class="form-group">
                    <label for="procedureId">Procedure Id</label>
                    <input type="text" class="form-control" id="procedureId" name="procedureId" aria-describedby="procedureIdHelp"
                           value="GBM_PROCEDURE">
                    <small id="procedureIdHelp" class="form-text text-muted">This field is optional</small>
                </div>

                <button class="btn btn-primary" type="submit">Query</button>
            </form>


            <br/><br/>
            <h3>Parameters</h3>
            <b>Toop Directory Address: </b> <span> <%= ROAConfig.getToopDirUrl() %> </span> </br></br>
            <p class="small">
                <b>Version: </b> <span> <%= ROAConfig.getRoaVersion() %></span> </br>
                <b>Build Date</b> <span><%= ROAConfig.getBuildDate() %> </span> </br>
            </p>
        </div>
    </div>
</div>
</body>
</html>
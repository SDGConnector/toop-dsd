/**
 * Copyright (C) 2018-2021 toop.eu
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *         http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package eu.toop.dsd.service;

import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.datatype.DatatypeConfigurationException;

import org.apache.http.HttpRequest;
import org.apache.http.HttpRequestFactory;
import org.apache.http.HttpStatus;
import org.apache.http.MethodNotSupportedException;
import org.apache.http.NameValuePair;
import org.apache.http.RequestLine;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.config.ConnectionConfig;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.DefaultBHttpServerConnectionFactory;
import org.apache.http.impl.DefaultHttpRequestFactory;
import org.apache.http.impl.io.DefaultHttpRequestParserFactory;
import org.apache.http.impl.io.DefaultHttpResponseWriterFactory;
import org.apache.http.io.HttpMessageParserFactory;
import org.apache.http.localserver.LocalServerTestBase;
import org.apache.http.message.BasicLineParser;
import org.junit.BeforeClass;
import org.junit.Test;

import eu.toop.dsd.api.DsdDataConverter;
import eu.toop.dsd.api.ToopDirClient;
import eu.toop.dsd.api.types.DSDQuery;
import eu.toop.dsd.client.DSDClient;
import eu.toop.edm.jaxb.dcatap.DCatAPDatasetType;
import eu.toop.edm.xml.dcatap.DatasetMarshaller;

/**
 * A test class that tests the DSDClient
 *
 * @author yerlibilgin
 */
public final class DSDClientTest {

  private static final int TEST_PORT = 25434;

  @BeforeClass
  public static void mocServer() throws Exception {
    MyLocalTestServer mlts = new MyLocalTestServer();
    mlts.setUp();
    mlts.start(TEST_PORT);
  }

  @Test
  public void testRawQuery() {
    String rawResult = new DSDClient("http://localhost:" + TEST_PORT).queryDatasetRawByLocation(
        "REGISTERED_ORGANIZATION_TYPE",
        "SV");

    System.out.println(rawResult);
  }

  /**
   * Test query
   *
   * @throws DatatypeConfigurationException the datatype configuration exception
   */
  @Test
  public void testQueryByLocation() {
    final List<DCatAPDatasetType> dcatList = new DSDClient("http://localhost:" + TEST_PORT).queryDatasetByLocation("REGISTERED_ORGANIZATION_TYPE",
        "SV");

    if (dcatList == null) {
      throw new IllegalStateException("Cannot parse Dataset, please check previous exceptions");
    }
    final DatasetMarshaller datasetMarshaller = new DatasetMarshaller();
    datasetMarshaller.setFormattedOutput(true);

    dcatList.forEach(dataset -> {
      System.out.println(datasetMarshaller.getAsString(dataset));
    });
  }


  @Test
  public void testQueryByDpType() {
    final List<DCatAPDatasetType> dcatList = new DSDClient("http://localhost:" + TEST_PORT).queryDatasetByDPType("REGISTERED_ORGANIZATION_TYPE",
        "abc");

    if (dcatList == null) {
      throw new IllegalStateException("Cannot parse Dataset, please check previous exceptions");
    }
    final DatasetMarshaller datasetMarshaller = new DatasetMarshaller();
    datasetMarshaller.setFormattedOutput(true);

    dcatList.forEach(dataset -> {
      System.out.println(datasetMarshaller.getAsString(dataset));
    });
  }

  private static class MyLocalTestServer extends LocalServerTestBase {
    public static final String TOOP_DIR_URL = "http://directory.acc.exchange.toop.eu";

    @Override
    public void setUp() throws Exception {
      super.setUp();
      System.out.println("Calling setup");
      HttpRequestFactory requestFactory = new DefaultHttpRequestFactory() {
        @Override
        public HttpRequest newHttpRequest(final RequestLine requestline) throws MethodNotSupportedException {
          return super.newHttpRequest(requestline);
        }
      };
      HttpMessageParserFactory<HttpRequest> requestParserFactory = new DefaultHttpRequestParserFactory(
          BasicLineParser.INSTANCE, requestFactory);
      DefaultBHttpServerConnectionFactory connectionFactory = new DefaultBHttpServerConnectionFactory(
          ConnectionConfig.DEFAULT, requestParserFactory, DefaultHttpResponseWriterFactory.INSTANCE);
      this.serverBootstrap.setConnectionFactory(connectionFactory);

      //register "/rest/search"
      System.out.println("Register handler");
      this.serverBootstrap.registerHandler("/rest/search", (request, response, context) -> {
        List<NameValuePair> parameters = null;

        System.out.println(request.getRequestLine());
        try {
          parameters = URLEncodedUtils.parse(new URI(
              request.getRequestLine().getUri()), StandardCharsets.UTF_8);


          Map<String, String> paramMap = new HashMap<>();
          for (NameValuePair nameValuePair : parameters) {
            System.out.println(nameValuePair.getName() + ": "
                + nameValuePair.getValue());
            paramMap.put(nameValuePair.getName(), nameValuePair.getValue());
          }

          String dataSetType = paramMap.get(DSDQuery.PARAM_NAME_DATA_SET_TYPE);
          String countryCode = paramMap.get(DSDQuery.PARAM_NAME_COUNTRY_CODE);
          String dpType = paramMap.get(DSDQuery.PARAM_NAME_DATA_PROVIDER_TYPE);

          final String resultXml;
          if (countryCode != null) {
            //query all the matches without a document type id.
            String directoryResult = ToopDirClient.callSearchApiWithCountryCode(TOOP_DIR_URL, countryCode);
            resultXml = DsdDataConverter.convertDIRToDSDWithCountryCode(directoryResult, dataSetType, countryCode);
          } else {
            String directoryResult = ToopDirClient.callSearchApiForDpType(TOOP_DIR_URL, dpType);
            resultXml = DsdDataConverter.convertDIRToDSDWithDPType(directoryResult, dataSetType, dpType);
          }
          response.setEntity(new StringEntity(resultXml, ContentType.APPLICATION_XML));
          response.setStatusCode(HttpStatus.SC_OK);
        } catch (Exception e) {
          e.printStackTrace();
          throw new IllegalStateException(e.getMessage(), e);
        }
      });
    }

    public void start(int port) throws Exception {
      this.serverBootstrap.setListenerPort(port);
      start();
    }
  }
}

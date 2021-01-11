/**
 * Copyright (C) 2018-2021 toop.eu. All rights reserved.
 *
 * This project is dual licensed under Apache License, Version 2.0
 * and the EUPL 1.2.
 *
 *  = = = = = = = = = = = = = = = = = = = = = = = = = = = = = = = = = =
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
 *
 *  = = = = = = = = = = = = = = = = = = = = = = = = = = = = = = = = = =
 *
 * Licensed under the EUPL, Version 1.2 or – as soon they will be approved
 * by the European Commission - subsequent versions of the EUPL
 * (the "Licence");
 * You may not use this work except in compliance with the Licence.
 * You may obtain a copy of the Licence at:
 *
 *         https://joinup.ec.europa.eu/software/page/eupl
 */
package eu.toop.dsd.api;

import org.junit.Test;

import java.io.IOException;

public class ToopDirClientTest {

  public static final String TOOP_DIR_URL = "http://directory.acc.exchange.toop.eu";

  @Test
  public void searchCountry() throws IOException {
    final String result = ToopDirClient.callSearchApiWithCountryCode(TOOP_DIR_URL, "SV");
    System.out.println(result);
  }

  @Test
  public void searchDpType() throws IOException {
    final String result = ToopDirClient.callSearchApiForDpType(TOOP_DIR_URL, "abc");
    System.out.println(result);
  }
}
/**
 * Copyright (C) 2018-2020 toop.eu
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package eu.toop.roa.main;

import eu.toop.roa.controller.AppComponent;
import eu.toop.roa.controller.DaggerAppComponent;
import eu.toop.roa.controller.RoaController;
import eu.toop.roa.controller.UIController;
import io.jooby.Jooby;
import io.jooby.ebean.EbeanModule;
import io.jooby.hikari.HikariModule;
import io.jooby.json.JacksonModule;
import io.jooby.thymeleaf.ThymeleafModule;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * The HTTP servlet for the REST query. One servlet could be used
 * for handling both the get and post requests, but it the specification
 * {http://docs.oasis-open.org/regrep/regrep-core/v4.0/os/regrep-core-rs-v4.0-os.html#__RefHeading__32747_422331532}
 * suggests using "/rest/search" for the rest based query, which would
 * be meaningless for handling a Post request that is not REST.
 *
 * @author yerlibilgin
 */
public class ROAMain extends Jooby {

  private static final Logger LOGGER = LoggerFactory.getLogger(ROAMain.class);

  {
    install(new HikariModule());

    install(new EbeanModule());

    install(new ThymeleafModule("/views/templates"));
    install(new JacksonModule());

    assets("/static/*", "/views/static");

    AppComponent dagger = DaggerAppComponent.builder()
        .build();

    LOGGER.info("Running TOOP Registry of Authorities V" + dagger.getRoaConfig().get().getRoaVersion());
    mvc(RoaController.class, dagger.getRoaController());
    mvc(UIController.class, dagger.getUIController());
  }

  public static void main(String[] args) {
    runApp(args, ROAMain::new);
  }
}

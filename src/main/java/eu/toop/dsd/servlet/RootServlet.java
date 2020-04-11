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
package eu.toop.dsd.servlet;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * The root servlet (/). Used as a welcome.
 * Might be replaced with a JSP later.
 *
 * @author Muhammet Yildiz
 */
@WebServlet("/")
public class RootServlet extends HttpServlet {
  private static final Logger LOGGER = LoggerFactory.getLogger(RootServlet.class);

  @Override
  protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
    LOGGER.debug("Someone is here!");

    resp.getOutputStream().println("<html>");
    resp.getOutputStream().println("  <head><title>TOOP DSD</title></head>");
    resp.getOutputStream().println("  <body><h1>Welcome to DSD</h1></body>");
    resp.getOutputStream().println("</html");
  }
}

package eu.toop.dsd;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;


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

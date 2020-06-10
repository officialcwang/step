package com.google.sps.servlets;

import com.google.appengine.api.users.UserService;
import com.google.appengine.api.users.UserServiceFactory;
import java.io.IOException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/login")
public class LoginServlet extends HttpServlet {
  @Override
  public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
    response.setContentType("text/html");
    UserService userService = UserServiceFactory.getUserService();

    if (userService.isUserLoggedIn()) {
      String logoutUrl = userService.createLogoutURL("/login");
      response.getWriter().println("<p>Logged in</p>");
      response.getWriter().println("<p>Log-out <a href=\"" + logoutUrl + "\">here</a>.</p>");
    } else {
      String loginUrl = userService.createLoginURL("/login");
      response.getWriter().println("<p>Logged out</p>");
      response.getWriter().println("<p>Log-in <a href=\"" + loginUrl + "\">here</a>.</p>");
    }
  }
}

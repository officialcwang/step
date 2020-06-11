package com.google.sps.servlets;

import com.google.appengine.api.users.UserService;
import com.google.appengine.api.users.UserServiceFactory;
import com.google.gson.Gson;
import java.io.IOException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/login")
public class LoginServlet extends HttpServlet {
  private Gson gson = new Gson();

  @Override
  public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
    response.setContentType("text/html");
    UserService userService = UserServiceFactory.getUserService();

    if (userService.isUserLoggedIn()) {
      String logoutUrl = userService.createLogoutURL("/login");
      response.getWriter().println("<p>Log-out <a href=\"" + logoutUrl + "\">here</a>.</p>");
    } else {
      String loginUrl = userService.createLoginURL("/login");
      response.getWriter().println("<p>Log-in <a href=\"" + loginUrl + "\">here</a>.</p>");
    }

    String json = gson.toJson(userService.getCurrentUser());
    response.getWriter().println(json);
  }
}

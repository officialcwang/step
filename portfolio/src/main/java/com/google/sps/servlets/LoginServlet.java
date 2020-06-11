package com.google.sps.servlets;

import com.google.appengine.api.users.UserService;
import com.google.appengine.api.users.UserServiceFactory;
import com.google.gson.Gson;
import com.google.sps.data.LoginInfo;
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
    String link = (userService.isUserLoggedIn()) ? userService.createLogoutURL("/professional.html")
                                                 : userService.createLoginURL("/professional.html");

    LoginInfo loginInfo = new LoginInfo(link, userService.getCurrentUser());
    String json = gson.toJson(loginInfo);
    response.getWriter().println(json);
  }
}

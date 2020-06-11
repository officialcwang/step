package com.google.sps.data;

import com.google.appengine.api.users.User;

/** Class containing server statistics. */
public final class LoginInfo {
  /** Log-in link if user is logged out, log-out link if the user is logged in.*/
  private String link;
  private User userInfo;

  public LoginInfo(String link, User userInfo) {
    this.link = link;
    this.userInfo = userInfo;
  }

  public String getLink() {
    return link;
  }

  public User getUserInfo() {
    return userInfo;
  }
}

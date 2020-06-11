package com.google.sps.data;

import com.google.appengine.api.users.User;

/** Class containing server statistics. */
public final class Comment {
  /** Log-in link if user is logged out, log-out link if the user is logged in.*/
  private String text;
  private String email;

  public Comment(String text, String email) {
    this.text = text;
    this.email = email;
  }

  public String getText() {
    return text;
  }

  public String getEmail() {
    return email;
  }
}

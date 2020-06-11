package com.google.sps.data;

import com.google.appengine.api.users.User;

/** Class containing a comment and its pertinent information. */
public final class Comment {
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

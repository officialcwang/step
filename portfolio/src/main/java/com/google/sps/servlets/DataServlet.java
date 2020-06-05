// Copyright 2019 Google LLC
//
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
//
//     https://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.

package com.google.sps.servlets;

import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.PreparedQuery;
import com.google.appengine.api.datastore.Query;
import com.google.appengine.api.datastore.Query.SortDirection;
import com.google.gson.Gson;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/** Servlet that returns some example content. TODO: modify this file to handle comments data */
@WebServlet("/data")
public class DataServlet extends HttpServlet {
  private List<String> comments;
  private Gson gson = new Gson();
  private static final String COMMENT = "Comment";
  private static final String TEXT = "text";
  private static final String TIMESTAMP = "timestamp";

  @Override
  public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
    Query query = new Query(COMMENT).addSort(TIMESTAMP, SortDirection.DESCENDING);

    DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
    PreparedQuery results = datastore.prepare(query);
    int numComments = getNumberOfComments(request, "comments-number");

    comments = new ArrayList<>();
    Iterator commentsIterator = results.asIterator();

    // Accept either all comments or as many as specified in numComments.
    for (int i = 0; i < numComments && commentsIterator.hasNext(); i++) {
      Entity entity = (Entity) commentsIterator.next();
      String output = (String) entity.getProperty(TEXT);
      comments.add(output);
    }

    String json = gson.toJson(comments);
    response.setContentType("text/html;");
    response.getWriter().println(json);
  }

  /**
   * Retrieve and store inputted comments and redirect the result to the page.
   */
  @Override
  public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
    // Get the input from the form.
    Optional<String> textOptional = getParameter(request, "text-input");
    String input = textOptional.orElse("");
    long timestamp = System.currentTimeMillis();

    // Respond with the result.
    String json = gson.toJson(input);
    comments.add(json);

    // Store the comment.
    Entity commentEntity = new Entity(COMMENT);
    commentEntity.setProperty(TEXT, input);
    commentEntity.setProperty(TIMESTAMP, timestamp);
    DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
    datastore.put(commentEntity);

    // Redirect back to the proper container.
    response.setContentType("text/html;");
    response.getWriter().println(json);
    response.sendRedirect("/index.html");
  }

  /**
   * @return the request parameter, or the default value if the parameter
   *         was not specified by the client
   */
  private Optional<String> getParameter(HttpServletRequest request, String name) {
    return Optional.ofNullable(request.getParameter(name));
  }

  /** Returns the choice entered by the player, or -1 if the choice was invalid. */
  private int getNumberOfComments(HttpServletRequest request, String name) {
    // Get the input from the form.
    String numCommentString = request.getParameter(name);

    // Convert the input to an int.
    int numComments;
    try {
      numComments = Integer.parseInt(numCommentString);
    } catch (NumberFormatException e) {
      System.err.println("Could not convert to int: " + numCommentString);
      return -1;
    }

    return numComments;
  }
}

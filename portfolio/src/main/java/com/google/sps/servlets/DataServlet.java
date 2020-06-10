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
import com.google.common.collect.Iterables;
import com.google.gson.Gson;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/** Servlet that returns some example content. */
@WebServlet("/data")
public class DataServlet extends HttpServlet {
  private List<String> comments;
  private Gson gson = new Gson();
  
  @Override
  public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
    Query query = new Query(Constants.COMMENT_KIND).addSort(Constants.TIMESTAMP_KIND, SortDirection.DESCENDING);

    DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
    PreparedQuery results = datastore.prepare(query);
    int numComments = getNumberOfComments(request, "comments-number");

    comments = new ArrayList<>();

    for (Entity entity : Iterables.limit(results.asIterable(), numComments)) {
      String output = (String) entity.getProperty(Constants.TEXT_KEY);
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

    // Respond with the result.
    String json = gson.toJson(input);
    comments.add(json);

    // Store the comment.
    Entity commentEntity = new Entity(Constants.COMMENT_KIND);
    commentEntity.setProperty(Constants.TEXT_KEY, input);
    commentEntity.setProperty(Constants.TIMESTAMP_KIND, System.currentTimeMillis());
    DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
    datastore.put(commentEntity);

    // Redirect back to the proper container.
    response.setContentType("text/html;");
    response.getWriter().println(json);
    response.sendRedirect("/professional.html");
  }

  /**
   * @return the request parameter, or the default value if the parameter
   *         was not specified by the client.
   */
  private Optional<String> getParameter(HttpServletRequest request, String name) {
    return Optional.ofNullable(request.getParameter(name));
  }

  /** Returns the number of comments to display, as selected by the user. */
  private int getNumberOfComments(HttpServletRequest request, String name) {
    // Convert the input to an int.
    return Integer.parseInt(request.getParameter(name));
  }
}

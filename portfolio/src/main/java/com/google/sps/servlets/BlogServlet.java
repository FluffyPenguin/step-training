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

import com.google.gson.Gson;
import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.PreparedQuery;
import com.google.appengine.api.datastore.Query;
import com.google.appengine.api.datastore.Query.SortDirection;

import java.io.IOException;
import java.util.List;
import java.util.ArrayList;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.ServletException;

/** Servlet that handles requests to the blog page */
@WebServlet("/blog")
public class BlogServlet extends HttpServlet {
  private List<String> comments;
  private Gson gson;
  private DatastoreService datastore;
  @Override
  public void init(){
    datastore = DatastoreServiceFactory.getDatastoreService();
    gson = new Gson();
    
    
  }
  @Override
  public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
    response.setContentType("text/html;");
    request.getRequestDispatcher("/data-comments").include(request, response);
    request.getRequestDispatcher("blog.jsp").forward(request, response);

    
    //response.getWriter().println(jsonComments);
  }
  @Override
  public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
    Entity commentEntity = new Entity("Comment");
    commentEntity.setProperty("timestamp",System.currentTimeMillis());
    commentEntity.setProperty("text", getParameter(request, "commentText"));
    datastore.put(commentEntity);
    response.sendRedirect("/");
  }

  /**
   * @param request the HttpServletRequest made by the client
   * @param name the name of the parameter to get from the request
   * @return the request parameter, or the default value if the parameter
   *         was not specified by the client
   * @throws IllegalArgumentException if the specified parameter name is not found
   */
  private String getParameter(HttpServletRequest request, String name) {
    String value = request.getParameter(name);
    if (value == null) {
      throw new IllegalArgumentException("Specified parameter not found.");
    }
    return value;
  }
  /**
   * @param request the HttpServletRequest made by the client
   * @param name the name of the parameter to get from the request
   * @return the request parameter, or the default value if the parameter
   *         was not specified by the client
   * @throws NumberFormatException if the parameter is not a number
   */
  private int getNumParameter(HttpServletRequest request, String name) {
    String value = request.getParameter(name);
    if (value == null) {
      throw new IllegalArgumentException("Specified parameter not found.");
    }
    return Integer.parseInt(value);
  }
}

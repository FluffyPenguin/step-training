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

import com.google.appengine.api.users.UserService;
import com.google.appengine.api.users.UserServiceFactory;
import com.google.appengine.api.users.User;

import java.io.IOException;
import java.util.List;
import java.util.ArrayList;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.ServletException;

/** Servlet that handles requests to the blog page */
@WebServlet("/createProfile")
public class CreateProfileServlet extends HttpServlet {
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
    UserService userService = UserServiceFactory.getUserService();
    if (userService.isUserLoggedIn()) {
      User user = userService.getCurrentUser();
      Query query =
        new Query("User")
            .setFilter(new Query.FilterPredicate("id", Query.FilterOperator.EQUAL, user.getUserId()));
      PreparedQuery results = datastore.prepare(query);
      Entity userEntity = results.asSingleEntity();
      if (userEntity != null) { //already has account
        response.sendRedirect("/");
        return;
      }
      //show page to make an account
      request.getRequestDispatcher("createProfile.jsp").include(request, response);
    } else {
      //shouldn't be here without being logged in
      response.sendRedirect("/");
    }
  }

  @Override
  public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
    UserService userService = UserServiceFactory.getUserService();
    if (userService.isUserLoggedIn()) {
      User user = userService.getCurrentUser();
      String userId = user.getUserId();
      Query query =
        new Query("User")
            .setFilter(new Query.FilterPredicate("id", Query.FilterOperator.EQUAL, userId));
      PreparedQuery results = datastore.prepare(query);
      if (results.asSingleEntity() != null) { //already has account
        response.sendRedirect("/");
        return;
      }
      //make User entity in datastore
      Entity userEntity = new Entity("User");
      userEntity.setProperty("id", userId);
      userEntity.setProperty("username", getParameter(request, "username"));
      datastore.put(userEntity);
    } 
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

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
import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.EntityNotFoundException;

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

import com.google.sps.data.Comment;

/** Servlet that handles comment data.*/
@WebServlet("/data-comments")
public class CommentDataServlet extends HttpServlet {
  private List<Comment> comments;
  private Gson gson;
  private DatastoreService datastore;
  @Override
  public void init(){
    datastore = DatastoreServiceFactory.getDatastoreService();
    gson = new Gson();
    
    
  }
  @Override
  public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException{
    //int maxNumComments = getNumParameter(request, "maxNumComments");
    int maxNumComments = 100;
    comments = new ArrayList<>(maxNumComments);
    Query query = new Query("Comment").addSort("timestamp", SortDirection.DESCENDING);
    PreparedQuery results = datastore.prepare(query);
    for (Entity commentEntity : results.asIterable()) {
      if (comments.size() == maxNumComments) {
        break;
      }
      Comment comment = new Comment(commentEntity);
      // String userName;
      // try {
      //   userName = (String) datastore.get(comment.getUserKey()).getProperty("username");
      // } catch (EntityNotFoundException e) {
      //   userName = "";
      // }
      comments.add(comment);
    }
    request.setAttribute("datastore", datastore);
    request.setAttribute("comments", comments);
    request.setAttribute("hello", "hi");
    // String jsonComments = gson.toJson(comments);
    // response.setContentType("application/json;");
    // response.getWriter().println(jsonComments);
  }
  @Override
  public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
    Entity userEntity = getUserEntity();
    if (userEntity == null) { //need to login or make account
      //send to login
      UserService userService = UserServiceFactory.getUserService();
      if (userService.isUserLoggedIn()) {
        response.sendRedirect("/createProfile");
      } else {
        response.sendRedirect(userService.createLoginURL("/createProfile"));
      }
      return;
    }
    Comment newComment = new Comment(userEntity.getKey(), getParameter(request,"commentText"));
    datastore.put(newComment.asEntity());
    response.sendRedirect(request.getHeader("referer"));
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
      //throw new IllegalArgumentException("Specified parameter not found.");
      return 100;
    }
    return Integer.parseInt(value);
  }
  /**
   * @return an entity object of the user if they are logged in and have registered an account
   *         null if the user has not logged in or has not registered an account
   */
  private Entity getUserEntity() {
    UserService userService = UserServiceFactory.getUserService();
    if (userService.isUserLoggedIn()) {
      User user = userService.getCurrentUser();
      String userId = user.getUserId();
      Query query =
        new Query("User")
            .setFilter(new Query.FilterPredicate("id", Query.FilterOperator.EQUAL, userId));
      PreparedQuery results = datastore.prepare(query);
      return results.asSingleEntity();
    }
    return null;
  }
}

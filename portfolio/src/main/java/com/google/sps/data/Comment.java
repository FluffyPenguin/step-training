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

package com.google.sps.data;


import com.google.gson.Gson;
import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.PreparedQuery;
import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.EntityNotFoundException;

/**
 * This class represts all the data that a comment consists of.
 */
public class Comment {
  private Key userKey;
  private String commentText;
  private long timestamp;
  /**
   * Constructs a comment with all parameters.
   * @param userKey the Key object of the user that posted the comment
   * @param commentText the text of the comment
   * @param timestamp the timestamp when the comment was posted in miliseconds
   */
  public Comment(Key userKey, String commentText, long timestamp) {
    this.userKey = userKey;
    this.commentText = commentText;
    this.timestamp = timestamp;
  }
  /**
   * Constructs a comment with the current time as its timestamp
   * @param userKey the Key object of the user that posted the comment
   * @param commentText the text of the comment
   */
  public Comment (Key userKey, String commentText) {
    this(userKey, commentText, System.currentTimeMillis());
  }
  /**
   * Constructs a comment with an entity object.
   * @param commentEntity entity of type "Comment" from the datastore
   */
  public Comment (Entity commentEntity) {
    this((Key) commentEntity.getProperty("userKey"),
          (String) commentEntity.getProperty("commentText"),
          (Long) commentEntity.getProperty("timestamp"));
  }
  /**
   * @return an Entity object representing this comment object
   */
  public Entity asEntity() {
    Entity commentEntity = new Entity("Comment");
    commentEntity.setProperty("userKey", userKey);
    commentEntity.setProperty("commentText", commentText);
    commentEntity.setProperty("timestamp", timestamp);
    return commentEntity;
  }

  public Key getUserKey() {
    return userKey;
  }

  public String getCommentText() {
    return commentText;
  }

  public long getTimestamp() {
    return timestamp;
  }
}
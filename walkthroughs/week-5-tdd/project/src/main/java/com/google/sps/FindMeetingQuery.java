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

package com.google.sps;

import java.util.Collection;
import java.util.Set;
import java.util.List;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.ArrayDeque;
import java.util.Queue;

public final class FindMeetingQuery {

  /**
   * @param events the collection of all events for that day
   * @param request a meeting request which includes the required and optional attendees and the length of the meeting
   * @return a collection of TimeRanges constituting all the available timeslots for the day.
   *         if there are optional attendees, then it returns the time slots where at least one optional attendee
   *         can attend as well unless there does not exist such time.
   *         If there is no time that includes optional attendees, the timeslots for only mandatory attendees are returned
   */
  public Collection<TimeRange> findAllAvailableTimeslots(Collection<Event> events, MeetingRequest request) {
    List<TimeRange> mandatoryTimes = (List<TimeRange>) queryMandatoryAttendees(events, request);
    if (request.getOptionalAttendees().size() == 0) {
      return mandatoryTimes;
    }
    List<TimeRange> newTimes = new ArrayList<>();
    for (String optAttendee : request.getOptionalAttendees()) {
      List<TimeRange> optAttendeeTimes = (List<TimeRange>) removeConflicts(events, mandatoryTimes, Arrays.asList(optAttendee));
      newTimes = combine(newTimes, optAttendeeTimes);
    }
    //remove time slots that are too short
    for (int i = 0; i < newTimes.size(); i++) {
      if (newTimes.get(i).duration() < request.getDuration()) {
        newTimes.remove(i--);
      }
    }
    if (newTimes.size() == 0) {
      if (request.getAttendees().size() == 0) {
        return newTimes;
      }
      return mandatoryTimes;
    }
    return newTimes;
  }
  /**
   * @return the combination of these two time ranges
   */
  private List<TimeRange> combine(List<TimeRange> times1, List<TimeRange> times2) {
    List<TimeRange> combinedTimes = new ArrayList<>(times1);
    for(TimeRange timeToAdd : times2) {
      boolean add = true;
      for (int i = 0; i < combinedTimes.size(); i++) {
        TimeRange existingTime = combinedTimes.get(i);
        if (existingTime.overlaps(timeToAdd)) {
          //four cases of overlapping
          if (existingTime.contains(timeToAdd.start()) && existingTime.contains(timeToAdd.end())) {
            //timeToAdd is completely inside existingTime -> do nothing
            add = false;
            break;
          } else if (timeToAdd.contains(existingTime.start()) && timeToAdd.contains(existingTime.end())) {
            //existingTime is completely inside timeToAdd -> remove existingTime, add timeToAdd
            //timeToAddTime:        |-|
            //openTime:     |---------|
            combinedTimes.remove(i);
            combinedTimes.add(timeToAdd);
            add = false;
            break;
          } else if (existingTime.contains(timeToAdd.start())) {
            //existingTime:     |-----|
            //timeToAdd:           |-----|
            //-> timeToAdd becomes the combination of both, remove existingTime, and reset i
            timeToAdd = TimeRange.fromStartEnd(existingTime.start(), timeToAdd.end(), false);
            combinedTimes.remove(i);
            i = -1;
          } else {
            //existingTime:       |-----|
            //timeToAdd:     |-----|
            //-> timeToAdd becomes the combination of both, remove existingTime, and reset i
            timeToAdd = TimeRange.fromStartEnd(timeToAdd.start(), existingTime.end(), false);
            combinedTimes.remove(i);
            i = -1;
          }
        }
      }
      if (add) {
        combinedTimes.add(timeToAdd);
      }
    }
    return combinedTimes;
  }
  /**
   * @return a Queue with the times that the attendees do not have conflicts with in times 
   */
  private Queue<TimeRange> removeConflicts(Collection<Event> events, Collection<TimeRange> times, Collection<String> attendees) {
    Queue<TimeRange> timesQueue = new ArrayDeque<>(times); //make a copy so we don't modify the original
    for (Event event : events) {
      if (hasOverlappingAttendendees(event.getAttendees(), attendees)) {
        Queue<TimeRange> adjTimesQueue = new ArrayDeque<>(timesQueue.size() * 2); //hold adjusted times. start size() *2 -> guarantee no resize
        TimeRange eventTime = event.getWhen();
        while (!timesQueue.isEmpty()) {
          TimeRange openTime = timesQueue.remove();
          //found an overlapping time in free time
          if (eventTime.overlaps(openTime)) {
            //four cases of overlapping
            if (eventTime.contains(openTime.start()) && eventTime.contains(openTime.end())) {
              //eventTime: |------------|
              //openTime:        |-|
              //openTime is completely inside evenTime -> remove it from the free times by not adding it
            } else if (openTime.contains(eventTime.start()) && openTime.contains(eventTime.end())) {
              //eventTime is completely inside openTime -> split openTime into two
              //evenTime:        |-|
              //openTime:     |---------|
              adjTimesQueue.add(TimeRange.fromStartEnd(openTime.start(), eventTime.start(), false));
              adjTimesQueue.add(TimeRange.fromStartEnd(eventTime.end(), openTime.end(), false));
            } else if (eventTime.contains(openTime.start())) {
              //eventTime:     |-----|
              //openTime:           |-----|
              //-> remove overlapping beginning of openTime
              adjTimesQueue.add(TimeRange.fromStartEnd(eventTime.end(), openTime.end(), false));
            } else {
              //eventTime:       |-----|
              //openTime:     |-----|
              //-> remove overlapping end of openTime
              adjTimesQueue.add(TimeRange.fromStartEnd(openTime.start(), eventTime.start(), false));
            }
          } else { //no overlap in times, no need to change it
            adjTimesQueue.add(openTime);
          }
        } //end while over timesQueue
        timesQueue = adjTimesQueue;
      }
    }
    return timesQueue;
  }
  /**
   * @return the available times for all mandatory attendees to meet
   */
  private Collection<TimeRange> queryMandatoryAttendees(Collection<Event> events, MeetingRequest request) {
    Queue<TimeRange> times = new ArrayDeque<>();
    if (request.getDuration() > 1440) {
      return times;
    }
    //asume all time is valid
    times.add(TimeRange.WHOLE_DAY);
    times = removeConflicts(events, times, request.getAttendees());
    Queue<TimeRange> fixedTimes = new ArrayDeque<>(times.size());
    //remove time slots that are too short
    while (!times.isEmpty()) {
      TimeRange time = times.remove();
      if (!times.get(i).duration() < request.getDuration()) {
        fixedTimes.add(time);
      }
    }
    return fixedTimes;
  }
  /*
   * @return True if there is at least one attendee that appears in both eventAttendees and requestAttendees
   * False if there are no overlap in attendees between both sets.
   */
  private boolean hasOverlappingAttendendees(Set<String> eventAttendees, Iterable<String> requestAttendees) {
    for (String requestAttendee : requestAttendees) {
      if (eventAttendees.contains(requestAttendee)) {
        return true;
      }
    }
    return false;
  }
}

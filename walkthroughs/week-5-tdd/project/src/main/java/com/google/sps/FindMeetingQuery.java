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

public final class FindMeetingQuery {

  /**
   * @return the available times for all mandatory attendees to meet while somewhat considering
   *         optional attendees. Only considers one optional attendee at a time.
   */
  public Collection<TimeRange> query(Collection<Event> events, MeetingRequest request) {
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
    List<TimeRange> combinedTimes = new ArrayList(times1);
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
   * @return the times that the attendees do not have conflicts with in times
   */
  private Collection<TimeRange> removeConflicts(Collection<Event> events, List<TimeRange> times, Collection<String> attendees) {
    times = new ArrayList(times); //make a copy so we don't modify the original
    for (Event event : events) {
      if (hasOverlappingAttendendees(event.getAttendees(), attendees)) {
        TimeRange eventTime = event.getWhen();
        for (int i = 0; i < times.size(); i++) {
          TimeRange openTime = times.get(i);
          //found an overlapping time in free time
          if (eventTime.overlaps(openTime)) {
            //four cases of overlapping
            if (eventTime.contains(openTime.start()) && eventTime.contains(openTime.end())) {
              //eventTime:
              //openTime is completely inside evenTime -> remove it from the free times
              times.remove(i--);
            } else if (openTime.contains(eventTime.start()) && openTime.contains(eventTime.end())) {
              //eventTime is completely inside openTime -> split openTime into two
              //evenTime:        |-|
              //openTime:     |---------|
              times.remove(i--);
              times.add(TimeRange.fromStartEnd(openTime.start(), eventTime.start(), false));
              times.add(TimeRange.fromStartEnd(eventTime.end(), openTime.end(), false));
            } else if (eventTime.contains(openTime.start())) {
              //eventTime:     |-----|
              //openTime:           |-----|
              //-> remove overlapping beginning of openTime
              times.set(i, TimeRange.fromStartEnd(eventTime.end(), openTime.end(), false));
            } else {
              //eventTime:       |-----|
              //openTime:     |-----|
              //-> remove overlapping end of openTime
              times.set(i, TimeRange.fromStartEnd(openTime.start(), eventTime.start(), false));
            }
          }
        }
      }
    }
    return times;
  }
  /**
   * @return the available times for all mandatory attendees to meet
   */
  private Collection<TimeRange> queryMandatoryAttendees(Collection<Event> events, MeetingRequest request) {
    List<TimeRange> times = new ArrayList<>();
    if (request.getDuration() > 1440) {
      return times;
    }
    //asume all time is valid
    times.add(TimeRange.WHOLE_DAY);
    times = (List<TimeRange>) removeConflicts(events, times, request.getAttendees());
    //remove time slots that are too short
    for (int i = 0; i < times.size(); i++) {
      if (times.get(i).duration() < request.getDuration()) {
        times.remove(i--);
      }
    }
    return times;
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

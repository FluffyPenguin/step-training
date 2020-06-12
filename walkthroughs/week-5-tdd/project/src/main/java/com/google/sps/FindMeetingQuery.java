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

public final class FindMeetingQuery {
  public Collection<TimeRange> query(Collection<Event> events, MeetingRequest request) {
    List<TimeRange> times = new ArrayList<>();
    if (request.getDuration() > 1440) {
      return times;
    }
    //asume all time is valid
    times.add(TimeRange.WHOLE_DAY);
    for (Event event : events) {
      if (hasOverlappingAttendendees(event.getAttendees(), request.getAttendees())) {
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
  private boolean hasOverlappingAttendendees(Set<String> eventAttendees, Collection<String> requestAttendees) {
    for (String requestAttendee : requestAttendees) {
      if (eventAttendees.contains(requestAttendee)) {
        return true;
      }
    }
    return false;
  }
}

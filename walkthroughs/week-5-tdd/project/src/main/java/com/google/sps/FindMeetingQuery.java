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

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;

public final class FindMeetingQuery {

  /**
   * Given the meeting information,
   * finds the times when the meeting could happen that day.
   */
  public Collection<TimeRange> query(Collection<Event> events, MeetingRequest request) {
    // Create a list of the TimeRanges when 
    // the attendees of the MeetingRequest are busy.
    ArrayList<TimeRange> busyTimes = new ArrayList<>();

    // If an attendee of the MeetingRequest is specified as 
    // one of the attendees of the other events,
    // add the TimeRange of the event to the list of busy times.
    for (Event event : events) {
      for (String attendee : request.getAttendees()) {
        if (event.getAttendees().contains(attendee)) {
          busyTimes.add(event.getWhen());
          break;
        }
      }
    }

    Collections.sort(busyTimes, TimeRange.ORDER_BY_START);
    
    // When the first meeting could start.
    int startTime = TimeRange.START_OF_DAY;
    ArrayList<TimeRange> freeTimes = new ArrayList<>();

    // Find gaps between events to schedule the meeting.
    for (TimeRange busy : busyTimes) {
      // If the meeting can be held between the start of the meeting and
      // the beginning of the next event (when the attendee will be busy). 
      if (busy.start() - startTime >= request.getDuration()) {
        freeTimes.add(TimeRange.fromStartEnd(startTime, busy.start(), /* inclusive */ false));
      }
      if (startTime < busy.end()) {
        startTime = busy.end();
      }
    }

    if (TimeRange.END_OF_DAY - startTime >= request.getDuration()) {
      freeTimes.add(TimeRange.fromStartEnd(startTime, TimeRange.END_OF_DAY, /* inclusive */ true));
    }

    return freeTimes;
  }
}

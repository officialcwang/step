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
    int startTime = TimeRange.START_OF_DAY;

    ArrayList<TimeRange> busyTimes = new ArrayList<>();
    for (Event event : events) {
      for (String attendee : request.getAttendees()) {
        if (event.getAttendees().contains(attendee)) {
          busyTimes.add(event.getWhen());
          break;
        }
      }
    }

    ArrayList<TimeRange> freeTimes = new ArrayList<>();
    Collections.sort(busyTimes, TimeRange.ORDER_BY_START);

    for (TimeRange busy : busyTimes) {
      if (busy.start() - startTime >= request.getDuration()) {
        freeTimes.add(TimeRange.fromStartEnd(startTime, busy.start(), false));
      }
      if (startTime < busy.end()) {
        startTime = busy.end();
      }
    }

    int endOfDay = TimeRange.END_OF_DAY;
    if (endOfDay - startTime >= request.getDuration()) {
      freeTimes.add(TimeRange.fromStartEnd(startTime, endOfDay, true));
    }

    return freeTimes;
  }
}

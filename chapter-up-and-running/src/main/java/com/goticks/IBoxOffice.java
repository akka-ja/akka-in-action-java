package com.goticks;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

interface IBoxOffice {
  // メッセージプロトコルの定義
  class CreateEvent extends AbstractMessage {
    private final String name;
    private final int tickets;

    public CreateEvent(String name, int tickets) {
      this.name = name;
      this.tickets = tickets;
    }

    public String getName() {
      return name;
    }

    public int getTickets() {
      return tickets;
    }
  }

  class GetEvent extends AbstractMessage {
    private final String name;

    public GetEvent(String name) {
      this.name = name;
    }

    public String getName() {
      return name;
    }
  }

  class GetEvents extends AbstractMessage {
  }

  class GetTickets extends AbstractMessage {
    private final String event;
    private final int tickets;

    public GetTickets(String event, int tickets) {
      this.event = event;
      this.tickets = tickets;
    }

    public String getEvent() {
      return event;
    }

    public int getTickets() {
      return tickets;
    }

  }

  class CancelEvent extends AbstractMessage {
    private final String name;

    public CancelEvent(String name) {
      this.name = name;
    }

    public String getName() {
      return name;
    }
  }

  class Event extends AbstractMessage {
    private final String name;
    private final int tickets;

    public Event(String name, int tickets) {
      this.name = name;
      this.tickets = tickets;
    }

    public String getName() {
      return name;
    }

    public int getTickets() {
      return tickets;
    }
  }

  class Events extends AbstractMessage {
    private final List<Event> events;

    public Events(List<Event> events) {
      this.events = Collections.unmodifiableList(new ArrayList<>(events));
    }

    public List<Event> getEvents() {
      return events;
    }
  }

  abstract class EventResponse extends AbstractMessage {
  }

  class EventCreated extends EventResponse {
    private final Event event;

    public EventCreated(Event event) {
      this.event = event;
    }

    public Event getEvent() {
      return event;
    }
  }

  class EventExists extends EventResponse {
  }
}

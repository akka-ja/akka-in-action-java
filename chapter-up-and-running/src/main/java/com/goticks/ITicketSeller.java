package com.goticks;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

interface ITicketSeller {
  // メッセージプロトコルの定義
  class Add extends AbstractMessage {
    private final List<Ticket> tickets;

    public Add(List<Ticket> tickets) {
      this.tickets = Collections.unmodifiableList(new ArrayList<>(tickets));
    }

    public List<Ticket> getTickets() {
      return tickets;
    }
  }

  class Ticket extends AbstractMessage {
    private final int id;

    public Ticket(int id) {
      this.id = id;
    }

    public int getId() {
      return id;
    }
  }

  class Tickets extends AbstractMessage {
    private final String event;
    private final List<Ticket> entries;

    public Tickets(String event, List<Ticket> entries) {
      this.event = event;
      this.entries = Collections.unmodifiableList(new ArrayList<>(entries));
    }

    public Tickets(String event) {
      this.event = event;
      this.entries = new ArrayList<>();
    }

    public String getEvent() {
      return event;
    }

    public List<Ticket> getEntries() {
      return entries;
    }
  }

  class Buy extends AbstractMessage {
    private final int tickets;

    public Buy(int tickets) {
      this.tickets = tickets;
    }

    public int getTickets() {
      return tickets;
    }
  }

  class GetEvent extends AbstractMessage {
  }

  class Cancel extends AbstractMessage {
  }
}

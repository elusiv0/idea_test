package util;

import model.Ticket;
import model.Tickets;
import parser.Parser;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class Calculations {
    private Parser parser;

    public Calculations(Parser parser) {
        this.parser = parser;
    }

    public List<Ticket> getTickets() throws Exception {
        return parser.parseTickets();
    }

    public List<Ticket> filterByPlaces(List<Ticket> tickets, String origin, String destination) {
        List<Ticket> filtered = tickets.stream()
                .filter((ticket) -> ticket.getDestination().equals(destination) && ticket.getOrigin().equals(origin))
                .collect(Collectors.toList());

        return filtered;
    }

    public long findMinDuration(List<Ticket> ticketList) {
        long min = Integer.MAX_VALUE;

        for (Ticket ticket : ticketList) {
            min = Math.min(min, ticket.getDurationMillis());
        }

        return min;
    }

    public double differenceBetweenAverageAndMedian(List<Ticket> ticketList) {
        long sum = 0;

        for (Ticket ticket : ticketList) {
            sum+=ticket.getPrice();
        }

        Collections.sort(ticketList, Comparator.comparingInt(Ticket::getPrice));

        double median = 0;
        int n = ticketList.size();
        if (n % 2 == 0) {
            median = ticketList.get(n/2).getPrice() + ticketList.get((n/2) -1).getPrice();
            median/=2.0;
        } else {
            median = ticketList.get(n/2).getPrice()/1.0;
        }

        double avg = sum /1.0/ticketList.size();

        return Math.abs(avg - median);
    }
}

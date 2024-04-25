package util;

import model.Ticket;
import model.Tickets;
import parser.Parser;

import java.util.*;
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

    public Map<String, Long> findMinDuration(List<Ticket> ticketList) {
        Map<String, Long> mins = new HashMap<>();

        for (Ticket ticket : ticketList) {
            String carrier = ticket.getCarrier();
            if (mins.containsKey(carrier)) {
                mins.put(carrier, Math.min(mins.get(carrier), ticket.getDurationMillis()));
                continue;
            }
            mins.put(carrier, ticket.getDurationMillis());
        }

        return mins;
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

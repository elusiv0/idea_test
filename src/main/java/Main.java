import com.fasterxml.jackson.databind.ObjectMapper;
import model.Ticket;
import parser.Parser;

import java.io.File;
import java.io.InputStream;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class Main {

    public static void main(String... args) throws Exception {
        String filePath = "tickets.json";
        Parser parse = new Parser();
        try {
            List<Ticket> ticketList = parse.parseTickets(filePath);
            long minDuration = findMinDuration(ticketList);
            double diffAvgMed = differenceBetweenAverageAndMedian(ticketList);

            System.out.printf("Minimal flight duration: %s\nDifference between median and average: %.5f",
                                String.format("%d mins", TimeUnit.MILLISECONDS.toMinutes(minDuration)),
                                diffAvgMed
                    );


        }
        catch (Exception ex) {
            System.out.println(ex.getMessage());
        }
    }

    public static long findMinDuration(List<Ticket> ticketList) {
        long min = Integer.MAX_VALUE;

        for (Ticket ticket : ticketList) {
            min = Math.min(min, ticket.getDurationMillis());
        }

        return min;
    }

    public static double differenceBetweenAverageAndMedian(List<Ticket> ticketList) {
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

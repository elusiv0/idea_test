import com.fasterxml.jackson.databind.ObjectMapper;
import loader.ResourceLoader;
import model.Ticket;
import parser.Parser;
import util.Calculations;

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
        String origin = "VVO";
        String destination = "TLV";
        ObjectMapper objectMapper = new ObjectMapper();
        ResourceLoader resourceLoader = new ResourceLoader(filePath);
        Parser parser = new Parser(objectMapper, resourceLoader);
        Calculations calculations = new Calculations(parser);
        List<Ticket> tickets = null;
        try {
            tickets = parser.parseTickets();
        }
        catch (Exception ex) {
            System.out.println(ex.getMessage());
        }

        List<Ticket> filtered = calculations.filterByPlaces(tickets, origin, destination);
        long minDuration = calculations.findMinDuration(filtered);
        double diffAvgMed = calculations.differenceBetweenAverageAndMedian(filtered);
        StringBuilder minDurationAnswer = new StringBuilder();
        minDuration = TimeUnit.MILLISECONDS.toMinutes(minDuration);
        if (minDuration > 59) {
            minDurationAnswer.append(String.format("%d hours ", (minDuration / 60)));
        }
        if (minDuration % 60 != 0) {
            minDurationAnswer.append(String.format("%d mins", minDuration % 60));
        }

        System.out.printf("Minimal flight duration: %s\nDifference between median and average: %.3f", minDurationAnswer.toString(), diffAvgMed);


    }

}

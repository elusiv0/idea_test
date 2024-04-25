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
import java.util.*;
import java.util.concurrent.TimeUnit;

public class Main {
    private static final String ORIGIN_DEFAULT = "VVO";
    private static final String DESTINATION_DEFAULT = "TLV";
    private static final String FILE_PATH = "tickets.json";

    public static void main(String[] args) throws Exception {
        String origin = ORIGIN_DEFAULT;
        String destination = DESTINATION_DEFAULT;
        if (args.length == 2) {
            origin = args[0];
            destination = args[1];
        }

        ObjectMapper objectMapper = new ObjectMapper();
        ResourceLoader resourceLoader = new ResourceLoader(FILE_PATH);
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
        Map<String, Long> minDurations = calculations.findMinDuration(filtered);
        if (minDurations.size() == 0) {
            System.out.printf("No flights from %s to %s", origin, destination);
            return;
        }
        double diffAvgMed = calculations.differenceBetweenAverageAndMedian(filtered);
        StringBuilder minDurationAnswer = new StringBuilder();

        for (Map.Entry<String, Long> entry : minDurations.entrySet()) {
            long minDuration = TimeUnit.MILLISECONDS.toMinutes(entry.getValue());
            minDurationAnswer.append(String.format("%s minimal flight duration: ", entry.getKey()));
            if (minDuration > 59) {
                minDurationAnswer.append(String.format("%d hours ", (minDuration / 60)));
            }
            if (minDuration % 60 != 0) {
                minDurationAnswer.append(String.format("%d mins", minDuration % 60));
            }

            minDurationAnswer.append("\n");
        }

        System.out.printf("%s to %s:\n\nMinimal flight durations by companies:\n%s\nDifference between median and average: %.3f", origin, destination, minDurationAnswer.toString(), diffAvgMed);


    }

}

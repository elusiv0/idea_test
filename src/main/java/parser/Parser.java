package parser;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.exc.StreamReadException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DatabindException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import loader.ResourceLoader;
import model.Ticket;
import model.Tickets;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

public class Parser {
    private ObjectMapper objectMapper;
    private ResourceLoader resourceLoader;

    public Parser(ObjectMapper objectMapper, ResourceLoader resourceLoader) {
        this.objectMapper = objectMapper;
        this.resourceLoader = resourceLoader;
        objectMapper.registerModule(new JavaTimeModule());
    }

    public List<Ticket> parseTickets() throws IllegalArgumentException, IOException, DatabindException, StreamReadException {
        InputStream inputStream = resourceLoader.getResourceInputStream();

        Tickets tickets = objectMapper.readValue(inputStream, Tickets.class);

        return tickets.getTickets();
    }
}

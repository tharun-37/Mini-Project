package services;

import models.Alumnus;
import models.Event;
import models.Message;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class DataManager {
    private static final String ALUMNI_FILE = "alumni.json";
    private static final String EVENTS_FILE = "events.json";
    private static final String MESSAGES_FILE = "messages.json";

    // Helper to escape strings for JSON
    private String escape(String s) {
        if (s == null) return "";
        return s.replace("\\", "\\\\").replace("\"", "\\\"");
    }
    
    public void saveAllData(Map<Integer, Alumnus> alumni, List<Event> events, List<Message> messages) {
        // Save Alumni
        List<String> alumniJson = alumni.values().stream()
            .map(a -> String.format(
                "{\"id\":%d, \"name\":\"%s\", \"batch\":%d, \"department\":\"%s\", \"contact\":\"%s\", \"email\":\"%s\", \"currentJob\":\"%s\"}",
                a.id, escape(a.name), a.batch, escape(a.department), escape(a.contact), escape(a.email), escape(a.currentJob)
            )).collect(Collectors.toList());
        try { Files.write(Paths.get(ALUMNI_FILE), ("[" + String.join(",", alumniJson) + "]").getBytes()); } 
        catch (IOException e) { System.out.println("Error saving alumni data."); }

        // Save Events
        List<String> eventsJson = events.stream()
            .map(e -> String.format(
                "{\"name\":\"%s\", \"assignedAlumniIds\":[%s]}",
                escape(e.name),
                e.assignedAlumniIds.stream().map(String::valueOf).collect(Collectors.joining(","))
            )).collect(Collectors.toList());
        try { Files.write(Paths.get(EVENTS_FILE), ("[" + String.join(",", eventsJson) + "]").getBytes()); } 
        catch (IOException e) { System.out.println("Error saving events data."); }

        // Save Messages
        List<String> messagesJson = messages.stream()
            .map(m -> String.format(
                "{\"timestamp\":\"%s\", \"sender\":\"%s\", \"recipient\":\"%s\", \"content\":\"%s\"}",
                escape(m.timestamp), escape(m.sender), escape(m.recipient), escape(m.content)
            )).collect(Collectors.toList());
        try { Files.write(Paths.get(MESSAGES_FILE), ("[" + String.join(",", messagesJson) + "]").getBytes()); } 
        catch (IOException e) { System.out.println("Error saving messages data."); }
    }

    // A simple helper to parse a JSON object string into a Map
    private Map<String, String> parseJsonObject(String json) {
        Map<String, String> map = new HashMap<>();
        Pattern p = Pattern.compile("\"(.*?)\":\"?(.*?)\"?[,}\\]]");
        Matcher m = p.matcher(json);
        while (m.find()) {
            map.put(m.group(1), m.group(2));
        }
        return map;
    }

    public Map<Integer, Alumnus> loadAlumni() {
        Map<Integer, Alumnus> alumniRecords = new HashMap<>();
        try {
            String content = new String(Files.readAllBytes(Paths.get(ALUMNI_FILE)));
            if (content.length() <= 2) return alumniRecords;
            content = content.substring(1, content.length() - 1); // Remove brackets
            
            for (String objStr : content.split("\\},\\{")) {
                Map<String, String> values = parseJsonObject(objStr);
                Alumnus a = new Alumnus(
                    Integer.parseInt(values.get("id")),
                    values.get("name"), Integer.parseInt(values.get("batch")),
                    values.get("department"), values.get("contact"),
                    values.get("email"), values.get("currentJob")
                );
                alumniRecords.put(a.id, a);
            }
        } catch (IOException | NullPointerException e) { /* File might not exist or be empty */ }
        return alumniRecords;
    }

    public List<Event> loadEvents() {
        List<Event> eventList = new ArrayList<>();
        try {
            String content = new String(Files.readAllBytes(Paths.get(EVENTS_FILE)));
            if (content.length() <= 2) return eventList;
            content = content.substring(1, content.length() - 1);

            for (String objStr : content.split("\\},\\{")) {
                Map<String, String> values = parseJsonObject(objStr);
                Event e = new Event(values.get("name"));
                
                // Manually parse the integer array
                Pattern p = Pattern.compile("\\[(.*?)\\]");
                Matcher m = p.matcher(objStr);
                if (m.find() && !m.group(1).isEmpty()) {
                    Arrays.stream(m.group(1).split(","))
                          .map(String::trim)
                          .map(Integer::parseInt)
                          .forEach(e.assignedAlumniIds::add);
                }
                eventList.add(e);
            }
        } catch (IOException | NullPointerException e) { /* File might not exist or be empty */ }
        return eventList;
    }

    public List<Message> loadMessages() {
        List<Message> messageLog = new LinkedList<>();
        try {
            String content = new String(Files.readAllBytes(Paths.get(MESSAGES_FILE)));
            if (content.length() <= 2) return messageLog;
            content = content.substring(1, content.length() - 1);
            
            for (String objStr : content.split("\\},\\{")) {
                Map<String, String> values = parseJsonObject(objStr);
                messageLog.add(new Message(
                    values.get("sender"), values.get("recipient"),
                    values.get("content"), values.get("timestamp")
                ));
            }
        } catch (IOException | NullPointerException e) { /* File might not exist or be empty */ }
        return messageLog;
    }
}


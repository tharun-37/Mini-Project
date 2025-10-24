package models;

import java.util.ArrayList;
import java.util.Map;

/** Represents a single Event. */
public class Event {
    public String name;
    public ArrayList<Integer> assignedAlumniIds = new ArrayList<>();

    public Event(String name) { this.name = name; }

    public void display(Map<Integer, Alumnus> alumniMap) {
        System.out.println("\nEvent: " + name);
        if (assignedAlumniIds.isEmpty()) {
            System.out.println("  - No alumni assigned.");
        } else {
            System.out.println("  - Assigned Alumni:");
            for (int id : assignedAlumniIds) {
                String alumnusName = alumniMap.getOrDefault(id, new Alumnus(-1, "Unknown", 0, "", "", "", "")).name;
                System.out.println("    - " + alumnusName + " (ID: " + id + ")");
            }
        }
    }
}


package services;

import models.Alumnus;
import models.Event;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

public class EventService {
    private List<Event> eventList;
    private Map<Integer, Alumnus> alumniRecords;

    public EventService(List<Event> events, Map<Integer, Alumnus> alumni) {
        this.eventList = events;
        this.alumniRecords = alumni;
    }
    
    public List<Event> getEventList() { return eventList; }

    public void manageEvents(Scanner scanner) {
        System.out.print("(1) Create Event, (2) List Events, (3) Assign Alumnus to Event: ");
        switch (scanner.nextLine()) {
            case "1":
                System.out.print("Enter event name: ");
                eventList.add(new Event(scanner.nextLine()));
                System.out.println("Event created.");
                break;
            case "2":
                if (eventList.isEmpty()) System.out.println("No events.");
                else eventList.forEach(e -> e.display(alumniRecords));
                break;
            case "3":
                assignAlumnusToEvent(scanner);
                break;
            default: System.out.println("Invalid choice.");
        }
    }

    private void assignAlumnusToEvent(Scanner scanner) {
        if (eventList.isEmpty() || alumniRecords.isEmpty()) {
            System.out.println("Cannot assign. Create events and alumni first.");
            return;
        }
        System.out.println("Select an event:");
        for (int i = 0; i < eventList.size(); i++) System.out.println((i + 1) + ". " + eventList.get(i).name);
        try {
            System.out.print("Enter event number: ");
            int eventIdx = Integer.parseInt(scanner.nextLine()) - 1;
            System.out.print("Enter Alumnus ID to assign: ");
            int alumnusId = Integer.parseInt(scanner.nextLine());

            if (eventIdx >= 0 && eventIdx < eventList.size() && alumniRecords.containsKey(alumnusId)) {
                eventList.get(eventIdx).assignedAlumniIds.add(alumnusId);
                System.out.println("Assignment successful.");
            } else {
                System.out.println("Invalid event or Alumnus ID.");
            }
        } catch (NumberFormatException e) {
            System.out.println("Invalid number.");
        }
    }
}


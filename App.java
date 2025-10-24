import models.Alumnus;
import services.AlumniService;
import services.CommunicationService;
import services.EventService;
import services.DataManager;
import ui.Menu;

import java.util.HashMap;
import java.util.Map;
import java.util.Queue;
import java.util.Scanner;
import java.util.LinkedList;

public class App {
    private final Scanner scanner = new Scanner(System.in);
    private final Map<String, String> credentials = new HashMap<>();
    private String loggedInUserRole;
    private int loggedInAlumnusId = -1;

    // Services
    private final AlumniService alumniService;
    private final EventService eventService;
    private final CommunicationService communicationService;
    private final DataManager dataManager;
    private final Queue<String> activityLog = new LinkedList<>();


    public static void main(String[] args) {
        new App().run();
    }

    public App() {
        credentials.put("admin", "admin,ADMIN");
        credentials.put("alumni", "alumni,ALUMNI");

        this.dataManager = new DataManager();
        // Load data and initialize services
        this.alumniService = new AlumniService(dataManager.loadAlumni());
        this.eventService = new EventService(dataManager.loadEvents(), this.alumniService.getAlumniRecords());
        this.communicationService = new CommunicationService(dataManager.loadMessages());
    }

    private void run() {
        System.out.println("===== Welcome to the Alumni Management System =====");
        authenticate();

        if (loggedInUserRole != null) {
            if ("ADMIN".equals(loggedInUserRole)) {
                adminLoop();
            } else {
                alumniLoop();
            }
        }

        dataManager.saveAllData(
            alumniService.getAlumniRecords(),
            eventService.getEventList(),
            communicationService.getMessageLog()
        );
        System.out.println("Data saved. Goodbye!");
    }

    private void authenticate() {
        System.out.print("Enter username: ");
        String user = scanner.nextLine();
        System.out.print("Enter password: ");
        String pass = scanner.nextLine();

        String cred = credentials.get(user);
        if (cred != null && cred.startsWith(pass + ",")) {
            loggedInUserRole = cred.split(",")[1];
            if ("ALUMNI".equals(loggedInUserRole)) loggedInAlumnusId = 1; // Default
            System.out.println(loggedInUserRole + " login successful.");
        } else {
            System.out.println("Authentication failed. Exiting.");
        }
    }

    private void adminLoop() {
        String choice;
        do {
            Menu.displayAdminMenu();
            choice = scanner.nextLine();
            switch (choice) {
                case "1": alumniService.addAlumnus(scanner); break;
                case "2": alumniService.viewAllAlumni(); break;
                case "3": alumniService.updateAlumnus(scanner); break;
                case "4": alumniService.deleteAlumnus(scanner); break;
                case "5": alumniService.searchAlumni(scanner); break;
                case "6": eventService.manageEvents(scanner); break;
                case "7": alumniService.trackCareerAndDonation(scanner); break;
                case "8": alumniService.viewReports(); break;
                case "9": viewActivityLog(); break;
                case "10": communicationService.manageCommunication(scanner, loggedInUserRole, loggedInAlumnusId, alumniService.getAlumniRecords()); break;
                case "11": break;
                default: System.out.println("Invalid choice.");
            }
            if (!choice.equals("11")) logActivity("Admin performed action: " + choice);

        } while (!"11".equals(choice));
    }

    private void alumniLoop() {
        String choice;
        do {
            Menu.displayAlumniMenu();
            choice = scanner.nextLine();
            switch (choice) {
                case "1": alumniService.searchAlumni(scanner); break;
                case "2":
                    Alumnus self = alumniService.getAlumnusById(loggedInAlumnusId);
                    if(self != null) self.display();
                    break;
                case "3": communicationService.manageCommunication(scanner, loggedInUserRole, loggedInAlumnusId, alumniService.getAlumniRecords()); break;
                case "4": break;
                default: System.out.println("Invalid choice.");
            }
        } while (!"4".equals(choice));
    }

    private void logActivity(String activity) {
        activityLog.add(java.time.LocalTime.now().withNano(0) + " - " + activity);
        if (activityLog.size() > 20) {
            activityLog.poll();
        }
    }

    private void viewActivityLog() {
        System.out.println("\n--- Recent Activity Log ---");
        if (activityLog.isEmpty()) {
            System.out.println("No recent activity.");
        } else {
            activityLog.forEach(System.out::println);
        }
    }
}


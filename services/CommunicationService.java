package services;

import models.Alumnus;
import models.Message;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

public class CommunicationService {
    private List<Message> messageLog;

    public CommunicationService(List<Message> messages) {
        this.messageLog = messages;
    }
    
    public List<Message> getMessageLog() { return messageLog; }

    public void manageCommunication(Scanner scanner, String userRole, int alumnusId, Map<Integer, Alumnus> alumniRecords) {
        boolean isAdmin = "ADMIN".equals(userRole);
        System.out.println("\n--- Communication Center ---");
        System.out.print(isAdmin ? "(1) Send to Alumnus, (2) Broadcast, (3) View All: " : "(1) Send to Admin, (2) View My Messages: ");
        
        String choice = scanner.nextLine();
        if (isAdmin) {
            switch (choice) {
                case "1":
                    System.out.print("Enter Alumnus ID: ");
                    String id = scanner.nextLine();
                    System.out.print("Enter message: ");
                    messageLog.add(new Message("Admin", "Alumnus ID: " + id, scanner.nextLine()));
                    System.out.println("Message sent.");
                    break;
                case "2":
                    System.out.print("Enter broadcast message: ");
                    messageLog.add(new Message("Admin", "Broadcast", scanner.nextLine()));
                    System.out.println("Broadcast sent.");
                    break;
                case "3":
                    messageLog.forEach(Message::display);
                    break;
                default: System.out.println("Invalid choice.");
            }
        } else { // Alumni View
            String myName = alumniRecords.get(alumnusId).name;
            switch (choice) {
                case "1":
                    System.out.print("Enter message for Admin: ");
                    messageLog.add(new Message("Alumnus: " + myName, "Admin", scanner.nextLine()));
                    System.out.println("Message sent.");
                    break;
                case "2":
                    String myRecipientId = "Alumnus ID: " + alumnusId;
                    messageLog.stream()
                        .filter(m -> m.recipient.equals(myRecipientId) || m.recipient.equals("Broadcast") || m.sender.contains(myName))
                        .forEach(Message::display);
                    break;
                default: System.out.println("Invalid choice.");
            }
        }
    }
}


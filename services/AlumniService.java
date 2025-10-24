package services;

import models.Alumnus;

import java.util.*;
import java.util.stream.Collectors;

public class AlumniService {
    private final Map<Integer, Alumnus> alumniRecords;
    private int nextId = 1;

    public AlumniService(Map<Integer, Alumnus> initialRecords) {
        this.alumniRecords = initialRecords;
        if (!alumniRecords.isEmpty()) {
            this.nextId = Collections.max(alumniRecords.keySet()) + 1;
        }
    }

    public Map<Integer, Alumnus> getAlumniRecords() {
        return alumniRecords;
    }

    public Alumnus getAlumnusById(int id) {
        return alumniRecords.get(id);
    }

    /**
     * Displays a collection of alumni records in a formatted SQL-like table.
     * @param alumniList The collection of Alumnus objects to display.
     */
    private void displayAlumniAsTable(Collection<Alumnus> alumniList) {
        if (alumniList.isEmpty()) {
            System.out.println("No records found.");
            return;
        }

        String border = "+----+------------------+-------+---------+----------------------------+--------------------------------+";
        System.out.println(border);
        System.out.printf("| %-2s | %-16s | %-5s | %-7s | %-26s | %-30s |\n", "ID", "Name", "Batch", "Dept", "Email", "Current Job");
        System.out.println(border);

        for (Alumnus a : alumniList) {
            // Truncate long strings to fit the table
            String name = a.name.length() > 16 ? a.name.substring(0, 15) + "." : a.name;
            String email = a.email.length() > 26 ? a.email.substring(0, 25) + "." : a.email;
            
            // --- FIX: Check if currentJob is null or empty before processing ---
            String job;
            if (a.currentJob == null || a.currentJob.isEmpty()) {
                job = "N/A";
            } else {
                job = a.currentJob.length() > 30 ? a.currentJob.substring(0, 29) + "." : a.currentJob;
            }
            
            System.out.printf("| %-2d | %-16s | %-5d | %-7s | %-26s | %-30s |\n", a.id, name, a.batch, a.department, email, job);
        }
        System.out.println(border);
    }

    public void viewAllAlumni() {
        System.out.println("\n--- All Alumni Records ---");
        displayAlumniAsTable(alumniRecords.values());
    }

    public void addAlumnus(Scanner scanner) {
        try {
            System.out.print("Enter Name: "); String name = scanner.nextLine();
            System.out.print("Enter Batch (Year): "); int batch = Integer.parseInt(scanner.nextLine());
            System.out.print("Enter Department: "); String dept = scanner.nextLine();
            System.out.print("Enter Contact Number: "); String contact = scanner.nextLine();
            System.out.print("Enter Email: "); String email = scanner.nextLine();
            System.out.print("Enter Current Job: "); String job = scanner.nextLine();

            Alumnus newAlumnus = new Alumnus(nextId, name, batch, dept, contact, email, job);
            alumniRecords.put(nextId, newAlumnus);
            nextId++;
            System.out.println("Alumnus added successfully!");
        } catch (NumberFormatException e) {
            System.out.println("Invalid batch year. Please enter a number.");
        }
    }
    
    public void updateAlumnus(Scanner scanner) {
        try {
            System.out.print("Enter Alumnus ID to update: ");
            int id = Integer.parseInt(scanner.nextLine());
            Alumnus alumnus = alumniRecords.get(id);

            if (alumnus == null) {
                System.out.println("Alumnus with ID " + id + " not found.");
                return;
            }

            System.out.println("Updating details for: " + alumnus.name + ". Press Enter to skip a field.");
            System.out.print("New Name ("+alumnus.name+"): ");
            String name = scanner.nextLine();
            if (!name.isEmpty()) alumnus.name = name;

            System.out.print("New Contact ("+alumnus.contact+"): ");
            String contact = scanner.nextLine();
            if (!contact.isEmpty()) alumnus.contact = contact;

            System.out.println("Update successful.");
        } catch (NumberFormatException e) {
            System.out.println("Invalid ID. Please enter a number.");
        }
    }

    public void deleteAlumnus(Scanner scanner) {
         try {
            System.out.print("Enter Alumnus ID to delete: ");
            int id = Integer.parseInt(scanner.nextLine());
            if (alumniRecords.containsKey(id)) {
                alumniRecords.remove(id);
                System.out.println("Alumnus deleted successfully.");
            } else {
                System.out.println("Alumnus not found.");
            }
        } catch (NumberFormatException e) {
            System.out.println("Invalid ID. Please enter a number.");
        }
    }
    
    public void searchAlumni(Scanner scanner) {
        System.out.print("Search by (1) Name, (2) Department, or (3) Batch Year: ");
        String choice = scanner.nextLine();
        System.out.print("Enter search term: ");
        String term = scanner.nextLine().toLowerCase();
        
        List<Alumnus> results = alumniRecords.values().stream().filter(a -> {
            switch (choice) {
                case "1": return a.name.toLowerCase().contains(term);
                case "2": return a.department.toLowerCase().contains(term);
                case "3": return String.valueOf(a.batch).equals(term);
                default: return false;
            }
        }).collect(Collectors.toList());

        System.out.println("\n--- Search Results ---");
        displayAlumniAsTable(results);
    }
    
    public void trackCareerAndDonation(Scanner scanner) {
        try {
            System.out.print("Enter Alumnus ID: ");
            int id = Integer.parseInt(scanner.nextLine());
            Alumnus alumnus = alumniRecords.get(id);
             if (alumnus == null) {
                System.out.println("Alumnus not found.");
                return;
            }
            System.out.print("(1) Update Career, (2) Add Donation: ");
            String choice = scanner.nextLine();
            if (choice.equals("1")) {
                System.out.print("Enter new job (e.g., 'Senior Developer at Amazon'): ");
                String newJob = scanner.nextLine();
                alumnus.careerHistory.add(newJob);
                alumnus.currentJob = newJob;
                System.out.println("Career updated.");
            } else if (choice.equals("2")) {
                System.out.print("Enter donation cause (e.g., 'Annual Meet 2025'): ");
                String cause = scanner.nextLine();
                System.out.print("Enter amount: ");
                double amount = Double.parseDouble(scanner.nextLine());
                alumnus.donations.put(cause, alumnus.donations.getOrDefault(cause, 0.0) + amount);
                System.out.println("Donation recorded.");
            }
        } catch (NumberFormatException e) {
            System.out.println("Invalid number format.");
        }
    }

    public void viewReports() {
        System.out.println("\n--- System Reports ---");
        System.out.println("Total Alumni Registered: " + alumniRecords.size());
        
        Map<String, Long> countByDept = alumniRecords.values().stream()
            .collect(Collectors.groupingBy(a -> a.department, Collectors.counting()));
        System.out.println("Alumni Count by Department: " + countByDept);
        System.out.println("--- End of Report ---");
    }
}


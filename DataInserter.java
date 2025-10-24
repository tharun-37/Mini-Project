import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * A simple, standalone utility to create the initial alumni.json file.
 * Compile and run this file once to generate the data file, then run the main App.
 */
public class DataInserter {

    // A simplified Alumnus class just for this inserter tool.
    static class Alumnus {
        int id;
        String name, department, contact, email, currentJob;
        int batch;

        Alumnus(int id, String name, int batch, String dept, String contact, String email, String job) {
            this.id = id; this.name = name; this.batch = batch; this.department = dept;
            this.contact = contact; this.email = email; this.currentJob = job;
        }

        // Helper to escape strings for JSON
        private String escape(String s) {
            return s.replace("\\", "\\\\").replace("\"", "\\\"");
        }

        // Converts the object to a JSON string format
        public String toJson() {
            return String.format(
                "{\"id\":%d, \"name\":\"%s\", \"batch\":%d, \"department\":\"%s\", \"contact\":\"%s\", \"email\":\"%s\", \"currentJob\":\"%s\"}",
                id, escape(name), batch, escape(department), escape(contact), escape(email), escape(currentJob)
            );
        }
    }

    public static void main(String[] args) {
        List<Alumnus> initialAlumni = new ArrayList<>();
        initialAlumni.add(new Alumnus(1, "Arun K", 2024, "CSE", "9876543210", "arunk.20cse@kongu.edu", "Software Engineer at Google"));
        initialAlumni.add(new Alumnus(2, "Priya S", 2023, "ECE", "8765432109", "priyas.19ece@kongu.edu", "Hardware Engineer at Intel"));
        initialAlumni.add(new Alumnus(3, "Vijay R", 2024, "MECH", "7654321098", "vijayr.20mech@kongu.edu", "Mechanical Designer at Bosch"));
        initialAlumni.add(new Alumnus(4, "Anitha G", 2022, "IT", "6543210987", "anithag.18it@kongu.edu", "Data Analyst at TCS"));
        initialAlumni.add(new Alumnus(5, "Suresh P", 2023, "CIVIL", "9123456780", "sureshp.19civil@kongu.edu", "Site Engineer at L&T"));

        // Convert the list of Alumnus objects to a list of JSON strings
        List<String> alumniJsonList = initialAlumni.stream()
                                                   .map(Alumnus::toJson)
                                                   .collect(Collectors.toList());

        // Join the JSON strings with commas and wrap them in brackets to form a valid JSON array
        String finalJson = "[" + String.join(",", alumniJsonList) + "]";

        try {
            // Write the final JSON string to the alumni.json file
            Files.write(Paths.get("alumni.json"), finalJson.getBytes());
            System.out.println("Successfully created alumni.json with 5 records.");
        } catch (IOException e) {
            System.err.println("An error occurred while writing the file: " + e.getMessage());
            e.printStackTrace();
        }
    }
}

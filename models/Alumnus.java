package models;

import java.util.LinkedList;
import java.util.TreeMap;

/** Represents a single Alumnus. */
public class Alumnus {
    public int id;
    public String name, department, contact, email, currentJob;
    public int batch;
    public LinkedList<String> careerHistory = new LinkedList<>();
    public TreeMap<String, Double> donations = new TreeMap<>();

    public Alumnus(int id, String name, int batch, String dept, String contact, String email, String job) {
        this.id = id; this.name = name; this.batch = batch; this.department = dept;
        this.contact = contact; this.email = email; this.currentJob = job;
        if (job != null && !job.isEmpty()) this.careerHistory.add(job);
    }

    public void display() {
        System.out.println("----------------------------------------");
        System.out.println("ID: " + id + " | Name: " + name);
        System.out.println("Batch: " + batch + " | Department: " + department);
        System.out.println("Contact: " + contact + " | Email: " + email);
        System.out.println("Current Job: " + currentJob);
        System.out.println("Career History: " + String.join(" -> ", careerHistory));
        System.out.println("Donations: " + donations);
        System.out.println("----------------------------------------");
    }
}


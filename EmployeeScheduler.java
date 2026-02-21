package edu.assignment.scheduler;

import java.util.*;

class Employee {
    String name;
    Map<String, String> preferences;
    int assignedDays;

    Employee(String name, Map<String, String> preferences) {
        this.name = name;
        this.preferences = preferences;
        this.assignedDays = 0;
    }
}

public class EmployeeScheduler {

    static String[] DAYS = {
            "Monday", "Tuesday", "Wednesday",
            "Thursday", "Friday", "Saturday", "Sunday"
    };

    static String[] SHIFTS = {
            "Morning", "Afternoon", "Evening"
    };

    public static void main(String[] args) {

        List<Employee> employees = new ArrayList<>();

        // 9 Employees 
        for (String name : Arrays.asList(
                "Alice", "Bob", "Charlie",
                "David", "Eva", "Frank",
                "Grace", "Henry", "Ivy")) {

            Map<String, String> prefs = new HashMap<>();

            for (String day : DAYS) {
                if (name.equals("Alice") || name.equals("David") || name.equals("Grace"))
                    prefs.put(day, "Morning");
                else if (name.equals("Bob") || name.equals("Eva") || name.equals("Henry"))
                    prefs.put(day, "Afternoon");
                else
                    prefs.put(day, "Evening");
            }

            employees.add(new Employee(name, prefs));
        }

        Map<String, Map<String, List<String>>> schedule = createSchedule(employees);

        printSchedule(schedule);
    }

    public static Map<String, Map<String, List<String>>> createSchedule(List<Employee> employees) {

        Map<String, Map<String, List<String>>> schedule = new HashMap<>();

        // Creating empty schedule
        for (String day : DAYS) {
            Map<String, List<String>> shiftMap = new HashMap<>();
            for (String shift : SHIFTS) {
                shiftMap.put(shift, new ArrayList<>());
            }
            schedule.put(day, shiftMap);
        }

        // PASS 1: Assigning preferred shifts
        for (String day : DAYS) {
            for (Employee emp : employees) {

                if (emp.assignedDays >= 5)
                    continue;

                String preferredShift = emp.preferences.get(day);

                // Checking  shift has space AND employee not already assigned that day
                if (schedule.get(day).get(preferredShift).size() < 2 &&
                        !isAssignedThatDay(schedule, day, emp.name)) {

                    schedule.get(day).get(preferredShift).add(emp.name);
                    emp.assignedDays++;
                }
            }
        }

        // PASS 2: Filling remaining shifts
        Random rand = new Random();

        for (String day : DAYS) {
            for (String shift : SHIFTS) {

                while (schedule.get(day).get(shift).size() < 2) {

                    List<Employee> available = new ArrayList<>();

                    for (Employee emp : employees) {

                        if (emp.assignedDays >= 5)
                            continue;

                        if (!isAssignedThatDay(schedule, day, emp.name)) {
                            available.add(emp);
                        }
                    }

                    if (available.isEmpty())
                        break;

                    Employee chosen = available.get(rand.nextInt(available.size()));
                    schedule.get(day).get(shift).add(chosen.name);
                    chosen.assignedDays++;
                }
            }
        }

        return schedule;
    }

    // Helper method to check if employee already works that day
    public static boolean isAssignedThatDay(
            Map<String, Map<String, List<String>>> schedule,
            String day,
            String employeeName) {

        for (String shift : SHIFTS) {
            if (schedule.get(day).get(shift).contains(employeeName)) {
                return true;
            }
        }
        return false;
    }

    public static void printSchedule(Map<String, Map<String, List<String>>> schedule) {

        System.out.println("\nFINAL WEEKLY SCHEDULE\n");

        for (String day : DAYS) {
            System.out.println("--- " + day + " ---");
            for (String shift : SHIFTS) {
                System.out.println(shift + ": " +
                        String.join(", ", schedule.get(day).get(shift)));
            }
            System.out.println();
        }
    }
}

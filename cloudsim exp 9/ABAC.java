// Experiment 9: Attribute-Based Access Control (ABAC)

public class ABAC {

    // Access decision based on attributes
    public static boolean checkAccess(
            String department,
            String clearanceLevel,
            String accessTime
    ) {
        // Policy:
        // Allow access if department is IT,
        // clearance is HIGH,
        // and access time is working hours
        if (department.equals("IT")
                && clearanceLevel.equals("HIGH")
                && accessTime.equals("WORKING_HOURS")) {
            return true;
        }
        return false;
    }

    public static void main(String[] args) {

        System.out.println("ABAC Simulation Started\n");

        testAccess("IT", "HIGH", "WORKING_HOURS");
        testAccess("HR", "HIGH", "WORKING_HOURS");
        testAccess("IT", "LOW", "WORKING_HOURS");
        testAccess("IT", "HIGH", "NON_WORKING_HOURS");

        System.out.println("\nABAC Simulation Completed");
    }

    // Test cases
    private static void testAccess(
            String department,
            String clearance,
            String time
    ) {
        System.out.print(
                "Department: " + department +
                ", Clearance: " + clearance +
                ", Time: " + time + " → "
        );

        if (checkAccess(department, clearance, time)) {
            System.out.println("ACCESS GRANTED");
        } else {
            System.out.println("ACCESS DENIED");
        }
    }
}

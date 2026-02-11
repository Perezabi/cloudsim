// Experiment 8: Role-Based Access Control (RBAC)

import java.util.HashMap;
import java.util.Map;

public class RBAC {

    // Role → Permission mapping
    private static final Map<String, String[]> rolePermissions = new HashMap<>();

    static {
        rolePermissions.put("ADMIN", new String[]{"READ", "WRITE", "DELETE"});
        rolePermissions.put("USER", new String[]{"READ", "WRITE"});
        rolePermissions.put("GUEST", new String[]{"READ"});
    }

    // Check access based on role
    public static boolean checkAccess(String role, String permission) {
        String[] permissions = rolePermissions.get(role);
        if (permissions == null) {
            return false;
        }
        for (String p : permissions) {
            if (p.equals(permission)) {
                return true;
            }
        }
        return false;
    }

    public static void main(String[] args) {

        System.out.println("RBAC Simulation Started\n");

        testAccess("ADMIN", "DELETE");
        testAccess("USER", "DELETE");
        testAccess("GUEST", "READ");
        testAccess("GUEST", "WRITE");

        System.out.println("\nRBAC Simulation Completed");
    }

    // Test method
    private static void testAccess(String role, String permission) {
        System.out.print("Role: " + role + " | Permission: " + permission + " → ");
        if (checkAccess(role, permission)) {
            System.out.println("ACCESS GRANTED");
        } else {
            System.out.println("ACCESS DENIED");
        }
    }
}

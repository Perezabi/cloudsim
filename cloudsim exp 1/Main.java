
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;

import org.cloudbus.cloudsim.Cloudlet;
import org.cloudbus.cloudsim.CloudletSchedulerTimeShared;
import org.cloudbus.cloudsim.Datacenter;
import org.cloudbus.cloudsim.DatacenterCharacteristics;
import org.cloudbus.cloudsim.Host;
import org.cloudbus.cloudsim.Log;
import org.cloudbus.cloudsim.Pe;
import org.cloudbus.cloudsim.Storage;
import org.cloudbus.cloudsim.UtilizationModel;
import org.cloudbus.cloudsim.UtilizationModelFull;
import org.cloudbus.cloudsim.Vm;
import org.cloudbus.cloudsim.VmAllocationPolicySimple;
import org.cloudbus.cloudsim.VmSchedulerTimeShared;
import org.cloudbus.cloudsim.core.CloudSim;
import org.cloudbus.cloudsim.provisioners.BwProvisionerSimple;
import org.cloudbus.cloudsim.provisioners.PeProvisionerSimple;
import org.cloudbus.cloudsim.provisioners.RamProvisionerSimple;

/**
 * CloudSim 3.0.3 Example with Custom Priority Scheduling Algorithm
 *
 * This program demonstrates a custom scheduling algorithm (Priority Scheduling)
 * that is NOT provided by CloudSim. The algorithm sorts cloudlets by priority
 * BEFORE submission to the broker, ensuring high-priority tasks execute first.
 *
 * Compile: javac -cp "lib/*" Main.java || Run: java -cp ".;lib/*" Main
 *
 * javac -cp "lib/*" Main.java || java -cp ".;lib/*" Main
 *
 */
public class Main {

    // Priority levels: Higher number = Higher priority
    private static final int PRIORITY_HIGH = 3;
    private static final int PRIORITY_MEDIUM = 2;
    private static final int PRIORITY_LOW = 1;

    /**
     * Custom Cloudlet class with priority attribute
     */
    static class PriorityCloudlet extends Cloudlet {

        private int priority;

        public PriorityCloudlet(int cloudletId, long cloudletLength, int pesNumber,
                long cloudletFileSize, long cloudletOutputSize,
                UtilizationModel utilizationModelCpu,
                UtilizationModel utilizationModelRam,
                UtilizationModel utilizationModelBw,
                int priority) {
            super(cloudletId, cloudletLength, pesNumber, cloudletFileSize,
                    cloudletOutputSize, utilizationModelCpu, utilizationModelRam,
                    utilizationModelBw);
            this.priority = priority;
        }

        public int getPriority() {
            return priority;
        }

        public void setPriority(int priority) {
            this.priority = priority;
        }
    }

    /**
     * CUSTOM SCHEDULING ALGORITHM: Priority Scheduling
     *
     * This algorithm sorts cloudlets by priority (descending order) before
     * submission to the broker. Cloudlets with higher priority values are
     * scheduled first.
     *
     * Algorithm Steps: 1. Assign priority to each cloudlet 2. Sort cloudlets by
     * priority (high to low) 3. Submit sorted cloudlets to broker
     *
     * This ensures high-priority tasks execute before low-priority ones,
     * improving response time for critical workloads.
     */
    private static void applyPriorityScheduling(List<PriorityCloudlet> cloudletList) {
        // Sort cloudlets by priority (descending: highest priority first)
        Collections.sort(cloudletList, new Comparator<PriorityCloudlet>() {
            @Override
            public int compare(PriorityCloudlet c1, PriorityCloudlet c2) {
                // Higher priority number = higher priority
                // Return negative if c1 should come before c2
                return Integer.compare(c2.getPriority(), c1.getPriority());
            }
        });

        Log.printLine("\n=== CUSTOM PRIORITY SCHEDULING APPLIED ===");
        Log.printLine("Cloudlets sorted by priority (High to Low):");
        for (PriorityCloudlet cloudlet : cloudletList) {
            String priorityStr = getPriorityString(cloudlet.getPriority());
            Log.printLine("  Cloudlet " + cloudlet.getCloudletId()
                    + " -> Priority: " + priorityStr + " (" + cloudlet.getPriority() + ")");
        }
        Log.printLine("==========================================\n");
    }

    private static String getPriorityString(int priority) {
        switch (priority) {
            case PRIORITY_HIGH:
                return "HIGH";
            case PRIORITY_MEDIUM:
                return "MEDIUM";
            case PRIORITY_LOW:
                return "LOW";
            default:
                return "UNKNOWN";
        }
    }

    public static void main(String[] args) {
        Log.printLine("==========================================");
        Log.printLine("CloudSim 3.0.3 - Custom Priority Scheduling");
        Log.printLine("==========================================");

        try {
            // Step 1: Initialize CloudSim
            int numUser = 1;
            Calendar calendar = Calendar.getInstance();
            boolean traceFlag = false;

            Log.printLine("\nInitializing CloudSim...");
            CloudSim.init(numUser, calendar, traceFlag);

            // Step 2: Create Datacenter
            Log.printLine("Creating Datacenter...");
            Datacenter datacenter = createDatacenter("Datacenter_0");

            // Step 3: Create Broker
            Log.printLine("Creating Broker...");
            org.cloudbus.cloudsim.DatacenterBroker broker = createBroker();
            int brokerId = broker.getId();

            // Step 4: Create 2 VMs
            Log.printLine("Creating 2 VMs...");
            List<Vm> vmList = new ArrayList<Vm>();

            Vm vm1 = new Vm(0, brokerId, 1000, 1, 512, 1000, 10000, "Xen",
                    new CloudletSchedulerTimeShared());
            Vm vm2 = new Vm(1, brokerId, 1000, 1, 512, 1000, 10000, "Xen",
                    new CloudletSchedulerTimeShared());
            vmList.add(vm1);
            vmList.add(vm2);

            broker.submitVmList(vmList);
            Log.printLine("VMs submitted: " + vmList.size());

            // Step 5: Create Multiple Cloudlets with Different Priorities
            Log.printLine("\nCreating Cloudlets with different priorities...");
            List<PriorityCloudlet> cloudletList = new ArrayList<PriorityCloudlet>();
            UtilizationModel utilizationModel = new UtilizationModelFull();

            // Create cloudlets with varying priorities and lengths
            cloudletList.add(new PriorityCloudlet(0, 10000, 1, 300, 300,
                    utilizationModel, utilizationModel, utilizationModel, PRIORITY_LOW));

            cloudletList.add(new PriorityCloudlet(1, 8000, 1, 300, 300,
                    utilizationModel, utilizationModel, utilizationModel, PRIORITY_HIGH));

            cloudletList.add(new PriorityCloudlet(2, 12000, 1, 300, 300,
                    utilizationModel, utilizationModel, utilizationModel, PRIORITY_MEDIUM));

            cloudletList.add(new PriorityCloudlet(3, 6000, 1, 300, 300,
                    utilizationModel, utilizationModel, utilizationModel, PRIORITY_HIGH));

            cloudletList.add(new PriorityCloudlet(4, 15000, 1, 300, 300,
                    utilizationModel, utilizationModel, utilizationModel, PRIORITY_LOW));

            Log.printLine("Cloudlets created: " + cloudletList.size());

            // Step 6: APPLY CUSTOM PRIORITY SCHEDULING ALGORITHM
            // This is the custom scheduling logic - sorting by priority BEFORE submission
            applyPriorityScheduling(cloudletList);

            // Step 7: Submit sorted cloudlets to broker
            // Convert PriorityCloudlet list to Cloudlet list for submission
            List<Cloudlet> cloudletsToSubmit = new ArrayList<Cloudlet>();
            for (PriorityCloudlet pcl : cloudletList) {
                pcl.setUserId(brokerId);
                cloudletsToSubmit.add(pcl);
            }

            broker.submitCloudletList(cloudletsToSubmit);
            Log.printLine("Cloudlets submitted to broker (sorted by priority)");

            // Step 8: Start Simulation
            Log.printLine("\nStarting simulation...");
            CloudSim.startSimulation();

            // Step 9: Stop Simulation
            CloudSim.stopSimulation();

            // Step 10: Print Results
            Log.printLine("\n==========================================");
            Log.printLine("SIMULATION RESULTS");
            Log.printLine("==========================================");
            List<Cloudlet> finishedCloudlets = broker.getCloudletReceivedList();
            printCloudletList(finishedCloudlets);

            // Print scheduling analysis
            Log.printLine("\n=== SCHEDULING ANALYSIS ===");
            Log.printLine("Execution order shows priority scheduling in effect:");
            for (int i = 0; i < finishedCloudlets.size(); i++) {
                Cloudlet cl = finishedCloudlets.get(i);
                if (cl instanceof PriorityCloudlet) {
                    PriorityCloudlet pcl = (PriorityCloudlet) cl;
                    Log.printLine("Position " + (i + 1) + ": Cloudlet " + pcl.getCloudletId()
                            + " (Priority: " + getPriorityString(pcl.getPriority()) + ")");
                }
            }

            Log.printLine("\n==========================================");
            Log.printLine("SIMULATION COMPLETED SUCCESSFULLY!");
            Log.printLine("Custom Priority Scheduling Algorithm executed successfully.");
            Log.printLine("==========================================");

        } catch (Exception e) {
            e.printStackTrace();
            Log.printLine("Simulation failed: " + e.getMessage());
        }
    }

    /**
     * Creates a Datacenter with 1 Host
     */
    private static Datacenter createDatacenter(String name) {
        // Create PEs (Processing Elements)
        List<Pe> peList = new ArrayList<Pe>();
        int mips = 1000;

        peList.add(new Pe(0, new PeProvisionerSimple(mips)));
        peList.add(new Pe(1, new PeProvisionerSimple(mips)));

        // Create Host
        int hostId = 0;
        int ram = 2048; // MB
        long storage = 1000000; // MB
        int bw = 10000; // Mbps

        Host host = new Host(
                hostId,
                new RamProvisionerSimple(ram),
                new BwProvisionerSimple(bw),
                storage,
                peList,
                new VmSchedulerTimeShared(peList)
        );

        List<Host> hostList = new ArrayList<Host>();
        hostList.add(host);

        // Datacenter characteristics
        String arch = "x86";
        String os = "Linux";
        String vmm = "Xen";
        double time_zone = 10.0;
        double cost = 3.0;
        double costPerMem = 0.05;
        double costPerStorage = 0.001;
        double costPerBw = 0.0;

        LinkedList<Storage> storageList = new LinkedList<Storage>();

        DatacenterCharacteristics characteristics = new DatacenterCharacteristics(
                arch, os, vmm, hostList, time_zone, cost, costPerMem,
                costPerStorage, costPerBw
        );

        Datacenter datacenter = null;
        try {
            datacenter = new Datacenter(name, characteristics,
                    new VmAllocationPolicySimple(hostList), storageList, 0);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return datacenter;
    }

    /**
     * Creates a Broker
     */
    private static org.cloudbus.cloudsim.DatacenterBroker createBroker() {
        org.cloudbus.cloudsim.DatacenterBroker broker = null;
        try {
            broker = new org.cloudbus.cloudsim.DatacenterBroker("Broker");
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        return broker;
    }

    /**
     * Prints Cloudlet execution results
     */
    private static void printCloudletList(List<Cloudlet> list) {
        int size = list.size();
        Cloudlet cloudlet;

        String indent = "    ";
        Log.printLine("\nCloudlet ID" + indent + "STATUS" + indent
                + "Data center ID" + indent + "VM ID" + indent
                + "Time" + indent + "Start Time" + indent + "Finish Time");

        DecimalFormat dft = new DecimalFormat("###.##");
        for (int i = 0; i < size; i++) {
            cloudlet = list.get(i);
            Log.print(indent + cloudlet.getCloudletId() + indent + indent);

            if (cloudlet.getCloudletStatus() == Cloudlet.SUCCESS) {
                Log.print("SUCCESS");

                Log.printLine(indent + indent + cloudlet.getResourceId()
                        + indent + indent + indent + cloudlet.getVmId()
                        + indent + indent + dft.format(cloudlet.getActualCPUTime())
                        + indent + indent + dft.format(cloudlet.getExecStartTime())
                        + indent + indent + dft.format(cloudlet.getFinishTime()));
            }
        }
    }
}

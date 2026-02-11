import org.cloudbus.cloudsim.*;
import org.cloudbus.cloudsim.core.CloudSim;
import org.cloudbus.cloudsim.provisioners.*;

import java.util.*;
import java.io.*;

public class Main {

    private static FileWriter logWriter;

    public static void main(String[] args) {

        try {
            // Create log file
            logWriter = new FileWriter("cloud_logs.txt", true);
            writeLog("Cloud Log Forensics Simulation Started");

            // 1. Initialize CloudSim
            CloudSim.init(1, Calendar.getInstance(), false);

            // 2. Create Datacenter
            Datacenter datacenter = createDatacenter("Datacenter_1");
            writeLog("Datacenter created");

            // 3. Create Broker
            DatacenterBroker broker = new DatacenterBroker("Broker_1");
            int brokerId = broker.getId();
            writeLog("Broker created with ID: " + brokerId);

            // 4. Create VM
            Vm vm = new Vm(
                    0, brokerId, 1000, 1,
                    512, 1000, 10000,
                    "Xen", new CloudletSchedulerTimeShared()
            );
            writeLog("VM created with ID: 0");

            broker.submitVmList(Collections.singletonList(vm));

            // 5. Create Cloudlets
            List<Cloudlet> cloudletList = new ArrayList<>();

            for (int i = 0; i < 4; i++) {
                Cloudlet cloudlet = new Cloudlet(
                        i, 3000, 1, 300, 300,
                        null, null, null
                );
                cloudlet.setUserId(brokerId);
                cloudletList.add(cloudlet);
                writeLog("Cloudlet created with ID: " + i);
            }

            broker.submitCloudletList(cloudletList);

            // 6. Start Simulation
            CloudSim.startSimulation();
            CloudSim.stopSimulation();

            writeLog("Simulation completed");

            // 7. Log Forensics Analysis
            analyzeLogs();

            writeLog("Log forensics analysis completed");
            logWriter.close();

            System.out.println("Simulation and log forensics completed successfully");
            System.out.println("Check cloud_logs.txt for details");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Datacenter creation
    private static Datacenter createDatacenter(String name) {

        List<Host> hostList = new ArrayList<>();

        List<Pe> peList = new ArrayList<>();
        peList.add(new Pe(0, new PeProvisionerSimple(2000)));

        Host host = new Host(
                0,
                new RamProvisionerSimple(4096),
                new BwProvisionerSimple(10000),
                1000000,
                peList,
                new VmSchedulerTimeShared(peList)
        );

        hostList.add(host);

        DatacenterCharacteristics characteristics =
                new DatacenterCharacteristics(
                        "x86", "Linux", "Xen",
                        hostList, 10.0, 3.0,
                        0.05, 0.1, 0.1
                );

        try {
            return new Datacenter(
                    name,
                    characteristics,
                    new VmAllocationPolicySimple(hostList),
                    new LinkedList<>(),
                    0
            );
        } catch (Exception e) {
            return null;
        }
    }

    // Write logs
    private static void writeLog(String message) throws IOException {
        logWriter.write(new Date() + " : " + message + "\n");
    }

    // Simple forensic analysis
    private static void analyzeLogs() throws IOException {

        writeLog("Forensic Analysis Started");

        BufferedReader reader = new BufferedReader(new FileReader("cloud_logs.txt"));
        String line;
        int cloudletCount = 0;

        while ((line = reader.readLine()) != null) {
            if (line.contains("Cloudlet created")) {
                cloudletCount++;
            }
        }

        reader.close();

        if (cloudletCount > 3) {
            writeLog("ALERT: High number of cloudlets detected – Possible overload or attack");
        } else {
            writeLog("Normal cloudlet activity detected");
        }
    }
}

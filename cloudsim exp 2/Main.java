import org.cloudbus.cloudsim.*;
import org.cloudbus.cloudsim.core.CloudSim;
import org.cloudbus.cloudsim.provisioners.PeProvisionerSimple;
import org.cloudbus.cloudsim.provisioners.RamProvisionerSimple;
import org.cloudbus.cloudsim.provisioners.BwProvisionerSimple;

import java.util.*;


public class Main {

    public static void main(String[] args) {

        System.out.println("Starting Resource Management Simulation...");

        try {
            // 1. Initialize CloudSim
            CloudSim.init(1, Calendar.getInstance(), false);

            // 2. Create Datacenter
            Datacenter datacenter = createDatacenter("Datacenter_1");

            // 3. Create Broker
            DatacenterBroker broker = new DatacenterBroker("Broker_1");
            int brokerId = broker.getId();

            // 4. Create Initial VM list
            List<Vm> vmList = new ArrayList<>();

            Vm vm1 = new Vm(0, brokerId, 1000, 1, 512, 1000,
                    10000, "Xen", new CloudletSchedulerTimeShared());

            vmList.add(vm1);

            System.out.println("Initial VM created");

            // 5. Create Cloudlets (High load)
            List<Cloudlet> cloudletList = new ArrayList<>();

            for (int i = 0; i < 6; i++) {
                Cloudlet cloudlet = new Cloudlet(
                        i, 4000, 1, 300, 300,
                        null, null, null
                );
                cloudlet.setUserId(brokerId);
                cloudletList.add(cloudlet);
            }

            // 6. RESOURCE MANAGEMENT LOGIC
            // If cloudlets > threshold, add a new VM
            int THRESHOLD = 4;

            if (cloudletList.size() > THRESHOLD) {
                Vm vm2 = new Vm(1, brokerId, 1000, 1, 512, 1000,
                        10000, "Xen", new CloudletSchedulerTimeShared());

                vmList.add(vm2);
                System.out.println("High load detected → New VM allocated");
            }

            // 7. Submit VMs and Cloudlets
            broker.submitVmList(vmList);
            broker.submitCloudletList(cloudletList);

            // 8. Start Simulation
            CloudSim.startSimulation();
            CloudSim.stopSimulation();

            // 9. Print Results
            System.out.println("Simulation finished successfully");
            System.out.println("Total VMs used: " + vmList.size());
            System.out.println("Total Cloudlets executed: " + cloudletList.size());

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
                        "x86",
                        "Linux",
                        "Xen",
                        hostList,
                        10.0,
                        3.0,
                        0.05,
                        0.1,
                        0.1
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
}

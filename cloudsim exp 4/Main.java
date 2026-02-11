import org.cloudbus.cloudsim.*;
import org.cloudbus.cloudsim.core.CloudSim;
import org.cloudbus.cloudsim.provisioners.*;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import java.util.*;
import java.io.*;
import java.util.Base64;

public class Main {

    // AES key (16 bytes)
    private static final String SECRET_KEY = "1234567890123456";

    public static void main(String[] args) {

        try {
            System.out.println("Starting Secure File Sharing Simulation...");

            // 1. Initialize CloudSim
            CloudSim.init(1, Calendar.getInstance(), false);

            // 2. Create Datacenter
            Datacenter datacenter = createDatacenter("Datacenter_1");

            // 3. Create Broker
            DatacenterBroker broker = new DatacenterBroker("Broker_1");
            int brokerId = broker.getId();

            // 4. Create VM
            Vm vm = new Vm(
                    0, brokerId, 1000, 1,
                    512, 1000, 10000,
                    "Xen", new CloudletSchedulerTimeShared()
            );
            broker.submitVmList(Collections.singletonList(vm));

            // 5. Secure File Upload
            String originalData = "This is a confidential cloud file";
            System.out.println("Original File Data: " + originalData);

            String encryptedData = encrypt(originalData);
            saveToCloud(encryptedData);

            System.out.println("File encrypted and stored securely in cloud");

            // 6. Secure File Download (Authorized User)
            String retrievedEncryptedData = readFromCloud();
            String decryptedData = decrypt(retrievedEncryptedData);

            System.out.println("Decrypted File Data (Authorized Access): " + decryptedData);

            // 7. Start Simulation
            CloudSim.startSimulation();
            CloudSim.stopSimulation();

            System.out.println("Secure File Sharing Simulation Completed");

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

    // AES Encryption
    private static String encrypt(String data) throws Exception {
        SecretKeySpec key = new SecretKeySpec(SECRET_KEY.getBytes(), "AES");
        Cipher cipher = Cipher.getInstance("AES");
        cipher.init(Cipher.ENCRYPT_MODE, key);
        return Base64.getEncoder().encodeToString(cipher.doFinal(data.getBytes()));
    }

    // AES Decryption
    private static String decrypt(String encryptedData) throws Exception {
        SecretKeySpec key = new SecretKeySpec(SECRET_KEY.getBytes(), "AES");
        Cipher cipher = Cipher.getInstance("AES");
        cipher.init(Cipher.DECRYPT_MODE, key);
        return new String(cipher.doFinal(Base64.getDecoder().decode(encryptedData)));
    }

    // Simulate storing encrypted file in cloud
    private static void saveToCloud(String data) throws IOException {
        FileWriter writer = new FileWriter("cloud_storage.txt");
        writer.write(data);
        writer.close();
    }

    // Simulate retrieving encrypted file from cloud
    private static String readFromCloud() throws IOException {
        BufferedReader reader = new BufferedReader(new FileReader("cloud_storage.txt"));
        String data = reader.readLine();
        reader.close();
        return data;
    }
}

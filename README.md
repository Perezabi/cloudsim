🛠 Requirements

Make sure the following software is installed:

1️⃣ Java

Install Java 25 or latest version

Check installation:

      java -version

2️⃣ Python

Install Python latest version

Check installation:

      python --version

3️⃣ CloudSim

CloudSim version 3.0.3 is already included in the project.

Location:

      /jar/cloudsim-3.0.3.jar

▶️ How to Run the Experiment
Step 1 — Open Project

Open the project folder in Terminal / Command Prompt

      cd cloudsim exp 1
Step 2 — Compile the Program

      javac -cp "jar/cloudsim-3.0.3.jar" src/CloudSimExp1.java

      java -cp ".;jar/cloudsim-3.0.3.jar;src" CloudSimExp1   
            //(Linux/Mac users replace ; with :)

🔐 Encryption Test

Images stored in the images folder can be used for encryption testing.

Example:

images/
   sample1.png
   sample2.jpg

These images are used as input to test encryption algorithms.

📊 Output

The program will display:

    VM creation
    
    Cloudlet execution
    
    Simulation results
    
    Encryption test results

📚 Technologies Used

    Java
    
    CloudSim 3.0.3
    
    Python
    
    Image Encryption

👨‍💻 Author

    T Thanga Paresh

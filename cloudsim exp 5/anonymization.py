# Experiment 5: Data Anonymization

import pandas as pd

# -----------------------------
# Step 1: Create Sample Dataset
# -----------------------------
data = {
    "Name": ["Ravi", "Kumar", "Anu", "Ravi", "Anu", "Kumar"],
    "Age": [25, 26, 25, 27, 26, 27],
    "ZIP": ["623001", "623001", "623002", "623001", "623002", "623002"],
    "Disease": ["Flu", "Covid", "Flu", "Cancer", "Covid", "Flu"]
}

df = pd.DataFrame(data)

print("\nOriginal Dataset:")
print(df)

# -----------------------------
# Step 2: Data Masking
# -----------------------------
df_masked = df.copy()
df_masked["Name"] = "XXXX"
df_masked["ZIP"] = df_masked["ZIP"].str[:3] + "***"

print("\nAfter Data Masking:")
print(df_masked)

# -----------------------------
# Step 3: K-Anonymization (k=2)
# -----------------------------
# Group by Age and ZIP so that at least 2 records exist per group
df_k_anonymized = df.groupby(["Age", "ZIP"]).filter(lambda x: len(x) >= 2)

print("\nAfter K-Anonymization (k = 2):")
print(df_k_anonymized)

# -----------------------------
# Step 4: Conclusion
# -----------------------------
print("\nData Anonymization Completed Successfully")

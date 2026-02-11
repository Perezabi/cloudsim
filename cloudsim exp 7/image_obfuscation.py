# Experiment 7: Image Obfuscation using Pixel Shifting

import cv2
import numpy as np

# -----------------------------
# Step 1: Read Image
# -----------------------------
image = cv2.imread("original.jpg")

if image is None:
    print("Error: Image not found")
    exit()

print("Original image loaded")

# -----------------------------
# Step 2: Obfuscation (Pixel Shift)
# -----------------------------
# Shift pixels horizontally by 100 positions
obfuscated_image = np.roll(image, shift=100, axis=1)

# -----------------------------
# Step 3: Save Obfuscated Image
# -----------------------------
cv2.imwrite("obfuscated.jpg", obfuscated_image)

print("Image obfuscated successfully")
print("Image Obfuscation Experiment Completed")

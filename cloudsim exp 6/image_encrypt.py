# Experiment 6: Image Encryption using AES

from Crypto.Cipher import AES
from Crypto.Random import get_random_bytes
import os

# -----------------------------
# Step 1: Read Image
# -----------------------------
with open("original.jpg", "rb") as f:
    image_data = f.read()

print("Original image loaded")

# -----------------------------
# Step 2: Generate AES Key
# -----------------------------
key = get_random_bytes(16)   # 128-bit key
cipher = AES.new(key, AES.MODE_EAX)

# -----------------------------
# Step 3: Encrypt Image
# -----------------------------
ciphertext, tag = cipher.encrypt_and_digest(image_data)

with open("encrypted.img", "wb") as f:
    f.write(cipher.nonce)
    f.write(tag)
    f.write(ciphertext)

print("Image encrypted successfully")

# -----------------------------
# Step 4: Decrypt Image
# -----------------------------
with open("encrypted.img", "rb") as f:
    nonce = f.read(16)
    tag = f.read(16)
    ciphertext = f.read()

cipher = AES.new(key, AES.MODE_EAX, nonce=nonce)
decrypted_data = cipher.decrypt_and_verify(ciphertext, tag)

with open("decrypted.jpg", "wb") as f:
    f.write(decrypted_data)

print("Image decrypted successfully")
print("Image Encryption Experiment Completed")

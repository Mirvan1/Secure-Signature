# A Mobile Application for Encryption of Signature-Mirvan SADIGLI-20160807004

Abstract
The application ensures that store your signature securely. Besides, you can share your signature with others in an encrypted format. It can be used that the encrypted file 
decrypts to image. The project can only be run on the android-based operating system.

Purpose
 The purpose of the project keeps the signature securely. Because some people do not want to share signature with everyone. Through this project, they can encrypt their signature.
 Besides, they will be able to share it with the people they want. And by not signing on the paper, they will be able to sign directly through the application.

Methodology
The project is written Java language. AES encryption algorithm is used for encryption and decryption of signatures. AES is a symmetric key algorithm and it has 128 bits block size.
AES encryption algorithm has three different key lengths as 128,192 and 256 bits. AES operates on 16 bytes that means a 4x4 matrix in column-major order. It also has a secret key
used for encryption and decryption. It means cipher uses the same key for encryption and decryption. Thus, the sender and receiver know this secret key. In this project, I used 
AES/CBC/PKCS5Padding encryption algorithm which was implemented in Java. CBC is mode and stands for Cipher Block Chaining that uses Initialization Vector to increase encryption.
And it uses plaintext xor with Initialization Vector and encrypts result in ciphertext block until it reaches last ciphertext block using xor.PKCS5 is a padding algorithm. I used
UTF-8 to convert plaintext bytes to string. For encryption method has four parameters like a secret key, spec key, input, and output. These keys in 16 bytes for encryption and 
the first one is spec key used for Initialization Vector. The second one is the secret key I talked about above. These keys are written as a variable. I do not want to ask the
user for generating keys. I think that It can cause confusion for the user. Input parameter takes from drawing signature in the application. The output reads input byte-by-byte.
The generated file is output and you can share a file with others or you can save the file to phone indirectly. I used Canvas class to draw the signature. There are some colors 
like red, green, and blue to draw the signature. The default color is black and you can erase what you drew. So, the drawn signature is sent to the encryption method as a bitmap.
For decryption of the image, you can choose from the phone's storage. And the encrypted image shows as ImageView in the activity. You can save the image to the phone gallery. 
In detail, the decryption method is the same as encryption. The only difference is to change cipher to decrypt mode. You can share the generated file using Gmail or can save
Google drive, etc. For this, I used a File Provider to do it.

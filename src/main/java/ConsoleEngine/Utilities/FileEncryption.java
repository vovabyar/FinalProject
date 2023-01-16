package ConsoleEngine.Utilities;

import ConsoleEngine.Exceptions.FileEncryptionException;

import javax.crypto.Cipher;
import javax.crypto.CipherInputStream;
import javax.crypto.CipherOutputStream;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.io.*;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

public class FileEncryption {

    /**
     * Initial vector key. This key is used as a seed during the encryption process,
     * should be 16 bytes (= 16 chars), it's highly recommended that you change this key.
     * Note: Different keys are most likely to produce different results, so if you encrypt
     * a file using a IV_KEY and then change the key, it's almost guaranteed that you won't be able
     * to decrypt that same file with the new IV_KEY.
     */
    private final static String IV_KEY = "\"bp-<p4*1'XR%aF>";

    /**
     * A String with the transformation that's going to be made while encrypting:
     * <Engine or Algorithm>/<Block Cipher>/<Padding method>
     * Engine: Global transformation
     * Block cipher: Block transformation
     * Padding method: How to handle if a block is not filled
     */
    private final static String TRANSFORMATION = "AES/CBC/PKCS5Padding";

    /**
     * Algorithm used
     */
    private final static String ALGORITHM = "AES";

    /**
     * Max length of the password/key
     */
    private final static int KEY_LENGTH = 16;

    /**
     * Encrypted files extension
     */
    private final static String FILE_EXTENSION = ".encrypted";

    /**
     * Buffer length used while encrypting/decrypting
     */
    private final static int BUFFER_LENGTH = 16 * 1024;

    /**
     * Sets the Utilities.FileEncryption to ENCRYPT_MODE
     */
    public final static byte ENCRYPT_MODE = 0;

    /**
     * Sets the Utilities.FileEncryption to DECRYPT_MODE
     */
    public final static byte DECRYPT_MODE = 1;

    /**
     * Token to use to fill the password in case it's not long enough, password has to
     * be exactly 16 bytes (16 ASCII chars), not more nor less. If the password received on the constructor
     * contains this token, an exception is thrown.
     */
    private static final char PASSWORD_FILL_TOKEN = ' ';

    /**
     * File size in bytes
     */
    private long fileSize;

    /**
     * Counter used to increment the number of bytes processed so it's possible
     * to have an average % of the encryption/decryption process
     */
    private long counter;

    /**
     * File which is going to suffer the changes
     */
    private File file;

    /**
     * Password to be used
     */
    private String password;

    /**
     * Transformation mode, see Utilities.FileEncryption.ENCRYPT_MODE and Utilities.FileEncryption.DECRYPT_MODE
     */
    private byte mode;

    /**
     * Current transformation status. All status on a successful file transformation:
     * "Initializing"
     * "Encrypting/Decrypting"
     * "Canceled"
     * "Successfully encrypted/decrypted"
     * "Wrong password or broken file"
     */
    private String status;

    /**
     * If it was given orders to abort the transformation process
     */
    private boolean aborting;

    /**
     * Creates a new instance of a file encryption/decryption
     * that should be completed by calling start()
     * @param path     String with the ABSOLUTE path to the file that will be
     *                 decrypted/encrypted
     * @param password Password used as the encryption/decryption key
     * @param mode     If it's to Encrypt or to Decrypt, check Utilities.FileEncryption.ENCRYPT_MODE and
     *                 Utilities.FileEncryption.DECRYPT_MODE
     * @throws FileEncryptionException Either if the file doesn't exist or if it was given
     *                                 an invalid mode or the password is invalid
     */
    public FileEncryption(String path, String password, byte mode) throws FileEncryptionException {
        if (password.contains(String.valueOf(PASSWORD_FILL_TOKEN)) || password.length() > KEY_LENGTH) {
            throw new FileEncryptionException("Invalid password");
        }
        this.password = password;
        this.file = new File(path);
        if (!file.exists()) {
            throw new FileEncryptionException("Inexistent file");
        }
        this.fileSize = file.length();
        this.counter = 0;
        if (mode < 0 || mode > 1) {
            throw new FileEncryptionException("Invalid mode");
        }
        this.mode = mode;
        this.aborting = false;
        this.status = "Initializing";
    }

    /**
     * Gets the accurate destination file:
     * Encrypting file x.txt
     * - If the file x.txt.enc already exists, the system
     * - will check if the file "(1) x.txt.enc" exists, if it does
     * - it will check if "(2) x.txt.enc" exists and so on until it reaches a
     * - fileName that doesn't exist yet
     * Decrypting file x.txt.enc
     * - If the file x.txt already exists, the system
     * - will check if the file "(1) x.txt" exists, if it does
     * - it will check if "(2) x.txt" exists and so on until it reaches
     * - a fileName that doesn't exist yet
     *
     * @param fileName the original fileName
     * @return the accurate destination file
     */
    public File getDestinationFile(String fileName, byte mode) {
        File temp = new File(fileName);
            fileName = mode == ENCRYPT_MODE ? createAlternativeFileNameToEncrypt(0) : createAlternativeFileNameToDecrypt(0);
            temp = new File(fileName);

        return temp;
    }

    public File getFile() {
        return file;
    }

    /**
     * Main method to encrypt
     */
    private void encrypt() {
        this.status = "Encrypting";
        FileInputStream fis = null;
        FileOutputStream fos = null;
        CipherOutputStream cout = null;
        try {
            fis = new FileInputStream(file);
            String fileName = file.getAbsolutePath() + FILE_EXTENSION;
            file = getDestinationFile(fileName, mode);
            fos = new FileOutputStream(file);
            byte keyBytes[] = validatePassword().getBytes();
            SecretKeySpec key = new SecretKeySpec(keyBytes, ALGORITHM);
            Cipher cipher = Cipher.getInstance(TRANSFORMATION);
            cipher.init(Cipher.ENCRYPT_MODE, key, new IvParameterSpec(IV_KEY.getBytes()));
            cout = new CipherOutputStream(fos, cipher);
            byte[] buffer = new byte[BUFFER_LENGTH];
            int read;
            while (!aborting && ((read = fis.read(buffer)) != -1)) {
                cout.write(buffer, 0, read);
                counter += read;
            }
            cout.flush();
            status = aborting? "Canceled" : "Successfully encrypted";
        } catch (IOException | InvalidAlgorithmParameterException | InvalidKeyException | NoSuchAlgorithmException | NoSuchPaddingException e) {
            aborting = true;
            status = e.getMessage();
        } finally {
            finish(cout, fos, fis);
        }
    }

    /**
     * Finishes a transformation process by closing all the streams and
     * deleting the file if it was aborted
     * @param closeables the streams
     */
    private void finish(Closeable... closeables) {
        counter = fileSize; // To force the getProgress() method to return 1.0
        for (Closeable closeable : closeables) {
            try {
                closeable.close();
            } catch (Exception e) {
            }
        }
        if (aborting) file.delete(); // If it was aborted the file might be corrupted
    }

    /**
     * Gets the transformation progress:
     * 1.0, if it's complete
     * 0.0, if it didn't start yet
     * 0.5, if it's in the middle of the process
     *
     * @return the transformation progress
     */
    public double getProgress() {
        return 1.0 * counter / fileSize;
    }

    /**
     * Safely aborts the transformation process
     */
    public void abort() {
        this.aborting = true;
    }

    /**
     * Gets the absolute path without the extension
     * Example: example/file.txt > example/file
     * @return The absolute path without the extension
     */
    private String getAbsolutePathWithoutExtension() {
        return file.getAbsolutePath().substring(0, file.getAbsolutePath().lastIndexOf('.'));
    }

    /**
     * Main method to decrypt
     */
    private void decrypt() {
        status = "Decrypting";
        FileInputStream fis = null;
        FileOutputStream fos = null;
        CipherInputStream cin = null;
        try {
            fis = new FileInputStream(file);
            String fileName = getAbsolutePathWithoutExtension();
            file = getDestinationFile(fileName, mode);
            fos = new FileOutputStream(file);
            byte keyPassword[] = validatePassword().getBytes();
            SecretKeySpec key = new SecretKeySpec(keyPassword, ALGORITHM);
            Cipher decrypt = Cipher.getInstance(TRANSFORMATION);
            decrypt.init(Cipher.DECRYPT_MODE, key, new IvParameterSpec(IV_KEY.getBytes()));
            cin = new CipherInputStream(fis, decrypt);
            byte[] buffer = new byte[BUFFER_LENGTH];
            int read;
            while (!aborting && ((read = cin.read(buffer)) > 0)) {
                fos.write(buffer, 0, read);
                counter += read;
            }
            fos.flush();
            status = aborting? "Canceled" : "Successfully decrypted";
        } catch (NoSuchAlgorithmException | InvalidKeyException | NoSuchPaddingException | IOException | InvalidAlgorithmParameterException e) {
            aborting = true;
            status = "Wrong password or broken file";
        } finally {
            finish(fos, cin, fis);
        }
    }

    /**
     * Adds PASSWORD_FILL_TOKEN to fill the given password as
     * the password has to be exactly 16 bytes (16 chars).
     * Example: "test" goes "            test" assuming PASSWORD_FILL_TOKEN
     * value is ' ' (blank space).
     * This method is actually safe since on the constructor an exception is thrown if
     * the given password contains PASSWORD_FILL_TOKEN.
     * @return the renewed password
     */
    private String validatePassword() {
        StringBuilder temp = new StringBuilder(password);
        while (temp.length() < KEY_LENGTH) {
            temp.insert(0, PASSWORD_FILL_TOKEN);
        }
        return temp.toString();
    }

    private String createAlternativeFileNameToEncrypt(int i) {
        StringBuilder sb = new StringBuilder();
        if (file.getParent() != null) { // If the file is not on a relative directory, all the previous directories are added
            sb.append(file.getParent()).append(File.separator); // Like "C:/.../.../"
        }
        // Adds "(i) " on the beggining as well as the extension
        sb.append(file.getName()).append(FILE_EXTENSION);
        return sb.toString();
    }

    private String createAlternativeFileNameToDecrypt(int i) {
        StringBuilder sb = new StringBuilder();
        if (file.getParent() != null) { // If the file is not on a relative directory, all the previous directories are added
            sb.append(file.getParent()).append(File.separator); // Like "C:/.../.../"
        }
        // Adds "(i) " on the beggining as well as removing the extension
        sb.append(file.getName().substring(0, file.getName().lastIndexOf(FILE_EXTENSION)));
        return sb.toString();
    }

    /**
     * Getter for the status
     * @return the status
     */
    public String getStatus() {
        return status;
    }

    /**
     * starts
     */
    public void start() {
        if (mode == ENCRYPT_MODE) {
            encrypt();
        } else if (mode == DECRYPT_MODE) {
            decrypt();
        }
    }
}

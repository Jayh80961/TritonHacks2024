import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;

public class BlockchainCode {
    private static int difficulty = 4;
    private static List<Block> blockchain = new ArrayList<>();
    private static String previousHash = "Genesis";

    public static void main(String[] args) {
        // Your main code for adding blocks is here
    }

    public static String checkHash(BlockData data) throws NoSuchAlgorithmException {
        int nonce = 0;

        String dataStr = data.getName() + data.getUsage() + data.getAmount() + previousHash + nonce;
        String curHash = applySha256(dataStr);

        String nonceCheck = "";
        for (int i = 0; i < difficulty; i++) {
            nonceCheck = nonceCheck + "0";
        }

        while (!curHash.startsWith(nonceCheck)) {
            nonce++;
            dataStr = data.getName() + data.getUsage() + data.getAmount() + previousHash + nonce;
            curHash = applySha256(dataStr);
        }

        // Convert letters in current hash to ASCII, keeping numbers unchanged
        StringBuilder asciiCurHash = new StringBuilder();
        for (int i = 0; i < curHash.length(); i++) {
            char c = curHash.charAt(i);
            if (Character.isLetter(c)) {
                asciiCurHash.append((int) c);
            } else {
                asciiCurHash.append(c);
            }
        }

        // Convert numbers in block to hexadecimal
        String save = data.getAmount();
        String hexAmount = convertToHex(data.getAmount());

        Block block = new Block(data, previousHash, asciiCurHash.toString(), nonce);
        blockchain.add(block);

        // Update the previous hash to ASCII format
        previousHash = asciiCurHash.toString();

        // Remove leading "0000" in the hash
        String strippedHash = asciiCurHash.toString().replaceFirst("^0+", "");

        return "Data: " + data.getName() + ", " + data.getUsage() + ", " + save + "\n" +
               "Previous Hash: " + block.getPrevHash() + "\n" + // Use the block's previous hash
               "Current Hash: " + strippedHash + "\n" +
               "Nonce: " + nonce;
    }

    private static String convertToHex(String decimalString) {
        try {
            int decimalValue = Integer.parseInt(decimalString);
            return Integer.toHexString(decimalValue);
        } catch (NumberFormatException e) {
            // Handle invalid number format
            return "Invalid Number";
        }
    }

    public static String applySha256(String input) throws NoSuchAlgorithmException {
        MessageDigest digest = MessageDigest.getInstance("SHA-256");
        byte[] hash = digest.digest(input.getBytes());

        StringBuilder hexString = new StringBuilder();
        for (byte b : hash) {
            String hex = Integer.toHexString(0xff & b);
            if (hex.length() == 1) {
                hexString.append('0');
            }
            hexString.append(hex);
        }

        return hexString.toString();
    }
}

class Block {
    private BlockData data;
    private String prevHash;
    private String curHash;
    private int nonce;

    public Block(BlockData data, String prevHash, String curHash, int nonce) {
        this.data = data;
        this.prevHash = prevHash;
        this.curHash = curHash;
        this.nonce = nonce;
    }

    public String getPrevHash() {
        return prevHash;
    }
}

class BlockData {
    private String name;
    private String usage;
    private String amount;

    public BlockData(String name, String usage, String amount) {
        this.name = name;
        this.usage = usage;
        this.amount = amount;
    }

    public String getName() {
        return name;
    }

    public String getUsage() {
        return usage;
    }

    public String getAmount() {
        return amount;
    }
}
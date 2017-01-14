import java.nio.file.*;
import javax.xml.bind.annotation.adapters.HexBinaryAdapter;
import java.security.MessageDigest;

public class ComputeSHA
{
   public static void main(String[] args)
   {
        try {
            // Read in file as byte array
            byte[] data = Files.readAllBytes(Paths.get(args[0]));

            // Message Digest to compute SHA1
            MessageDigest md = MessageDigest.getInstance("SHA-1");

            // Compute hash
            byte[] hashbytes = md.digest(data);

            // Convert hash to readable string
            String hash = (new HexBinaryAdapter()).marshal(hashbytes);

            // Print hash
            System.out.println(hash.toLowerCase());
        } catch (Exception e) {
            e.printStackTrace();
        }
   }
}
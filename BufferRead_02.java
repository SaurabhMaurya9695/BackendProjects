import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class BufferRead_02 {
    public static void main(String[] args) throws IOException {

        // 1 - make obj of BufferReader
        // 2 - pass reader in constructor -> new InputStreamReader
        // 3 - use readLine to read The data from system

        BufferedReader buffer = new BufferedReader(new InputStreamReader(System.in));
        String x = buffer.readLine(); // read rauank input as char "r""a""u""n""k"
        System.out.println("value of first input is " + x);
        int t = Integer.parseInt(buffer.readLine()); // "9" , "8" "7"
        System.out.println("value of second input is " + t);

    }
}

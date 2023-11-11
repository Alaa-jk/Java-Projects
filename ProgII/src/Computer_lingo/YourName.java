package Computer_lingo;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class YourName {
    public static void main(String[] args) throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        String name = reader.readLine();
        System.out.println("Your Name" + name);

    }
}

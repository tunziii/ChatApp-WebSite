import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class IOFile {

    public static void WriteFile(String text) {
        try {
            FileWriter writer = new FileWriter("data.txt", true);

            writer.write(text + "\n");
            writer.close();

        } catch (IOException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    public static String ReadFile(String sender, String receiver, boolean lastMsg) {
        try {
            BufferedReader reader = new BufferedReader(new FileReader("data.txt"));
            String line;
            String s = "";

            while ((line = reader.readLine()) != null) {
                String[] split = line.split("\\^");

                if ((sender.equals(split[0]) && receiver.equals(split[1])) || (sender.equals(split[1]) && receiver.equals(split[0]))) {
                        if(lastMsg){s = line;}
                        else if(!lastMsg){s += "\n" + line;}
                }
            }

            reader.close();
            return s;

        } catch (IOException e) {
            System.out.println("Error: " + e.getMessage());
        }
        return null;
    }
}
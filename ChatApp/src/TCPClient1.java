import java.net.*;
import java.io.*;
import java.util.Scanner;

//"C:\Users\tunja\.jdks\openjdk-25.0.1\bin\java" TCPClient
// C:\Users\tunja\IdeaProjects\ChatApp\out\production\Chat App
//"C:\Users\tunja\.jdks\openjdk-25.0.1\bin\java.exe" -cp . TCPClient
// cd /d "C:\Users\tunja\IdeaProjects\ChatApp\out\production\Chat App" && "C:\Users\tunja\.jdks\openjdk-25.0.1\bin\java.exe" -cp . TCPClient

public class TCPClient1 {
	public boolean online = true;
	public boolean start = false;
	public void main (String args[]) {
		// args[0]: Message
		// args[1]: Server

		try{
			int serverPort = 9000;
			Socket s = new Socket ("87.158.227.92", serverPort);
			Scanner scanner = new Scanner(System.in);
			int j = 1;

			DataOutputStream out = new DataOutputStream (s.getOutputStream());
			DataInputStream in = new DataInputStream (s.getInputStream());

			String user = "Test";
			String reciever = "Tuni";

			out.writeUTF(user + "^" + reciever);

			clearScreen();
			Thread recieve = new Thread(() -> {
				try {
					int i;
					while (true) {
						String data = in.readUTF();
						String[] msg1 = data.split("\n");

						for (i = 0; i < msg1.length; i++) {
							String[] msg2 = msg1[i].split("\\^");

							if (msg2[0].equals(user)) {
								System.out.println(msg2[0] + ": " + msg2[2]);
							} else if (msg2[0].equals(reciever)) {
								System.out.println(msg2[0] + ": " + msg2[2]);
							}
						}

						start=true;
					}
				} catch (IOException e) {
					System.out.println("Disconnected from server");};
			});

			recieve.start();

			while(online){
				while(start) {
					String input = scanner.nextLine();
					clearScreen();
					out.writeUTF(user + "^" + reciever + "^" + input);
				}
			}
			s.close();
		}catch (UnknownHostException e){
			System.out.println(" Sock:"+ e.getMessage());
		}catch (EOFException e){ System.out.println(" EOF:"+ e.getMessage());
		}catch (IOException e){ System.out.println(" IO:"+ e.getMessage());}
	}//

	public static void clearScreen() {
		try {
			new ProcessBuilder("cmd", "/c", "cls")
					.inheritIO()
					.start()
					.waitFor();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}// main
}// class

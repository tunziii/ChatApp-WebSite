import java.net.*;
import java.io.*;
import java.util.function.Consumer;

public class TCPClient {
	public boolean online = true;
	public boolean start = false;
	public DataOutputStream out;
	public DataInputStream in;

  public TCPClient() {
    try{
	  int serverPort = 9000;
	  Socket s = new Socket ("87.158.227.92", serverPort);

	  out = new DataOutputStream (s.getOutputStream());
	  in = new DataInputStream (s.getInputStream());

    }catch (UnknownHostException e){
	  System.out.println(" Sock:"+ e.getMessage());
    }catch (IOException e){ System.out.println(" IO:"+ e.getMessage());}
  }// main

	public void connect(Consumer<String> onMessage, String user, String reciever) throws IOException {
	  out.writeUTF(user + "^" + reciever);
		String data = in.readUTF();
		String[] msg1 = data.split("\n");

		for (int i = 0; i < msg1.length; i++) {
			String[] msg2 = msg1[i].split("\\^");

			if (msg2[0].equals(user)) {
				//System.out.println(msg2[0] + ": " + msg2[2]);
				onMessage.accept(msg2[0] + "^" + msg2[2]);
			} else if (msg2[0].equals(reciever)) {
				//System.out.println(msg2[0] + ": " + msg2[2]);
				onMessage.accept(msg2[0] + "^" + msg2[2]);
			}
		}
  }

	public void send(String user, String reciever, String input) throws IOException {out.writeUTF(user + "^" + reciever + "^" + input);}

	public void listenOnce(Consumer<String> onMessage, String user, String reciever) throws IOException{}

	public void listenSingleMsg(Consumer<String> onMsg, String user, String reciever) throws IOException {
	  //THREAD
		while (true) {
			String data = in.readUTF();
			String[] msg = data.split("\\^");

			if(msg[0].equals(user)) {
				//System.out.println(msg2[0] + ": " + msg2[2]);
				onMsg.accept(msg[0] + "^" + msg[2]);
			}
			else if(msg[0].equals(reciever)) {
				//System.out.println(msg2[0] + ": " + msg2[2]);
				onMsg.accept(msg[0] + "^" + msg[2]);
			}

		}
	}
}// class

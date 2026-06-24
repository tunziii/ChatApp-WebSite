
import java.net.*;
import java.io.*;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

//Normal chat - saved in database(txt file)
//Private chat - end to end, not in database, messages disappear when client is closed
//Login or Register(user names)
//encryption
//Database SQL
//UI and Website

class Clients{
    public static List<Connection> clients = new CopyOnWriteArrayList<>();
}

public class TCPServer {
    public String publicIP = "87.158.227.92";
    public String privateI = "192.168.2.148";
    public int serverPort = 9000;
    Clients clients = new Clients(); //CopyOnWrite because multiple threads will access it!

  public void main (String args[]) {
    try{
      System.out.println("The Server is running");
	  ServerSocket listenSocket = new ServerSocket(serverPort);

	  while(true) { //Listen for messages on the socket
	    Socket clientSocket = listenSocket.accept();
	    System.out.println("New Connection");
	    Connection c = new Connection(clientSocket);
	  }
    } catch( IOException e) {System.out.println(" Listen :"+ e.getMessage());}
  }// main
}//class


class Connection extends Thread {
  DataInputStream in;
  DataOutputStream out;
  Socket clientSocket;
  String username;
  String chatUsers;

  public Connection (Socket aClientSocket) {
    try {
      clientSocket = aClientSocket;
      out = new DataOutputStream ( clientSocket.getOutputStream() );
      in = new DataInputStream ( clientSocket.getInputStream() );

      chatUsers = in.readUTF();
      String[] users = chatUsers.split("\\^");
      username = users[0];
      Clients.clients.add(this);
      PrintMsg(users, false);
      this.start();

    } catch( IOException e) {System.out.println(" Connection:"+ e.getMessage());}
  }

  public void run() {
      while (true) {
          try {
              String data = in.readUTF();
              String[] msg = data.split("\\^"); //Split message
              IOFile.WriteFile(data);

              PrintMsg(msg, true);

          } catch (IOException e) {
              throw new RuntimeException(e);
          }
      }
  }

  public void PrintMsg(String[] msg, boolean lastMsg){
      try {
          for(Connection c : Clients.clients) {
              if(c != null && (c.username.equals(msg[0]) || c.username.equals(msg[1]))) {
                  System.out.println(c.username);
                  String send = IOFile.ReadFile(msg[0], msg[1], lastMsg); //Read from "database"
                  c.out.writeUTF(send);
                  System.out.println("Sent data: ");//send to user
              }
          }

      } catch (IOException e) {
          System.out.println(" IO:" + e.getMessage());
      }
  }
}


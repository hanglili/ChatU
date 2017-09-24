package chatU;

import java.net.*;
import java.io.*;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class ChatServer {

  private PasswordEncoder encoder = new PasswordEncoder();
  public static final List<User> users = new ArrayList<>();

  public ChatServer (int port) throws IOException {
    ServerSocket server = new ServerSocket(port);
    users.add(new User("Hang", encoder.encode("123")));
    while(true) {
      Socket client = server.accept();
      System.out.println("Accepted from " + client.getInetAddress());
      ChatHandler c = new ChatHandler(client);
      c.start();
    }
  }

  public static void main(String args[]) throws IOException {
    if (args.length != 1)
      throw new RuntimeException("Syntax: chatU.ChatServer <port>");
    new ChatServer(Integer.parseInt(args[0]));
  }

}
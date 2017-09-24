package chatU;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.net.Socket;
import java.util.Map;
import java.util.Scanner;

import static chatU.ChatServer.users;

public class Verification {

  private JFrame frame;
  private PasswordEncoder encoder = new PasswordEncoder();

  public Verification() {}

  public void verify() {
    frame = new JFrame("chatU.Verification");
    frame.setLayout(new GridLayout(2, 2));

    users.add(new User("Hang", encoder.encode("123")));
    users.add(new User("Ling", encoder.encode("234")));

    final JTextField user = new JTextField();
    final JTextField pass = new JTextField();

    user.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        pass.requestFocus();
      }
    });

    pass.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        String username = user.getText();
        String password = pass.getText();
        System.out.println("Do auth with " + username);
        for (User u : users) {
          if ((username.compareTo(u.getUsername()) == 0)  &&
              (encoder.encode(password) == u.getPassword())) {
            frame.setVisible(false);
            System.out.println("VERIFICATION PASSED WELCOME TO THE SYSTEM!");
//            Scanner scanner = new Scanner(System.in);
//            String arg0 = scanner.nextLine();
//            String arg1 = scanner.nextLine();
            try {
              Socket s = new Socket("localhost", Integer.parseInt("8000"));
              new ChatClient ("Chat " + "localhost" + ":" + "8000",
                  s.getInputStream(), s.getOutputStream(), u);
            } catch (IOException e1) {
              e1.printStackTrace();
            }
          }
        }
      }
    });

    frame.add(new JLabel("chatU.User:"));
    frame.add(user);

    frame.add(new JLabel("Password:"));
    frame.add(pass);

    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    frame.pack();
    frame.setVisible(true);
  }

}

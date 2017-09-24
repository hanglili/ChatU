package chatU;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.net.*;
import java.io.*;
import java.awt.*;
import java.util.Date;

public class ChatClient extends JFrame implements Runnable {

  protected DataInputStream i;
  protected DataOutputStream o;
  protected JTextArea output;
  protected JTextField input;
  protected Thread listener;
  protected User user;

  public ChatClient (String title, InputStream i, OutputStream o, User user) {
    super(title);
    this.i = new DataInputStream (new BufferedInputStream(i));
    this.o = new DataOutputStream (new BufferedOutputStream(o));
    this.user = user;
    setLayout(new BorderLayout());

//    JLabel enterYourName = new JLabel("Enter Your Name Here:");
//    JTextField textBoxToEnterName = new JTextField(21);
//    JPanel panelTop = new JPanel();
//    panelTop.add(enterYourName);
//    panelTop.add(textBoxToEnterName);

    add("Center", output = new JTextArea());
    output.setEditable(false);
    add("South", input = new JTextField());
    input.getInputMap().put(KeyStroke.getKeyStroke("ENTER"), "input");
    input.getActionMap().put("input", new Writing());

    addWindowListener(new WindowAdapter() {
      @Override
      public void windowClosing(WindowEvent e) {
        System.exit(0);
      }
    });

    pack();
    setVisible(true);
    input.requestFocus();
    listener = new Thread (this);
    listener.start();
  }

  @Override
  public void run() {
    try {
      while (true) {
        String line = i.readUTF();
        output.append(line + "\n");
      }
    } catch (IOException ex) {
      ex.printStackTrace ();
    } finally {
      listener = null;
      input.hide();
      validate();
      try {
        o.close ();
      } catch (IOException ex) {
        ex.printStackTrace ();
      }
    }
  }

//  public boolean handleEvent(Event e) {
//    if ((e.target == input) && (e.id == Event.ACTION_EVENT)) {
//      try {
//        System.out.println("Hi");
//        o.writeUTF((String) e.arg);
//        o.flush ();
//        output.append(((String) e.arg) + "\n");
//      } catch (IOException ex) {
//        ex.printStackTrace();
//        listener.stop();
//      }
//      input.setText("");
//      return true;
//    } else if ((e.target == this) && (e.id == Event.WINDOW_DESTROY)) {
//      if (listener != null)
//        listener.stop();
//      hide ();
//      return true;
//    }
//    return super.handleEvent(e);
//  }

  public static void main(String args[]) throws IOException {
    if (args.length != 2)
      throw new RuntimeException ("Syntax: chatU.ChatClient <host> <port>");
    Verification verification = new Verification();
    verification.verify();
  }

  private class Writing extends AbstractAction {

    @Override
    public void actionPerformed(ActionEvent e) {
      try {
        JTextField origin = (JTextField) e.getSource();
        Date date = new Date();
        o.writeUTF(user.getUsername() + " -- " + date.toString());
        o.writeUTF("-" + origin.getText());
        o.flush();
      } catch (IOException ex) {
        ex.printStackTrace();
        listener.stop();
      }
      input.setText("");
    }
  }

//  private class Close extends AbstractAction {
//  }
}
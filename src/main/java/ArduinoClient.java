package main.java;


import javax.swing.*;
import java.awt.*;
import java.io.PrintWriter;
import java.net.Socket;

public class ArduinoClient {

    private JFrame frame;
    //private JTextArea area;
    private JButton onButton;
    private JButton offButton;
    private JButton exitButton;
    Socket socket;
    PrintWriter writer;
    public static void  main(String[] args){
        ArduinoClient client = new ArduinoClient();
        client.setUpNetworking();
        client.gui();
    }

    public void gui(){
        frame = new JFrame("ArduinoClient");
        //area = new JTextArea();
        onButton = new JButton("ON");
        offButton = new JButton("OFF");
        exitButton = new JButton("EXIT");

        frame.getContentPane().add(BorderLayout.CENTER,onButton);
        frame.getContentPane().add(BorderLayout.WEST,offButton);
        frame.getContentPane().add(BorderLayout.EAST,exitButton);

        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(500,500);
        frame.setVisible(true);

        onButton.addActionListener(event -> {
            writer.println("on");
            writer.flush();
        });

        offButton.addActionListener(event -> {
            writer.println("off");
            writer.flush();
        });

        exitButton.addActionListener(event -> {
            writer.println("exit");
            writer.flush();
            try {
                socket.close();
            }catch (Exception e){e.printStackTrace();}
        });
    }

    public void setUpNetworking(){
        try{
            socket = new Socket("127.0.0.1",7000);
            writer = new PrintWriter(socket.getOutputStream());
        }catch(Exception e){e.printStackTrace();}

    }




}

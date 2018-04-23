package main.java;


import javax.swing.*;
import java.awt.*;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.nio.Buffer;

public class ArduinoClient {
    BufferedReader reader;
    private JFrame frame;
    //private JTextArea area;
    private JButton onButton;
    private JButton offButton;
    private JButton exitButton;
    private JTextArea outWindow;
    //private JTextArea inWindow;
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
        outWindow = new JTextArea();
        //inWindow = new JTextArea();
        JScrollPane scroller = new JScrollPane(outWindow);
        outWindow.setLineWrap(true);
        scroller.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);
        scroller.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);

        frame.getContentPane().add(BorderLayout.CENTER,scroller);
        frame.getContentPane().add(BorderLayout.WEST,offButton);
        frame.getContentPane().add(BorderLayout.EAST,exitButton);
        frame.getContentPane().add(BorderLayout.SOUTH,onButton);

        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(500,500);
        frame.setVisible(true);

        //Второй поток для считывания
        Thread thread = new Thread(new IncomingReader());
        thread.start();

        onButton.addActionListener(event -> {
            writer.println("on");
            writer.flush();
            try {
                Thread.sleep(20);
            } catch (Exception er){er.printStackTrace();}
        });

        offButton.addActionListener(event -> {
            writer.println("off");
            writer.flush();
            try {
                Thread.sleep(20);
            } catch (Exception er){er.printStackTrace();}
        });

        exitButton.addActionListener(event -> {
            writer.println("exit");
            writer.flush();
            try {
                socket.close();
            }catch (Exception e){e.printStackTrace();}
        });

        /*
        TODO remove this while
         */
        while (true)
        {
            try {
                System.out.println("Message from StabServer: " + reader.readLine());
            } catch (Exception er){er.printStackTrace();}
        }

    }

    public void setUpNetworking(){
        try{
            //socket = new Socket("77.37.181.102",7010);
            socket = new Socket("127.0.0.1",7020);
            System.out.println(socket.isConnected());
            writer = new PrintWriter(socket.getOutputStream());
            InputStreamReader streamReader = new InputStreamReader(socket.getInputStream());
            reader = new BufferedReader(streamReader);
            System.out.println("networking established");
        }catch(Exception e){e.printStackTrace();}

    }

    public class IncomingReader implements Runnable{
        /*BufferedReader reader;
        public IncomingReader(BufferedReader reader){
            this.reader = reader;
        }*/
        String message;
        public void run(){

            try{
                while((message = reader.readLine())!= null){
                    System.out.print("Сообщение с Ардуино: " + message);
                    outWindow.append("Сообщение с Ардуино: " + message + "\n");
                    Thread.sleep(100);
                }
            }catch (Exception r){r.printStackTrace();}
        }
    }




}

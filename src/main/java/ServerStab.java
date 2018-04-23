package main.java;


//import arduino.Arduino;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

public class ServerStab {
    int serverPort = 7020;
    String message;

    public static void main(String[] args) throws InterruptedException {
        ServerStab server = new ServerStab();
        server.go();
    }

    public void go() {
        try {
            // 1. Организовываем соединение с клиентом:
            ServerSocket socket = new ServerSocket(serverPort);
            Socket clientSocket = socket.accept();
            System.out.println("Выполнено подключение клиента...");
            System.out.println("IP: "+ socket.getInetAddress());
            System.out.println("");
            PrintWriter writer = new PrintWriter(clientSocket.getOutputStream());
            InputStreamReader isReader = new InputStreamReader(clientSocket.getInputStream());
            BufferedReader reader = new BufferedReader(isReader);
            String message;
            // Указываем на каком порту подключён Arduino
            //Arduino arduino = new Arduino("COM5", 9600);
            //Открываем подключение к arduino по указанному порту
            //boolean connected = arduino.openConnection();
            //System.out.println("Соединение установлено: " + connected);
            //String ardMessage;

            //Добавляем второй поток для считывания с ардуино
            //Thread thread = new Thread(new ArduinoServer.ClientHandler(reader,arduino));
            //thread.start();

            /*
             * Вернуть условие while((ardMessage = arduino.serialRead())!=null){
             */
            //
            //while((ardMessage = arduino.serialRead())!=null){
            //добавлено две строчки вместе
            //while (true){
            //    ardMessage = arduino.serialRead();

            while((message = reader.readLine())!=null) {
                //while(true){
                //ardMessage = arduino.serialRead();
                System.out.println("Пришло сообщение: " + message);
                writer.print(message);
                writer.flush();
                //System.out.println("Пришло сообщение с Arduino: "+ardMessage);
                // Use "" instead of null
            }

        } catch (Exception er) {
            er.printStackTrace();
        }
    }



}

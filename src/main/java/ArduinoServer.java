package main.java;


import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * Импорт для Arduino
 */
import arduino.Arduino;

public class ArduinoServer {
    int serverPort = 7010;
    String message;

    public static void main(String[] args) throws InterruptedException {
        ArduinoServer server = new ArduinoServer();
        server.go();
    }

    public void go() {
        //String message;
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

            // Указываем на каком порту подключён Arduino
            Arduino arduino = new Arduino("COM5", 9600);
            //Открываем подключение к arduino по указанному порту
            boolean connected = arduino.openConnection();
            System.out.println("Соединение установлено: " + connected);
            String ardMessage;

            //Добавляем второй поток для считывания с ардуино
            Thread thread = new Thread(new ClientHandler(reader,arduino));
            thread.start();

            /*
             * TODO вернуть условие while((ardMessage = arduino.serialRead())!=null){
             */
            //
            //while((ardMessage = arduino.serialRead())!=null){
            //добавлено две строчки вместе
            while (true){
                ardMessage = arduino.serialRead();

                System.out.println("Пришло сообщение с Arduino: "+ardMessage);
                if (ardMessage!=null){
                    writer.println("Пришло NOT NULL сообщение с ардуино:");

                    /*
                     * TODO поправить опечатку --> использовать "ardMessage" instead of "messsage"
                     */

                    writer.println(message);
                    writer.flush();
                    //Добавлен sleep для смены потока, чтобы не засиживался
                    //Thread.sleep(100);
                }
                else {
                    writer.println("Пришло NULL сообщение с ардуино!");
                    writer.flush();
                }
            }

        } catch (Exception er) {
            er.printStackTrace();
        }
    }

    public class ClientHandler implements Runnable{
        Socket sock;
        BufferedReader reader;
        Arduino arduino;
        public ClientHandler (BufferedReader reader,Arduino arduino){
            this.arduino = arduino;
            this.reader = reader;
        }

        public void run(){

            try {
                while ((message = reader.readLine()) != null) {
                    //Просто передаём строку, которую прислал сервер.
                    arduino.serialWrite(message);
                    Thread.sleep(100);
                }
            } catch (Exception e){e.printStackTrace();}
        }
    }
}

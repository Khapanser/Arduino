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
    static int count = 0;

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


            //while((ardMessage = arduino.serialRead())!=null){
            //добавлено две строчки вместе
            while (true){
                ardMessage = arduino.serialRead();

                System.out.println("Пришло сообщение с Arduino: "+ardMessage);
                if (ardMessage!=null){
                    writer.println("Пришло NOT NULL сообщение с ардуино:");
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


   /*
            while ((message = reader.readLine()) != null) {


                 //Код для отправки на Arduino

                System.out.println("С клиента получено: \"" + message + "\"");
                boolean connected = arduino.openConnection();
                System.out.println("Соединение установлено: " + connected);
                //Уменьшил с 2000 до 10
                Thread.sleep(10);

                switch (message) {
                    case "on":
                        //arduino.serialWrite('k');
                        arduino.serialWrite("NIKITA ");
                        break;
                    case "off":
                        //arduino.serialWrite('1');
                        arduino.serialWrite("KAPILA!");
                        break;
                    case "exit":
                        arduino.serialWrite('0');
                        arduino.closeConnection();
                }

            }
*/
        } catch (Exception er) {
            er.printStackTrace();
        }
    }

    public class ClientHandler implements Runnable{
        Socket sock;
        BufferedReader reader;
        Arduino arduino;
        public ClientHandler (Socket clientSocket){
            sock = clientSocket;
        }
        public ClientHandler (BufferedReader reader,Arduino arduino){
            this.arduino = arduino;
            this.reader = reader;
        }

        public void run(){

            try {
                while ((message = reader.readLine()) != null) {
                    /*switch (message) {
                        case "on":
                            //arduino.serialWrite('0');
                            arduino.serialWrite("NIKITA ");
                            break;
                        case "off":
                            //arduino.serialWrite('1');
                            arduino.serialWrite("KAPILA!");
                            break;
                        case "exit":
                            arduino.serialWrite('0');
                            arduino.closeConnection();
                    }*/
                    //Просто передаём строку, которую прислал сервер.
                    System.out.println("Сейчас работает поток: " + Thread.currentThread());
                    count++;
                    System.out.println("Он работает " +  count+" раз");
                    arduino.serialWrite(message);

                    Thread.sleep(100);
                    //break;
                    //

                }
            } catch (Exception e){e.printStackTrace();}
        }
    }
}

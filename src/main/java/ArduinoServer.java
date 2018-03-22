package main.java;


import java.io.BufferedReader;
import java.io.InputStreamReader;
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
            ServerSocket socket = new ServerSocket(serverPort);
            Socket clientSocket = socket.accept();
            System.out.println("Выполнено подключение клиента...");
            System.out.println("IP: "+ socket.getInetAddress());
            System.out.println("");
            //PrintWriter writer = new PrintWriter(clientSocket.getOutputStream());
            InputStreamReader isReader = new InputStreamReader(clientSocket.getInputStream());
            BufferedReader reader = new BufferedReader(isReader);

            // Указываем на каком порту подключён Arduino

            Arduino arduino = new Arduino("COM4", 9600);

            while ((message = reader.readLine()) != null) {

                /**
                 * Код для отправки на Arduino
                 */
                System.out.println("С клиента получено: \"" + message + "\"");
                boolean connected = arduino.openConnection();
                System.out.println("Соединение установлено: " + connected);

                Thread.sleep(2000);

                switch (message) {
                    case "on":
                        arduino.serialWrite('1');
                        break;
                    case "off":
                        arduino.serialWrite('0');
                        break;
                    case "exit":
                        arduino.serialWrite('0');
                        arduino.closeConnection();
                }

                //}catch (Exception ee){ee.printStackTrace();}
            }

        } catch (Exception er) {
            er.printStackTrace();
        }
    }
}

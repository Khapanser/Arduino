package main.java;


import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * TODO вернуть импорт для Arduino
 *
 * import arduino.Arduino;
 */


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

            //Добавим второй сервер:
            Socket socket2 = new Socket("127.0.0.1",7020);
            PrintWriter writer2 = new PrintWriter(socket2.getOutputStream());
            InputStreamReader streamReader2 = new InputStreamReader(socket2.getInputStream());
            BufferedReader reader2 = new BufferedReader(streamReader2);
            System.out.println("networking established");


            // Указываем на каком порту подключён Arduino
/* TODO раскомментировать
            Arduino arduino = new Arduino("COM5", 9600);
            boolean connected = arduino.openConnection();
            System.out.println("Соединение установлено: " + connected);
            String ardMessage;
*/
            //Добавляем второй поток для считывания с ардуино
            Thread thread = new Thread(new ClientHandler(reader,writer2));
            thread.start();
//try{
            //while((ardMessage = arduino.serialRead())!=null){
            String message2;
            while ((message2 = reader2.readLine())!=null ){
                writer.print(message2);
                writer.flush();
               // while(true){
/*     TODO раскомментировать
                    ardMessage = arduino.serialRead();
                System.out.println("Пришло сообщение с Arduino: "+ardMessage);
                if (ardMessage!=""){
                    writer.println("Пришло NOT NULL сообщение с ардуино:");
                    writer.println(ardMessage);
                    writer.flush();
                }
                else {
                    writer.println("Пришло NULL сообщение с ардуино!");
                    writer.flush();
                    }
*/
            }

        } catch (Exception er) {
            er.printStackTrace();
        }
    }

    public class ClientHandler implements Runnable{
        Socket sock;
        BufferedReader reader;
        PrintWriter writer;
        /* TODO раскомментировать
        Arduino arduino;
        */
        public ClientHandler (BufferedReader reader,  PrintWriter writer){
            this.reader = reader;
            this.writer = writer;
        }

        public void run(){
            try {
                while ((message = reader.readLine()) != null) {
                    //Просто передаём строку, которую прислал сервер.
                    //arduino.serialWrite(message);
                    writer.print(message);
                    writer.flush();

                    Thread.sleep(100);
                }
            } catch (Exception e){e.printStackTrace();}
        }
    }
}

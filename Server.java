
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

public class Server {

    private static final String TAG = "Server.java";

    private ServerSocket serverSocket;
    private static final int port = 50000;

    private ArrayList<UserInfo> UserInfos;

    public static void main(String[] args) {
        new Server();
    }

    public Server() {
        try {
            UserInfos = DBManager.getUserInfos();
            UserInfos.get(0).print();
            serverSocket = new ServerSocket(port);
            SendThread thread = new SendThread();
            thread.start();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    class SendThread extends Thread {
        @Override
        public void run() {
            try {
                System.out.println("Before serverSocket.accept()...");
                Socket socket = serverSocket.accept();
                System.out.println("After serverSocket.accept()...");

                ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());
                oos.writeObject(UserInfos);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}

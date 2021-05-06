import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Scanner;

public class First {

    private ServerSocket server;

    //사용자 객체들을 관리하는 ArrayList
    private ArrayList<UserClass> user_list;

    private UserClass user;

    private static ArrayList<UserInfo> UserInfos;

    public static void main(String[] args) {
        // TODO Auto-generated method stub

        new First();
    }

    //메인메소드가 static으로 되어있기 때문에 다른것들을 다 static 으로 하기 귀찮기 때문에
    // 따로 생성자를 만들어서 진행 - > 메인에서는 호출정도의 기능만 구현하는게 좋다.
    public First() {
        try {
            user_list = new ArrayList<UserClass>();
            // 서버 가동
            server = new ServerSocket(50000);
            // 사용자 접속 대기 스레드 가동
            ConnectionThread thread = new ConnectionThread();
            thread.start();


        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    // 사용자 접속 대기를 처리하는 스레드 클래스
    class ConnectionThread extends Thread {

        @Override
        public void run() {
            try {
                System.out.println("사용자 접속 대기");

                while(true) {
                    Socket socket = server.accept();
                    // 사용자 닉네임을 처리하는 스레드 가동
                    NickNameThread thread = new NickNameThread(socket);
                    thread.start();
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    // 닉네임 입력처리 스레드
    class NickNameThread extends Thread {
        private Socket socket;
        public NickNameThread(Socket socket) {
            this.socket = socket;
        }

        public void run() {
            try {
                // 스트림 추출
                InputStream is = socket.getInputStream();
                OutputStream os = socket.getOutputStream();
                DataInputStream dis = new DataInputStream(is);
                DataOutputStream dos = new DataOutputStream(os);

                //닉네임 수신
                String nickName = dis.readUTF();
                // 환영 메세지를 전달한다.
                dos.writeUTF(nickName + " 님 환영합니다.");
                System.out.println(nickName+"님이 접속하였습니다.");
                // 사용자 정보를 관리하는 객체를 생성한다.
                user = new UserClass(nickName, socket);
                user.start();
                user_list.add(user);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    // 사용자 정보를 관리하는 클래스
    class UserClass extends Thread {
        String nickName;
        Socket socket;
        ObjectInputStream ois;
        ObjectOutputStream oos;

        public UserClass(String nickName, Socket socket) {
            try {
                this.nickName = nickName;
                this.socket = socket;
                ois = new ObjectInputStream(socket.getInputStream());
                oos = new ObjectOutputStream(socket.getOutputStream());

            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        // 사용자로부터 메세지를 수신받는 스레드
        public void run() {
            try {
                while (true) {
                    //클라이언트에게 메세지를 수신받는다.
                    String msg = ois.readUTF();
                    // 사용자들에게 메세지를 전달한다
                    sendToClient(nickName + " : " + msg);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        public synchronized void sendToClient(String msg) {
            try {
                // 사용자의 수만큼 반복
                for (UserClass user : user_list) {
                    // 메세지를 클라이언트들에게 전달한다.
                    user.oos.writeUTF(msg);

                }
                System.out.println(msg);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
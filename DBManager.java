import java.sql.*;
import java.util.ArrayList;

public class DBManager {
    private String JDBC_DRIVER = "com.mysql.jdbc.Driver"; //드라이버
    private String DB_URL = "jdbc:mysql://127.0.0.1/coronaproject?&useSSL=false"; //접속할 DB 서버

    private String USER_NAME = "root"; //DB에 접속할 사용자 이름
    private String PASSWORD = "1z2x3c4A5S6D!0"; //사용자의 비밀번호

    private static ArrayList<UserInfo> UserInfos = new ArrayList<>();

    public DBManager(){
        Connection conn = null;
        Statement state = null;
        try{
            Class.forName(JDBC_DRIVER);
            conn = DriverManager.getConnection(DB_URL, USER_NAME, PASSWORD);
            state = conn.createStatement();

            String sql = "select * from user_info";
            ResultSet rs = state.executeQuery(sql);
            while(rs.next()){
                int p_id = rs.getInt("p_id");
                String login_id = rs.getString("login_id");
                String login_password = rs.getString("login_password");
                String name = rs.getString("name");
                String working_building = rs.getString("working_building");
                int working_floor = rs.getInt("working_floor");
                Boolean self_monitor = rs.getBoolean("self_monitor");

                UserInfo userInfo = new UserInfo(p_id, login_id, login_password, name, working_building, working_floor, self_monitor);
                UserInfos.add(userInfo);
            }

            rs.close();
            state.close();
            conn.close();
        } catch (ClassNotFoundException | SQLException e){
            e.printStackTrace();
        } finally {
            try{
                if(state != null) {
                    state.close();
                }
                if(conn != null){
                    conn.close();
                }
            } catch (SQLException e){
                e.printStackTrace();
            }
        }
    }

    public static ArrayList<UserInfo> getUserInfos(){
        new DBManager();
        return UserInfos;
    }
}

import java.sql.*; // sql library 사용하기
import java.util.ArrayList;
import java.util.List;

public class DBManager {
    private String JDBC_DRIVER = "com.mysql.jdbc.Driver"; //드라이버
    private String DB_URL = "jdbc:mysql://10.244.208.151:3306/coronaproject?&useSSL=false"; //접속할 DB 서버

    private String USER_NAME = "jinah"; //DB에 접속할 사용자 이름
    private String PASSWORD = ""; //사용자의 비밀번호

    private static ArrayList<UserInfo> UserInfos = new ArrayList<>();

    public DBManager() {
        Connection conn = null;
        Statement state = null;
        try { //Reflection 방식
            Class.forName(JDBC_DRIVER);
            conn = DriverManager.getConnection(DB_URL, USER_NAME, PASSWORD); //db 내의 데이터를 저장
            state = conn.createStatement(); //sql 문을 실행하기 위해 conn 연결 정보를 state로 생성

            String sql;
            sql = "select * from user_info";
            ResultSet rs = state.executeQuery(sql); // sql 실행결과를 rs에 저장
            while (rs.next()) {
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

        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        } finally {
            try{
                if(state != null)
                    state.close();
            } catch(SQLException e){
                e.printStackTrace();
            }
            try{
                if(conn != null)
                    conn.close();
            }catch (SQLException e){ }
        }
    }

    public static ArrayList<UserInfo> getUserInfos(){
        new DBManager();
        return UserInfos;
    }
}

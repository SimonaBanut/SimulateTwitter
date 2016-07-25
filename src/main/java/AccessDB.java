import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by icondor on 16/07/16.
 */
public class AccessDB {
    public static void addTweet(String textToTweet) throws ClassNotFoundException, SQLException {
    // 1. load driver
    Class.forName("org.postgresql.Driver");

    // 2. define connection params to db
        final String URL = "jdbc:postgresql://54.93.65.5:5432/4_Simona";
        final String USERNAME = "fasttrackit_dev";
        final String PASSWORD = "fasttrackit_dev";

    // 3. obtain a connection
    Connection conn = DriverManager.getConnection(URL, USERNAME, PASSWORD);

    // 4. create a query statement
    PreparedStatement pSt = conn.prepareStatement("INSERT INTO tweet (content, insertdate ) VALUES (?,now())");
    pSt.setString(1, textToTweet);

    // 5. execute a prepared statement
    int rowsInserted = pSt.executeUpdate();

    // 6. close the objects
    pSt.close();
    conn.close();
}
    public static List readTweets() throws ClassNotFoundException, SQLException {
        // 1. load driver
        Class.forName("org.postgresql.Driver");

        // 2. define connection params to db
        final String URL = "jdbc:postgresql://54.93.65.5:5432/4_Simona";
        final String USERNAME = "fasttrackit_dev";
        final String PASSWORD = "fasttrackit_dev";

        // 3. obtain a connection
        Connection conn = DriverManager.getConnection(URL, USERNAME, PASSWORD);

        // 4. create a query statement
        Statement st = conn.createStatement();

        // 5. execute a query
        ResultSet rs = st.executeQuery("SELECT id,content,insertdate FROM tweet order by insertdate desc");

        // 6. iterate the result set and print the values

        List<TweetBean> listOfTweets = new ArrayList();
        while (rs.next()) {

            // creez un bean de tipul tweetbean , adica un rand din db
            TweetBean tb = new TweetBean();
            tb.setId(rs.getLong("id"));
            tb.setContent(rs.getString("content").trim());
            tb.setInsertDate(rs.getDate("insertdate"));

            //scriu obiectul(randul) in lista
            listOfTweets.add(tb);

        }

        System.out.println("dimensiunea listei:"+listOfTweets.size());
        // 7. close the objects
        rs.close();
        st.close();
        conn.close();

        return listOfTweets;
    }
    public static int isUserInDB(String user, String passwd) throws ClassNotFoundException, SQLException {
        // 1. load driver
        Class.forName("org.postgresql.Driver");
        int userid =-1;
        String ppp= null;
        try {
            ppp = hashString(passwd);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        System.out.println("parola" + ppp);

        // 2. define connection params to db
        final String URL = "jdbc:postgresql://54.93.65.5:5432/4_Simona";
        final String USERNAME = "fasttrackit_dev";
        final String PASSWORD = "fasttrackit_dev";

        // 3. obtain a connection
        Connection conn = DriverManager.getConnection(URL, USERNAME, PASSWORD);

        // 4. create a query statement
        Statement st = conn.createStatement();

        // 5. execute a query
        ResultSet rs = st.executeQuery("SELECT * FROM users where nume='"+user+"' and parola='"+ppp+"'");

        // 6. iterate the result set and print the values

        while (rs.next()) {
            userid=rs.getInt("idu");
        }

        // 7. close the objects
        rs.close();
        st.close();
        conn.close();

        return userid;
    }



    public static List readMyFriends(long myID) throws ClassNotFoundException, SQLException {
        // 1. load driver
        Class.forName("org.postgresql.Driver");

        // 2. define connection params to db
        final String URL = "jdbc:postgresql://54.93.65.5:5432/4_Simona";
        final String USERNAME = "fasttrackit_dev";
        final String PASSWORD = "fasttrackit_dev";

        // 3. obtain a connection
        Connection conn = DriverManager.getConnection(URL, USERNAME, PASSWORD);

        // 4. create a query statement
        Statement st = conn.createStatement();

        // 5. execute a query
        ResultSet rs = st.executeQuery("SELECT * FROM tweet where iduser in (select idfriends from relations where idowner="+myID+" ) order by insertdate desc ");

        // 6. iterate the result set and print the values

        List<TweetBean> listOfTweets = new ArrayList();
        while (rs.next()) {

            // creez un bean de tipul tweetbean , adica un rand din db
            TweetBean tb = new TweetBean();
            tb.setId(rs.getLong("id"));
            tb.setContent(rs.getString("content").trim());
            tb.setInsertDate(rs.getDate("insertdate"));

            //scriu obiectul(randul) in lista
            listOfTweets.add(tb);

        }

        System.out.println("dimensiunea listei:"+listOfTweets.size());
        // 7. close the objects
        rs.close();
        st.close();
        conn.close();

        return listOfTweets;
    }

    public static boolean demoSave(String nume, String parola) throws ClassNotFoundException, SQLException {
    // 1. load driver
        boolean ss;
        String passwHash= null;
        try {
            passwHash = hashString(parola);

        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        System.out.println("parola" + passwHash);
        Class.forName("org.postgresql.Driver");

        // 2. define connection params to db
        final String URL = "jdbc:postgresql://54.93.65.5:5432/4_Simona";
        final String USERNAME = "fasttrackit_dev";
        final String PASSWORD = "fasttrackit_dev";

        // 3. obtain a connection
        Connection conn = DriverManager.getConnection(URL, USERNAME, PASSWORD);

        // 4. create a query statement
        PreparedStatement pSt = conn.prepareStatement("INSERT INTO users (nume,parola) VALUES (?,?)");
        pSt.setString(1, nume);
        pSt.setString(2, passwHash);
        ss = true;
        // 5. execute a prepared statement
        int rowsInserted = pSt.executeUpdate();

        // 6. close the objects
        pSt.close();
        conn.close();

        return ss;
    }

    public static String hashString(String parola) throws NoSuchAlgorithmException {
        byte[] hash = null;
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            hash = md.digest(parola.getBytes());

        } catch (NoSuchAlgorithmException e) { e.printStackTrace(); }
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < hash.length; ++i) {
            String hex = Integer.toHexString(hash[i]);
            if (hex.length() == 1) {
                sb.append(0);
                sb.append(hex.charAt(hex.length() - 1));
            } else {
                sb.append(hex.substring(hex.length() - 2));
            }
        }
        return sb.toString();
    }
    public static boolean follow (int myid, String nameOfMyFriend) throws ClassNotFoundException, SQLException {
        // 1. load driver
        int myFriendID = -1;
        boolean okk = false;
        Class.forName("org.postgresql.Driver");

        // 2. define connection params to db
        final String URL = "jdbc:postgresql://54.93.65.5:5432/4_Simona";
        final String USERNAME = "fasttrackit_dev";
        final String PASSWORD = "fasttrackit_dev";

         Connection conn = DriverManager.getConnection(URL, USERNAME, PASSWORD);
        Statement st = conn.createStatement();
        ResultSet rs = st.executeQuery("SELECT idu FROM users where nume='"+nameOfMyFriend+"");

        // 6. iterate the result set and print the values

        while (rs.next()) {
            myFriendID=rs.getInt("idu");
        }
        System.out.println(myFriendID);
        PreparedStatement pSt = conn.prepareStatement("INSERT idowner, idfriends INTO relations VALUES (?,?)");
        pSt.setInt(1, myid);
        pSt.setInt(2, myFriendID);
        okk = true;

        // 5. execute a prepared statement
        int rowsInserted = pSt.executeUpdate();

        // 6. close the objects
        pSt.close();
        conn.close();
        return okk;
    }
}
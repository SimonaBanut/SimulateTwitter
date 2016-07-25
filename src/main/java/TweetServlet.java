import javax.json.Json;
import javax.json.JsonArrayBuilder;
import javax.json.JsonObject;
import javax.json.JsonObjectBuilder;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;
import java.util.ListIterator;

/**
 * Created by icondor on 16/07/16.
 */

@WebServlet("/myfeed")
public class TweetServlet extends HttpServlet {

    @Override
    protected void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

            HttpSession session = req.getSession(true);

            Integer iduser = (Integer) session.getAttribute("iduser");
            if (iduser == null) {
                System.out.println("nu esti logat nene");
                //notLoginAction(request, response, true);
            } else {
                System.out.println("bravo, esti un user deja logat");

                try {
                // List l = AccessDB.readTweets();
                List l = AccessDB.readMyFriends(iduser);


//            l.forEach((temp) -> {
//                TweetBean tb = (TweetBean) temp;
//                System.out.println(tb.getContent());
//
//            });


                // put the list in a json
                JsonObjectBuilder jObjBuilder = Json.createObjectBuilder();
                JsonArrayBuilder jArrayBuilder = Json.createArrayBuilder();
                for (ListIterator<TweetBean> iterator = l.listIterator(); iterator.hasNext(); ) {
                    TweetBean element = iterator.next();
                    jArrayBuilder.add(Json.createObjectBuilder()
                            .add("content", element.getContent())
                            .add("insertDate", element.getInsertDate().toString())
                            .add("id", element.getId())
                    );

                }

                jObjBuilder.add("myFeed", jArrayBuilder);
                JsonObject jSonFinal = jObjBuilder.build();

                System.out.println("json pe list: " + jSonFinal.toString());

                returnJsonResponse(resp, jSonFinal.toString());


            }catch(Exception e){
                e.printStackTrace();
            }

        }

    }

    /**/
    private void returnJsonResponse(HttpServletResponse response, String jsonResponse) {
        response.setContentType("application/json");
        PrintWriter pr = null;
        try {
            pr = response.getWriter();
        } catch (IOException e) {
            e.printStackTrace();
        }
        assert pr != null;
        pr.write(jsonResponse);
        pr.close();
    }
}

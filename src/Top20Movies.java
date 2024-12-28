import com.google.gson.JsonArray;
import jakarta.servlet.Servlet;
import jakarta.servlet.ServletConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.*;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

@WebServlet(name = "Top20Movies", urlPatterns = "/api/top20-movies")
public class Top20Movies extends HttpServlet{
    private static final long serialVersionUID = 1L;
    private DataSource dataSource;

    //init the data source
    public void init(ServletConfig config){
        try{
            dataSource = (DataSource) new InitialContext().lookup("java:comp/env/jdbc/moviedb");
        } catch (NamingException e){
            e.printStackTrace();
        }
    }
    //create doget method
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType("application/json"); // Response mime type
        //output stream to STDOUT
        PrintWriter out = response.getWriter();
        String id = request.getParameter("id");

        //make database connection
        try (Connection conn = dataSource.getConnection()) {
            //construct query to get top 20 movies
            String query1 = "{CALL GetTop20Movies()}";
            //declare our statement
            CallableStatement stmt1 = conn.prepareCall(query1);
            //perform the query
            ResultSet rs1 = stmt1.executeQuery();
            //create array to store top 20 movies result
            JsonArray jsonArray = new JsonArray();
            //iterate through each row of rs1
            while(rs1.next()){
                String movieId = rs1.getString("id");
                String movieTitle = rs1.getString("title");
                String movieYear = rs1.getString("year");
                String movieDirector = rs1.getString("director");
                String movieRating = rs1.getString("rating");

                //Create a JsonObject based on the data we retrieve from rs1
                JsonObject jsonObject = new JsonObject();
                jsonObject.addProperty("movied_id", movieId);
                jsonObject.addProperty("title", movieTitle);
                jsonObject.addProperty("year", movieYear);
                jsonObject.addProperty("director", movieDirector);
                jsonObject.addProperty("rating", movieRating);

                //use the movieId to the genres from movie
                String query2 = "{CALL GetMovieGenres(?)}";
                CallableStatement stmt2 = conn.prepareCall(query2);
                stmt2.setString(1, movieId);
                ResultSet rs2 = stmt2.executeQuery();
                StringBuilder tmp = new StringBuilder();
                while(rs2.next()){
                    tmp.append(rs2.getString("genre")).append(", ");
                }
                jsonObject.addProperty("genres", tmp.toString().trim().substring(0, tmp.length()-2));
                //use the movieId to generate the stars
                String query3 = "{CALL GetStarsFromMovie(?)}";
                CallableStatement stmt3 = conn.prepareCall(query3);
                stmt3.setString(1, movieId);
                ResultSet rs3 = stmt3.executeQuery();
                tmp = new StringBuilder();
                while(rs3.next()){
                    tmp.append(rs3.getString("name")).append(", ");
                }
                jsonObject.addProperty("stars", tmp.toString().trim().substring(0, tmp.length()-2));
                jsonArray.add(jsonObject);
                rs3.close();
                stmt3.close();
                rs2.close();
                stmt2.close();
            }
            rs1.close();
            stmt1.close();
            //write JSON string to output
            out.write(jsonArray.toString());
            //set response status to 200 (OK)
            response.setStatus(200);
        }catch (Exception e){
            JsonObject jsonObject = new JsonObject();
            jsonObject.addProperty("errorMessage", e.getMessage());
            out.write(jsonObject.toString());
            //set response status to 500 (Internal Server Error)
            response.setStatus(500);
        }finally{
            out.close();
        }
    }
}

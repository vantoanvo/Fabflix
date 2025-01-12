import com.google.gson.JsonArray;
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

import com.google.gson.JsonObject;

@WebServlet(name = "SingleStar", urlPatterns = "/api/single-star")
public class SingleStar extends HttpServlet{
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
        response.setContentType("application/json"); // Response mime typ
        String id = request.getParameter("id");

        // log message
        request.getServletContext().log("getting id:" + id);
        //output stream to STDOUT
        PrintWriter out = response.getWriter();
        //make database connection
        try (Connection conn = dataSource.getConnection()) {
            //construct query
            String query = "{call SingleStar(?)}";
            //declare our statement
            CallableStatement stmt = conn.prepareCall(query);
            //set the parameter
            stmt.setString(1, id);
            //perform the query
            ResultSet rs = stmt.executeQuery();
            JsonObject jsonObject = new JsonObject();
            //create array to store top 20 movies result
            JsonArray jsonArray = new JsonArray();
            //iterate through each row of rs
            while(rs.next()){
                String starName = rs.getString("name");
                String birthYear = rs.getString("birthYear");
                if(birthYear == null) {
                    birthYear = "N/A";
                }

                jsonObject.addProperty("star_name", starName);
                jsonObject.addProperty("birth_year", birthYear);
                //create star array
                JsonArray moviesArray = new JsonArray();
                do {
                    String movieId = rs.getString("id");
                    String movieTitle = rs.getString("title");
                    JsonObject starObj = new JsonObject();
                    starObj.addProperty("movie_id", movieId);
                    starObj.addProperty("movie_title", movieTitle);
                    moviesArray.add(starObj);
                }while(rs.next() && rs.getString("name").equals(starName));
                jsonObject.add("movies", moviesArray);
                jsonArray.add(jsonObject);
            }
            rs.close();
            stmt.close();
            out.write(jsonArray.toString());
            response.setStatus(200);
        } catch (Exception e) {
            JsonObject jsonObject = new JsonObject();
            jsonObject.addProperty("errorMessage", e.getMessage());
            out.write(jsonObject.toString());
            response.setStatus(500);
        } finally{
            out.close();
        }
    }
}
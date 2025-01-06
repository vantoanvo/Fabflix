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

@WebServlet(name = "SingleMovie", urlPatterns = "/api/single-movie")
public class SingleMovie extends HttpServlet{
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
            String query = "{call SingleMovie(?)}";
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
                JsonObject starObj = new JsonObject();

                String movieTitle = rs.getString("title");
                String movieYear = rs.getString("year");
                String movieDirector = rs.getString("director");
                String movieGenres = rs.getString("all_genres");
                String movieRating = rs.getString("rating");
                String starId = rs.getString("id");
                String starName = rs.getString("name");

                jsonObject.addProperty("movie_title", movieTitle);
                jsonObject.addProperty("movie_year", movieYear);
                jsonObject.addProperty("movie_director", movieDirector);
                jsonObject.addProperty("movie_genres", movieGenres);
                jsonObject.addProperty("movie_rating", movieRating);
                jsonArray.add(jsonObject); //add the row t

                starObj.addProperty("star_id", starId);
                starObj.addProperty("star_name", starName);
                jsonArray.add(starObj);
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
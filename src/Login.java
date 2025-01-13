import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import jakarta.servlet.ServletConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;

@WebServlet(name = "Login", urlPatterns = "/api/login")
public class Login extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private DataSource dataSource;

    // Initialize the data source
    public void init(ServletConfig config) {
        try {
            dataSource = (DataSource) new InitialContext().lookup("java:comp/env/jdbc/moviedb");
        } catch (NamingException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType("application/json");
        PrintWriter out = response.getWriter();
        JsonObject jsonResponse = new JsonObject();

        try {
            // Get the email and password from the request
            String email = request.getParameter("email");
            String password = request.getParameter("password");

            System.out.println("Received email: " + email + ", password: " + password);

            try (Connection conn = dataSource.getConnection()) {
                String query = "{call CheckLogin(?,?)}";
                CallableStatement stmt = conn.prepareCall(query);
                stmt.setString(1, email);
                stmt.setString(2, password);

                ResultSet rs = stmt.executeQuery();
                if (rs.next()) {
                    int loginCount = rs.getInt("count");
                    System.out.println("Login count: " + loginCount);

                    if (loginCount > 0) {
                        HttpSession session = request.getSession(true);
                        session.setAttribute("email", email);

                        jsonResponse.addProperty("status", "success");
                        jsonResponse.addProperty("message", "Login successful.");
                    } else {
                        jsonResponse.addProperty("status", "failure");
                        jsonResponse.addProperty("message", "Invalid username or password. Try again!");
                    }
                } else {
                    jsonResponse.addProperty("status", "failure");
                    jsonResponse.addProperty("message", "No matching records found.");
                }

                rs.close();
                stmt.close();
            }
        } catch (Exception e) {
            jsonResponse.addProperty("status", "exception error");
            jsonResponse.addProperty("message", "Server error: " + e.getMessage());
            response.setStatus(500);
        } finally {
            out.write(jsonResponse.toString());
            out.close();
        }
    }
}

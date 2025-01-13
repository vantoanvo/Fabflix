import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.io.PrintWriter;

@WebServlet(name = "Logout", urlPatterns = "/api/logout")
public class Logout extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession(false);  // Get the current session
        if (session != null) {
            session.invalidate();  // Invalidate the session to log the user out
        }

        // Send a success response to the client
        response.setStatus(200);  // You can also send a response body if needed, e.g. response.getWriter().write("Logged out successfully");
    }
}


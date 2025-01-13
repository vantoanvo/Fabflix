import jakarta.servlet.*;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;

@WebFilter(urlPatterns = {"/api/*", "/top20movies.html", "/single-movie.html", "/single-star.html"}) // Apply the filter to all APIs under /api/*
public class LoginFilter implements Filter {

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        // Initialization logic if needed
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpServletResponse httpResponse = (HttpServletResponse) response;

        HttpSession session = httpRequest.getSession(false); // Get the current session, don't create a new one

        // Allow login and public endpoints to bypass authentication
        String requestURI = httpRequest.getRequestURI();
        System.out.println("Request URI: " + requestURI);
        if (requestURI.endsWith("login.html") || requestURI.endsWith("/api/login")) {
            chain.doFilter(request, response); // Proceed without checking the session
            return;
        }

        // Check if the user is logged in
        if (session == null || session.getAttribute("email") == null) {
            // User is not logged in, redirect to login page
            httpResponse.sendRedirect(httpRequest.getContextPath() + "/login.html");
        } else {
            // User is logged in, proceed with the request
            chain.doFilter(request, response);
        }
    }

    @Override
    public void destroy() {
        // Cleanup logic if needed
    }
}

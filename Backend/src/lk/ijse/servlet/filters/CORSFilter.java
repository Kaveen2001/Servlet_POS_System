package lk.ijse.servlet.filters;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebFilter(urlPatterns = "/*") // Filter all requests
public class CORSFilter implements Filter {

    public CORSFilter() {
        System.out.println("Object Created from CORS Filter..!");
    }

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        System.out.println("CORS Filter Initialized..!");
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        // Before the request send
        System.out.println("Do Filter Method Called..!");

        // Super Reference cast to Sub Type (Cast ServletRequest to HttpRequest to access addHeader method)
        HttpServletRequest req = (HttpServletRequest) servletRequest;
        HttpServletResponse resp = (HttpServletResponse) servletResponse;

        // Proceed request to the servlet
        filterChain.doFilter(servletRequest,servletResponse);

        // CORS Error Handling (Use Filters)
        resp.addHeader("Access-Control-Allow-Origin","*");
        resp.addHeader("Access-Control-Allow-Methods","DELETE, PUT");
        resp.addHeader("Access-Control-Allow-Headers","Content-Type");
    }

    @Override
    public void destroy() {
        System.out.println("Destroy method invoked..!");
    }
}

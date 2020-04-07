package pl.edu.agh.airsystem.filters;

import org.springframework.stereotype.Component;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

@Component
public class FrontendFilter implements Filter {

    @Override
    public void doFilter(ServletRequest request,
                         ServletResponse response,
                         FilterChain chain) throws IOException, ServletException {

        HttpServletRequest req = (HttpServletRequest) request;
        String requestURI = req.getRequestURI();

        System.out.println(requestURI);

        if (requestURI.startsWith("/api") || requestURI.startsWith("/static")) {
            chain.doFilter(request, response);
            return;
        }


        // all requests not api or static will be forwarded to index page.
        request.getRequestDispatcher("/").forward(request, response);
    }

}
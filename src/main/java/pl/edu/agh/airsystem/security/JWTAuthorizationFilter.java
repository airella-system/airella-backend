package pl.edu.agh.airsystem.security;

import lombok.AllArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import pl.edu.agh.airsystem.service.CustomUserDetailsService;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
@AllArgsConstructor
public class JWTAuthorizationFilter extends OncePerRequestFilter {

    private final JWTTokenUtil jwtTokenUtil;
    private final CustomUserDetailsService customUserDetailsService;

    @Override
    protected void doFilterInternal(HttpServletRequest req,
                                    HttpServletResponse res,
                                    FilterChain chain) throws IOException, ServletException {
        try {
            UsernamePasswordAuthenticationToken authentication = getAuthentication(req);
            SecurityContextHolder.getContext().setAuthentication(authentication);
        } catch (WrongTokenException | NoTokenException ignored) {

        }

        chain.doFilter(req, res);
    }

    private UsernamePasswordAuthenticationToken getAuthentication(HttpServletRequest request) {
        final String requestTokenHeader = request.getHeader("Authorization");
        if (requestTokenHeader != null && requestTokenHeader.startsWith("Bearer ")) {
            try {
                String token = requestTokenHeader.substring(7);
                String username = jwtTokenUtil.getUsernameFromToken(token);
                UserDetails userDetails = customUserDetailsService.loadUserByUsername(username);

                return new UsernamePasswordAuthenticationToken(
                        userDetails, null, userDetails.getAuthorities());
            } catch (Exception e) {
                throw new WrongTokenException("Authentication failed");
            }
        } else {
            throw new NoTokenException("Authentication failed");
        }
    }
}
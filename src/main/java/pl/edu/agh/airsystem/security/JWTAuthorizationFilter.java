package pl.edu.agh.airsystem.security;

import lombok.AllArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import pl.edu.agh.airsystem.model.Client;
import pl.edu.agh.airsystem.model.Role;
import pl.edu.agh.airsystem.repository.ClientRepository;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Collection;

@Component
@AllArgsConstructor
public class JWTAuthorizationFilter extends OncePerRequestFilter {

    private final JWTTokenUtil jwtTokenUtil;
    private final ClientRepository clientRepository;

    @Override
    protected void doFilterInternal(HttpServletRequest req,
                                    HttpServletResponse res,
                                    FilterChain chain) throws IOException, ServletException {
        try {
            Client client = getAuthentication(req);
            SecurityContextHolder.getContext().setAuthentication(
                    new UsernamePasswordAuthenticationToken(client,
                            null, getGrantedAuthorities(client)));
        } catch (WrongTokenException | NoTokenException ignored) {

        }

        chain.doFilter(req, res);
    }

    private Collection<GrantedAuthority> getGrantedAuthorities(Client client) {
        String[] roles = client.getRoles().stream()
                .map(Role::toString)
                .toArray(String[]::new);
        return AuthorityUtils.createAuthorityList(roles);
    }

    private Client getAuthentication(HttpServletRequest request) {
        final String requestTokenHeader = request.getHeader("Authorization");
        if (requestTokenHeader != null && requestTokenHeader.startsWith("Bearer ")) {
            try {
                String token = requestTokenHeader.substring(7);
                long id = jwtTokenUtil.getClientIdFromToken(token);

                return clientRepository.findById(id)
                        .orElseThrow(() -> new IllegalArgumentException("Unknown id"));
            } catch (Exception e) {
                throw new WrongTokenException("Authentication failed");
            }
        } else {
            throw new NoTokenException("Authentication failed");
        }
    }
}
package fhcampuswien.zeiterfassungssystem.security;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import fhcampuswien.zeiterfassungssystem.Enum.Role;
import fhcampuswien.zeiterfassungssystem.entity.AuftraggeberFirma;
import fhcampuswien.zeiterfassungssystem.entity.Mitarbeiter;
import fhcampuswien.zeiterfassungssystem.service.AuftraggeberFirmaService;
import fhcampuswien.zeiterfassungssystem.service.MitarbeiterService;
import fhcampuswien.zeiterfassungssystem.service.jwt.JwtUserDetailsService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Date;

@Component
@Slf4j
public class AuthSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {
    private final int expTime;
    private final String secret;
    private final JwtUserDetailsService userDetailsService;
    private MitarbeiterService mitarbeiterService;
    private AuftraggeberFirmaService firmaService;

    @Autowired
    public AuthSuccessHandler(@Value("${jwt.expiration}") int expTime,
                              @Value("${jwt.secret}") String secret,
                              JwtUserDetailsService userDetailsService,
                              MitarbeiterService mitarbeiterService,
                              AuftraggeberFirmaService firmaService) {
        this.expTime = expTime;
        this.secret = secret;
        this.userDetailsService = userDetailsService;
        this.mitarbeiterService = mitarbeiterService;
        this.firmaService = firmaService;
    }

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
                                        Authentication authentication) throws IOException {
        UserDetails principal = (UserDetails) authentication.getPrincipal();
        String grantedAuthority = principal.getAuthorities().stream().findFirst().get().toString();
        Role role = Role.valueOf(grantedAuthority);
        String username = principal.getUsername();

        Long id = getUserIdByRole(username, role);

        String token = JWT.create()
                .withSubject(userDetailsService.loadUserByUsername(principal.getUsername()).getUsername())
                .withExpiresAt(Date.from(Instant.ofEpochMilli(ZonedDateTime.now(ZoneId.systemDefault()).toInstant().toEpochMilli() + expTime)))
                .sign(Algorithm.HMAC256(secret));
        response.addHeader("Authorization", "Bearer " + token);
        response.addHeader("Content-Type", "application/json");
        response.getWriter().print("{\"token\": \""+token+"\"," +
                "\t\"username\": \""+principal.getUsername()+"\"\n," +
                "\t\"id\": \""+id+"\"\n," +
                "\t\"role\": \""+principal.getAuthorities().stream().findFirst().get()+"\"\n" +
                "}");
    }

    public Long getUserIdByRole(String username, Role role) {
        Long id;
        switch (role) {
            case ROLE_ADMIN:
            case ROLE_MITARBEITER:
            case ROLE_SCHICHTLEITER:
                id = mitarbeiterService.getMitarbeiterByUsername(username).getId();
                break;
            case ROLE_FIRMA:
                id = firmaService.getFirmaByUsername(username).getId();
                break;
            default:
                throw new IllegalStateException("Unexpected value: " + role);
        }
        return id;
    }

}
package JustWork.server.config;

import JustWork.server.models.Token;
import JustWork.server.service.TokenService;
import io.jsonwebtoken.ExpiredJwtException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.token.Sha512DigestUtils;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;

@Slf4j
@Component
public class JwtRequestFilter extends OncePerRequestFilter {

    public static final String X_TOKEN = "Authorization";

    @Autowired
    private TokenService tokenService;

    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
            throws ServletException, IOException {
        final String requestTokenHeader = request.getHeader(X_TOKEN);
        final String cabeceraLog="[doFilterInternal] ";

        String uuidUsuario = null;
        String jwtToken = null;
// JWT Token is in the form "Bearer token". Remove Bearer word and get
// only the Token
        if (requestTokenHeader != null && requestTokenHeader.startsWith("Bearer ")) {
            jwtToken = requestTokenHeader.substring(7);
            try {
                uuidUsuario = jwtTokenUtil.getUserUUIDFromToken(jwtToken);
                log.debug(cabeceraLog+"Tenemos uuidUsuario:"+uuidUsuario);
            } catch (IllegalArgumentException e) {
                log.error(cabeceraLog+"Unable to get JWT Token");
            } catch (ExpiredJwtException e) {
                log.error(cabeceraLog+"JWT Token has expired");
            }
        } else {
            log.warn(cabeceraLog+"JWT Token does not begin with Bearer String");
        }

        if (uuidUsuario != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            log.debug(cabeceraLog+"tenemos JWT valido y autenticado para uuidUsuario:"+uuidUsuario);
            Token token = tokenService.getFromShaToken(Sha512DigestUtils.shaHex(jwtToken));
            log.debug(cabeceraLog+"tenemos JWT valido token:"+token);
            if (token!=null && token.isVigente() && !token.getUsuario().isEliminado()
            ) {
                UserDetails userDetails = new User(token.getUsuario().getUuid().toString()
                        , "$2a$10$slYQmyNdGzTn7ZLBXBChFOC9f6kFjAqPhccnP6DxlWXx2lPk1C3G6",
                        new ArrayList<>());

                    UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(
                            userDetails, null, userDetails.getAuthorities());
                    usernamePasswordAuthenticationToken
                            .setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
// After setting the Authentication in the context, we specify
// that the current user is authenticated. So it passes the
// Spring Security Configurations successfully.
                    SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
            }
        }
        log.info(cabeceraLog+"No hay sesion valida requestTokenHeader:"+requestTokenHeader);
        chain.doFilter(request, response);
    }
}
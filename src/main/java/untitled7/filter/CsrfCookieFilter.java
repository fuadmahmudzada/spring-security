package untitled7.filter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.web.csrf.CsrfToken;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;

public class CsrfCookieFilter extends OncePerRequestFilter {

    private final List<String> securedEndpoints = List.of("/myAccount", "/myBalance", "/myLoans", "/myCards", "/user");

    private final AntPathMatcher antPathMatcher = new AntPathMatcher();
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        boolean isSecuredApi  = securedEndpoints.stream().anyMatch(api -> antPathMatcher.match(api, request.getRequestURI()));

        if(isSecuredApi){
            CsrfToken csrfToken = (CsrfToken) request.getAttribute(CsrfToken.class.getName());
            //Render the token value to a cookie by causing the deferred token to be loaded
            //Texire salinan token-i yukleyib token-i cookie formasina getirir
            if(csrfToken!=null){
                csrfToken.getToken();
            }

        }
        filterChain.doFilter(request, response);
        //generated csrf token i client e response headerin icerisinde geri gondermek ucun istifade olunur


    }
}

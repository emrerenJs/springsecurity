package com.emrerenjs.springsecurity.Filters;

import com.emrerenjs.springsecurity.Business.JwtUtilService;
import com.emrerenjs.springsecurity.Business.UserDetailsManager;
import io.jsonwebtoken.security.SignatureException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.util.WebUtils;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URLDecoder;

//Bu sınıfı gelen istekleri filtrelemek ve Token sorgulaması yapmak için kullanıyoruz.
@Component
public class JwtFilter extends OncePerRequestFilter {

    @Autowired
    private UserDetailsManager userDetailsService;

    //Token üzerinde okuma yapmak için aşağıdaki iş sınıfını enjekte ediyoruz.
    @Autowired
    private JwtUtilService jwtUtil;


    @Override
    protected void doFilterInternal(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, FilterChain filterChain) throws ServletException, IOException {
        String authHeader = null;
        Cookie cookie = null;
        //Kullanıcının token bilgisini HttpOnly cookie'den alıyoruz.
        try{
            cookie = WebUtils.getCookie(httpServletRequest,"jwt");
            authHeader = cookie.getValue();
        }catch(NullPointerException ignored){}
        String username = null;
        String jwt = null;
        //Token gerçekten var mı?
        if (authHeader != null) {
            //varsa token'ı decode et(URL formatında encodelandı)
            authHeader = URLDecoder.decode(authHeader,"UTF-8");
            if(authHeader.startsWith("Bearer ")){
                //Bearer olduğu için Stringden 'Bearer ' kısmını çıkarıyoruz.
                jwt = authHeader.substring(7);
                try{
                    //Kullanıcı username bilgisini al
                    username = jwtUtil.extractUsername(jwt);
                }catch(SignatureException ex){
                    //İmza değiştiyse
                    cookie.setMaxAge(0);
                    httpServletResponse.addCookie(cookie);
                }
            }
        }
        //kullanıcı adı bulunduysa ve giriş yapmadıysa
        if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            UserDetails userDetails = null;
            try{
                //Kullanıcı bilgilerini al.
                userDetails = userDetailsService.loadUserByUsername(username);
                //Token onaylamasını kullanıcı bilgileri ile yap.
                if (jwtUtil.validateToken(jwt, userDetails)) {
                    //token onaylandıysa girişi sağla.
                    UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken =
                            new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
                    usernamePasswordAuthenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(httpServletRequest));
                    SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
                }
            }catch(UsernameNotFoundException ex){
                cookie.setMaxAge(0);
                httpServletResponse.addCookie(cookie);
            }
        }
        //isteği filtrele.
        filterChain.doFilter(httpServletRequest, httpServletResponse);
    }
}

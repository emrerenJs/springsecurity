package com.emrerenjs.springsecurity.Business;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Service
public class JwtUtilService {

    @Value("${spring.application.tokenExpireTimeSeconds}")
    private int tokenExpireTimeSeconds;

    //Token şifresini çözmek ve şifrelemek için gizli bir key
    Key key = Keys.secretKeyFor(SignatureAlgorithm.HS256);

    /*
     * En önemli method generateTokendır. Kullanıcı bilgilerini UserDetails üzerinden alır.
     * UserDetails burada bir interfacedir ancak SecurityConfigurer içerisinde ki configure methodunda biz
     * kendi UserDetailsManager'ımızı verdik. O kısımda loadByUsername methodu içine yazdığımız kodlar ile aldığımız kullanıcı bilgileri
     * bu kısımda işimize yarıyacak. Bu bilgiler ile token'ımızı yaratacağız.
     */
    public String generateToken(UserDetails userDetails) {
        //Token bilgilerini içermesi için bir claim yaratıyoruz.
        Map<String, Object> claims = new HashMap<>();
        return createToken(claims,userDetails.getUsername());
    }
    /*
     * Token bilgileri alındıktan sonra createToken methodu devreye girer.
     * Burada Builder pattern'i ile istediğimiz şekilde token'ı oluşturup geriye döneriz.
     */
    private String createToken(Map<String, Object> claims, String subject) {
        return Jwts.builder().setClaims(claims)
                .setSubject(subject) // ilgili kullanıcı
                .setIssuedAt(new Date(System.currentTimeMillis())) // başlangıç
                .setExpiration(new Date(System.currentTimeMillis() + tokenExpireTimeSeconds * 1000)) // bitiş
                .signWith(key) // kullanılan algoritma ve bu algoritma çalışırken kullanılacak key değeri
                .compact();
    }

    // verilen token a ait kullanıcı adını döndürür.
    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    // verilen token a ait token bitiş süresini verir.
    public Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    // verilen token a ait claims bilgisini alır.
    private Claims extractAllClaims(String token) {
        return Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token).getBody();
    }

    // token ın geçerlilik süre doldu mu?
    private Boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    // token hala geçerli mi? kullanıcı adı doğru mu ise ve token ın geçerlilik süresi devam ediyorsa true döner.
    public Boolean validateToken(String token, UserDetails userDetails) {
        final String username = extractUsername(token);
        return (username.equals(userDetails.getUsername()) && !isTokenExpired(token));
    }
}

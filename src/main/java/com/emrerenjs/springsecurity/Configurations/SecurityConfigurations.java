package com.emrerenjs.springsecurity.Configurations;

import com.emrerenjs.springsecurity.Business.UserDetailsManager;
import com.emrerenjs.springsecurity.Filters.JwtFilter;
import com.emrerenjs.springsecurity.Handlers.CustomAccessDeniedHandler;
import com.emrerenjs.springsecurity.Handlers.CustomAuthenticationEntryPoint;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/*TR*/
//Bu sınıf'ı Spring Security'i konfigüre etmek için kullanıyoruz. Kullanıcının nerelere erişip erişemeyeceği,
//token filtresinin yazıldığı, Session yönetimi, kullanıcı isteklerinin(Güvenlik için) yönlendirilmesi gibi bir çok
//işi burada yaparız.


@EnableWebSecurity
public class SecurityConfigurations extends WebSecurityConfigurerAdapter {
    //Gelen isteklere JWT filtresini uyguladığımız sınıfı enjekte ediyoruz.
    @Autowired
    private JwtFilter jwtFilter;

    //Kendi opsiyonel Kullanıcı bilgilerini aldığmız sınıfı enjekte ediyoruz.
    @Autowired
    private UserDetailsManager userDetailsManager;

    //Kendi UserDetailsService sınıfımızı Spring Security'e tanıtıyoruz.
    @Override
    protected void configure(AuthenticationManagerBuilder authenticationManagerBuilder) throws Exception {

        authenticationManagerBuilder.userDetailsService(userDetailsManager).passwordEncoder(passwordEncoder());
    }

    //Güvenlik konfigürasyonlarını yaptığımız ana method. İstekleri ve rolleri filtrelemek,
    //Session bilgilerini düzenlemek ve filtre ekleme yaptığımız kısımdır.
    @Override
    protected void configure(HttpSecurity httpSecurity) throws Exception{
        httpSecurity.csrf().disable()//Csrf disable ediyoruz. Çünkü csrf için ayrı bir token yazılması gerekiyor. Csrf bir saldırı türüdür.
                .authorizeRequests()//Gelen isteklere izin veriyoruz.
                    .antMatchers("/","/auth","/auth/**").permitAll()
                    .antMatchers("/user").hasRole("USER")
                    .and()
                .exceptionHandling()//Gelen isteklerin erişim hatalarını yönetiyoruz.
                    .authenticationEntryPoint(authenticationEntryPoint())
                    .accessDeniedHandler(accessDeniedHandler())
                    .and()
                .sessionManagement()
                    .sessionCreationPolicy(SessionCreationPolicy.STATELESS);
        httpSecurity.addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);//İsteği JWT ile filtreliyoruz.
    }

    //Veri tabanında ki parolaların hangi formatta şifrelendiğini burada belirtiriz.
    //Bu sayede kullanıcı giriş yaptığında onunda parolası şifrelenir ve karşılaşma gerçekleştirilir.
    //Şu anda hiç bir şifreleme kullanmadığımızı söylüyoruz. (Kesinlikle olması gereken yöntem şifreleme kullanmaktır)
    @Bean
    public PasswordEncoder passwordEncoder(){
        return NoOpPasswordEncoder.getInstance();
    }

    //Kullanıcın giriş yapması durumunu Spring Security'nin kontrol edebilmesi için kalıtıldığımız üst sınıfın fasülyesini dönüyoruz.
    @Override
    @Bean
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    //Eğer kullanıcı giriş yapmadan bir yere erişmeye çalışırsa olacak işlemleri yazdığımız sınıf.
    @Bean
    public AuthenticationEntryPoint authenticationEntryPoint(){
        return new CustomAuthenticationEntryPoint();
    }

    //Eğer kullanıcı giriş yaptıysa ancak erişime yetkisi yoksa olacak işlemleri yazdığımız sınıf.
    @Bean
    public AccessDeniedHandler accessDeniedHandler(){
        return new CustomAccessDeniedHandler();
    }

}

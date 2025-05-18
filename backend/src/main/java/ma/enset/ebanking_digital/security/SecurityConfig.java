package ma.enset.ebanking_digital.security;

import com.nimbusds.jose.jwk.source.ImmutableJWKSet;
import com.nimbusds.jose.jwk.source.ImmutableSecret;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.oauth2.server.resource.OAuth2ResourceServerConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.jose.jws.MacAlgorithm;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.security.oauth2.jwt.NimbusJwtEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import javax.crypto.spec.SecretKeySpec;
import java.util.List;

@Configuration
@EnableWebSecurity
// pour protéger les entpoint !!
@EnableMethodSecurity(prePostEnabled = true)
public class SecurityConfig {

    // pour injecter le clé privé qui se trouve dans le fichier application.properties au variable secretKey
    @Value("${jwt.secret}")
    private String secretKey;

    // *******************************************************************************************

    @Bean
    public InMemoryUserDetailsManager inMemoryUserDetailsManager() {
        PasswordEncoder passwordEncoder = passwordEncoder();
        return new InMemoryUserDetailsManager(
                User.withUsername("user1").password(passwordEncoder.encode("12345")).authorities("USER").build(),
                User.withUsername("admin").password(passwordEncoder.encode("12345")).authorities("USER", "ADMIN").build()
        );
    }

    // *******************************************************************************************

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    // *******************************************************************************************

    // pour protégé l'app, il faut créer un "securityFilterChain"
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http
                // c-à-d je j'aire pas la session coté serveur, je veux utilisé une authentification "STATELESS", on utilisant un token JWT
                .sessionManagement(sm -> sm.sessionCreationPolicy(SessionCreationPolicy.STATELESS))

                // quand on utilise une authentification STATELESS, il faut désactiver la protection csrf
                .csrf(csrf -> csrf.disable())
                .cors(Customizer.withDefaults())
                .authorizeHttpRequests(ar->ar.requestMatchers("/auth/login/**").permitAll())

                // pour protéger
                .authorizeHttpRequests(ar -> ar.anyRequest().authenticated())

                //.httpBasic(Customizer.withDefaults())

                .oauth2ResourceServer(oa -> oa.jwt(Customizer.withDefaults()))
                .build();
    }

    // *******************************************************************************************
    // Cette méthode configure un encodeur JWT en utilisant une clé secrète.
    // L'encodeur est utilisé pour signer et générer des tokens JWT.

    @Bean
    JwtEncoder jwtEncoder() {
        //String secretKey = "9faa372517ac1d389758d3750fc07acf00f542277f26fec1ce4593e93f64e338";
        return new NimbusJwtEncoder(new ImmutableSecret<>(secretKey.getBytes()));
    }

    // *******************************************************************************************

    // Cette méthode configure un décodeur JWT en utilisant une clé secrète.
    // Le décodeur est utilisé pour vérifier et décoder les tokens JWT reçus.
    // La méthode utilise l'algorithme HS512 pour la signature.
    @Bean
    JwtDecoder jwtDecoder() {
        //String secretKey = "9faa372517ac1d389758d3750fc07acf00f542277f26fec1ce4593e93f64e338";
        SecretKeySpec secretKeySpec = new SecretKeySpec(secretKey.getBytes(), "RSA");
        return NimbusJwtDecoder.withSecretKey(secretKeySpec).macAlgorithm(MacAlgorithm.HS512).build();
    }

    // *******************************************************************************************

    /*
    - Cette méthode configure un AuthenticationManager personnalisé.
    - Elle utilise un DaoAuthenticationProvider pour gérer l'authentification des utilisateurs.
    - Le DaoAuthenticationProvider est configuré avec un PasswordEncoder pour vérifier les mots de passe
    - et un UserDetailsService pour charger les détails des utilisateurs.
     */
    @Bean
    public AuthenticationManager authenticationManager(UserDetailsService userDetailsService) {
        DaoAuthenticationProvider daoAuthenticationProvider = new DaoAuthenticationProvider();
        // pour comparer les mot de pass
        daoAuthenticationProvider.setPasswordEncoder(passwordEncoder());
        // load user by username
        daoAuthenticationProvider.setUserDetailsService(userDetailsService);

        return new ProviderManager(daoAuthenticationProvider);
    }

    @Bean
    CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration corsConfiguration = new CorsConfiguration();
        corsConfiguration.addAllowedOrigin("*");
        corsConfiguration.addAllowedHeader("*");
        corsConfiguration.addAllowedMethod("*");
        //corsConfiguration.setExposedHeaders(List.of("x-auth-token"));
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", corsConfiguration);
        return source;
    }
}

/*
@EnableWebSecurity : Elle active la sécurité web de Spring Security dans ton application.
Autrement dit, elle remplace la configuration de sécurité par défaut fournie par Spring Boot et te permet
de définir ta propre configuration personnalisée des règles de sécurité (comme les filtres, les CORS,
les endpoints publics/protégés...).

@EnableMethodSecurity(prePostEnabled = true) : Elle active la sécurité au niveau des méthodes grâce à des annotations comme :
- @PreAuthorize(...), - @PostAuthorize(...)
 */
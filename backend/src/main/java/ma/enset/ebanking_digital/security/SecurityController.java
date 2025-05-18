package ma.enset.ebanking_digital.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.jose.jws.MacAlgorithm;
import org.springframework.security.oauth2.jwt.*;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/auth")
public class SecurityController {

    // pour authentifier un utilisateur springSecurity utilise cette objet
    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private JwtEncoder jwtEncoder;

    @GetMapping("/profile")
    public Authentication authentication(Authentication authentication) {
        return authentication;
    }


    /*
    - Crée un endpoint permettant de recevoir une requête HTTP contenant un nom d'utilisateur et un mot de passe.
    - Le système authentifie l'utilisateur : s'il existe et que les identifiants sont valides,
    - un jeton (JWT) est généré avec les informations suivantes : date de génération, date d'expiration,
    - sujet (nom d'utilisateur) et rôles de l'utilisateur.
    - Le jeton est ensuite retourné dans une réponse JSON avec la clé "access-token".
     */
    @PostMapping("/login")
    public Map<String, String> login(String username, String password) {
        // Authentifie l'utilisateur avec le nom d'utilisateur et le mot de passe
        Authentication authenticate = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(username, password)
        );

        Instant instant = Instant.now(); // Récupère l'heure actuelle (pour l'horodatage du token)

        // Récupère les rôles de l'utilisateur et les convertit en une seule chaîne séparée par des espaces
        String scope = authenticate.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.joining(" "));

        // Crée les claims du JWT avec la date d’émission, d’expiration, le sujet (utilisateur), et les rôles
        JwtClaimsSet jwtClaimsSet = JwtClaimsSet.builder()
                .issuedAt(instant)
                .expiresAt(instant.plus(10, ChronoUnit.MINUTES)) // Le token expire après 10 minutes
                .subject(username) // L'utilisateur authentifié
                .claim("scope", scope) // Ajoute les rôles dans les claims
                .build();

        // Prépare les paramètres pour encoder le JWT avec l’algorithme HS512
        JwtEncoderParameters jwtEncoderParameters = JwtEncoderParameters.from(
                JwsHeader.with(MacAlgorithm.HS512).build(),
                jwtClaimsSet
        );

        // Encode le JWT à l’aide du jwtEncoder
        String jwt = jwtEncoder.encode(jwtEncoderParameters).getTokenValue();

        // Retourne le token dans une map avec la clé "access-token"
        return Map.of("access-token", jwt);
    }

}
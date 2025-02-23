package morpion.configuration;

import java.util.Collection;

import org.springframework.security.authentication.ReactiveAuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.ReactiveUserDetailsService;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import morpion.service.JWTService;
import reactor.core.publisher.Mono;

@Component
public class AuthManager implements ReactiveAuthenticationManager {

    final JWTService jwtService;
    final ReactiveUserDetailsService users;

    public AuthManager(JWTService jwtService, ReactiveUserDetailsService users) {
        this.jwtService = jwtService;
        this.users = users;
    }

    @Override
    public Mono<Authentication> authenticate(Authentication authentication) {
        return Mono.justOrEmpty(
                authentication)
                .cast(BearerToken.class)
                .flatMap(auth -> {
                    String getUserName = jwtService.getUserName(auth.getCredentials());
                    Mono<UserDetails> foundUser = users.findByUsername(getUserName).defaultIfEmpty(new UserDetails() {

                        @Override
                        public Collection<? extends GrantedAuthority> getAuthorities() {
                            // TODO Auto-generated method stub
                            throw new UnsupportedOperationException("Unimplemented method 'getAuthorities'");
                        }

                        @Override
                        public String getPassword() {
                            // TODO Auto-generated method stub
                            throw new UnsupportedOperationException("Unimplemented method 'getPassword'");
                        }

                        @Override
                        public String getUsername() {
                            // TODO Auto-generated method stub
                            throw new UnsupportedOperationException("Unimplemented method 'getUsername'");
                        }

                        @Override
                        public boolean isAccountNonExpired() {
                            // TODO Auto-generated method stub
                            throw new UnsupportedOperationException("Unimplemented method 'isAccountNonExpired'");
                        }

                        @Override
                        public boolean isAccountNonLocked() {
                            // TODO Auto-generated method stub
                            throw new UnsupportedOperationException("Unimplemented method 'isAccountNonLocked'");
                        }

                        @Override
                        public boolean isCredentialsNonExpired() {
                            // TODO Auto-generated method stub
                            throw new UnsupportedOperationException("Unimplemented method 'isCredentialsNonExpired'");
                        }

                        @Override
                        public boolean isEnabled() {
                            // TODO Auto-generated method stub
                            throw new UnsupportedOperationException("Unimplemented method 'isEnabled'");
                        }

                    });

                    Mono<Authentication> authenticatedUser = foundUser.flatMap(u -> {
                        if (u.getUsername() == null) {
                            Mono.error(new IllegalArgumentException("User not found"));
                        }
                        if (jwtService.validate(u, auth.getCredentials())) {
                            return Mono.justOrEmpty(new UsernamePasswordAuthenticationToken(u.getUsername(),
                                    u.getPassword(), u.getAuthorities()));
                        }
                        return Mono.error(new IllegalArgumentException("Invalid/ Expired Token"));

                    });
                    return authenticatedUser;
                });
    }
}

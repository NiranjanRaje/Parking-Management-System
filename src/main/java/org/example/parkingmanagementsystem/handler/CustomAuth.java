package org.example.parkingmanagementsystem.handler;

import org.example.parkingmanagementsystem.model.User;
import org.example.parkingmanagementsystem.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;


@Component
public class CustomAuth implements AuthenticationProvider {

    @Autowired
    UserService userService;


    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        String email = authentication.getName();
        String password = authentication.getCredentials().toString();
        Optional<User> user=userService.userByEmail(email).filter(User::isApproved);
        if(user.isPresent() && password.equals(user.get().getPassword())){
            List<GrantedAuthority> roles = new ArrayList<>();
            roles.add(new SimpleGrantedAuthority("ROLE_"+user.get().getRole().getRole()));
            return new UsernamePasswordAuthenticationToken(email, password, roles);
        }
        throw new BadCredentialsException("Invalid email or password");
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return UsernamePasswordAuthenticationToken.class.isAssignableFrom(authentication);
    }
}

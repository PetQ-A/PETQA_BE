package com.petqa.security;

import com.petqa.domain.User;
import com.petqa.dto.auth.CustomUserDetails;
import com.petqa.repository.UserRepository;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Component;

@Component
public class CustomAuthenticationProvider implements AuthenticationProvider {

    private final UserRepository userRepository;

    public CustomAuthenticationProvider(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        String socialId = String.valueOf(authentication.getName());
        String username = (String) authentication.getCredentials();

        User user = userRepository.findUserBySocialIdAndUsername(socialId, username).orElse(null);

        if (user == null){
            user = User.builder()
                    .socialId(socialId)
                    .username(username)
                    .build();

            userRepository.save(user);
        }



        CustomUserDetails userDetails = new CustomUserDetails(user);
        return new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return UsernamePasswordAuthenticationToken.class.isAssignableFrom(authentication);
    }
}

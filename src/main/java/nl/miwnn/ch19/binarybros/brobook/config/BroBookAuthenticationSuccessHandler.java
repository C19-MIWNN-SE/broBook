
package nl.miwnn.ch19.binarybros.brobook.config;

/**
 * @author Paul Rademaker
 * Handles post-login action
 */

import nl.miwnn.ch19.binarybros.brobook.model.BroBookUser;
import nl.miwnn.ch19.binarybros.brobook.repository.BroBookUserRepository;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;

    @Component
    public class BroBookAuthenticationSuccessHandler implements AuthenticationSuccessHandler {

        private final BroBookUserRepository userRepository;

        public BroBookAuthenticationSuccessHandler(BroBookUserRepository userRepository) {
            this.userRepository = userRepository;
        }

        @Override
        public void onAuthenticationSuccess(HttpServletRequest request,
                                            HttpServletResponse response,
                                            Authentication authentication) throws IOException, ServletException {

            String username = authentication.getName();
            BroBookUser user = userRepository.findByUsername(username).orElse(null);

            if (user != null &&  user.getRole().equals("Student")) {
                response.sendRedirect("/info/detail/" + user.getId());
            }else if (user != null && user.getRole().equals("Teacher")){
                response.sendRedirect("/cohort/all");
            }else if (user != null && user.getRole().equals("ADMIN")){
                response.sendRedirect("/user/all");
            }
        }
    }


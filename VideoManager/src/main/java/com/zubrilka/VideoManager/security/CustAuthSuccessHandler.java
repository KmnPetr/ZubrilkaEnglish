package com.zubrilka.VideoManager.security;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.zubrilka.VideoManager.dto.PersonDto;
import com.zubrilka.VideoManager.services.PersonService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

/**
 * overrides the standard positive response from the entry point "/login"
 */
@Component
public class CustAuthSuccessHandler implements AuthenticationSuccessHandler {
    private final PersonService personService;
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Autowired
    public CustAuthSuccessHandler(PersonService personService) {
        this.personService = personService;
    }

    @Override
    public void onAuthenticationSuccess(
            HttpServletRequest request,
            HttpServletResponse response,
            Authentication authentication) throws IOException {

        String username = authentication.getName();

        PersonDto personDto = personService.getPersonByName(username);

        response.setStatus(HttpServletResponse.SC_OK);
        response.setContentType("application/json");
        response.getWriter().write(objectMapper.writeValueAsString(personDto));
        response.getWriter().flush();
    }
}

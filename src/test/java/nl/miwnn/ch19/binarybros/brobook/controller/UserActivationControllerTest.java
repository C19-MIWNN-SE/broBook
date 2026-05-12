package nl.miwnn.ch19.binarybros.brobook.controller;

import nl.miwnn.ch19.binarybros.brobook.service.UserActivationService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;


import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
/*
 * @author Mart Stukje
 * */

@WebMvcTest(UserActivationController.class)
class UserActivationControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private UserActivationService userActivationService;

    @Test
    @WithMockUser
    @DisplayName("showActivationPage should show step1 with DTO attribute in model")
    void showActivationPageShouldShowStep1WithDtoAttributeInModel() throws Exception {
        mockMvc.perform(get("/activate/user"))
                .andExpect(status().isOk())
                .andExpect(view().name("activations/user-step1"))
                .andExpect(model().attributeExists("tokenFormDTO"));

    }

    @Test
    @WithMockUser
    @DisplayName("verifyActivationToken should return step1 when token is invalid")
    void verifyActivationTokenShouldReturnStep1WhenTokenIsInvalid() throws Exception {
        when(userActivationService.tokenIsNotUsed("ABC")).thenReturn(false);

        mockMvc.perform(post("/activate/user")
                        .param("token", "ABC")
                        .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(view().name("activations/user-step1"))
                .andExpect(model().attributeHasFieldErrors("tokenFormDTO", "token"));
    }

    @Test
    @WithMockUser
    @DisplayName("verifyActivationToken should return step1 when token is expired")
    void verifyActivationTokenShouldReturnStep1WhenTokenIsExpired() throws Exception {
        when(userActivationService.tokenIsNotUsed("ABC")).thenReturn(true);
        when(userActivationService.tokenHasNotExpired("ABC")).thenReturn(false);

        mockMvc.perform(post("/activate/user")
                        .param("token", "ABC")
                        .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(view().name("activations/user-step1"))
                .andExpect(model().attributeHasFieldErrors("tokenFormDTO", "token"));
    }

    @Test
    @WithMockUser
    @DisplayName("verifyActivationToken should redirect when token is valid")
    void verifyActivationTokenShouldRedirectWhenTokenIsValid() throws Exception {
        when(userActivationService.tokenIsNotUsed("ABC")).thenReturn(true);
        when(userActivationService.tokenHasNotExpired("ABC")).thenReturn(true);

        mockMvc.perform(post("/activate/user")
                        .param("token", "ABC")
                        .with(csrf()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/activate/user/ABC"));
    }

    @Test
    @WithMockUser
    @DisplayName("verifyActivationToken should return step1 when binding errors")
    void verifyActivationTokenShouldReturnStep1WhenBindingErrors() throws Exception {
        mockMvc.perform(post("/activate/user")
                        .param("token", "")
                        .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(view().name("activations/user-step1"));
    }

}
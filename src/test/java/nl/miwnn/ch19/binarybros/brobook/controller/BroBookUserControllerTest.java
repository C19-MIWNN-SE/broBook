package nl.miwnn.ch19.binarybros.brobook.controller;

import nl.miwnn.ch19.binarybros.brobook.model.BroBookUser;
import nl.miwnn.ch19.binarybros.brobook.service.BroBookUserService;
import nl.miwnn.ch19.binarybros.brobook.service.CohortService;
import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Collections;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(BroBookUserController.class)
class BroBookUserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private BroBookUserService userService;

    @MockitoBean
    private CohortService cohortService;

    @Test
    @WithMockUser(roles = "ADMIN")
    void testShowUserOverviewAsAdmin() throws Exception {
        BroBookUser mockUser = new BroBookUser();
        mockUser.setFirstName("Paul");
        List<BroBookUser> userList = List.of(mockUser);

        Page<BroBookUser> mockPage = new PageImpl<>(userList);

        when(userService.getPaginatedUsers(anyInt(), anyInt(), any(), any()))
                .thenReturn(mockPage);

        when(userService.getUserByUsername(any())).thenReturn(mockUser);
        when(cohortService.findAll()).thenReturn(Collections.emptyList());

        mockMvc.perform(get("/user/all"))
                .andExpect(status().isOk())
                .andExpect(view().name("user/overview"))
                .andExpect(model().attributeExists("allUsers"))
                .andExpect(model().attribute("allUsers", userList));
    }

    @Test
    @WithMockUser
    void testGetDetailPageExists() throws Exception {
        BroBookUser targetUser = new BroBookUser();
        targetUser.setId(1L);
        targetUser.setFirstName("Paul");

        BroBookUser currentUser = new BroBookUser();

        when(userService.getUserByUsername(any())).thenReturn(currentUser);
        when(userService.getUserById(1L)).thenReturn(targetUser);
        when(userService.findVisibleUsers(currentUser)).thenReturn(List.of(targetUser));

        mockMvc.perform(get("/info/detail/1"))
                .andExpect(status().isOk())
                .andExpect(view().name("user/details"))
                .andExpect(model().attribute("shownUser", targetUser));
    }

    @Test
    void testAccessDeniedForUnauthenticatedUser() throws Exception {
        mockMvc.perform(get("/user/all"))
                .andExpect(status().isUnauthorized());
    }
}
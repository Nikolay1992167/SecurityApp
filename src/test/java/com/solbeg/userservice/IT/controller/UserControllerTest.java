package com.solbeg.userservice.IT.controller;

import com.solbeg.userservice.dto.response.UserResponse;
import com.solbeg.userservice.entity.User;
import com.solbeg.userservice.exception.NotFoundException;
import com.solbeg.userservice.service.impl.UserServiceImpl;
import com.solbeg.userservice.util.PostgresSqlContainerInitializer;
import com.solbeg.userservice.util.testdata.UserTestData;
import org.json.JSONObject;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.support.PageableExecutionUtils;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.util.Collections;
import java.util.List;
import java.util.UUID;

import static com.solbeg.userservice.util.initdata.InitData.DEFAULT_PAGE_REQUEST_FOR_IT;
import static com.solbeg.userservice.util.initdata.InitData.ID_JOURNALIST;
import static com.solbeg.userservice.util.initdata.InitData.ID_NOT_EXIST;
import static com.solbeg.userservice.util.initdata.InitData.TOKEN_ADMIN;
import static com.solbeg.userservice.util.initdata.InitData.URL_USERS;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.http.HttpHeaders.AUTHORIZATION;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc
class UserControllerTest extends PostgresSqlContainerInitializer {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserServiceImpl userService;

    private final String BEARER = "Bearer ";

    @Nested
    class FindAllGetEndpointTest {

        @Test
        @WithMockUser(authorities = "ADMIN")
        void shouldReturnExpectedJsonAndStatus200() throws Exception {
            // given
            PageRequest pageRequest = DEFAULT_PAGE_REQUEST_FOR_IT;
            UserResponse userResponse = UserTestData.builder()
                    .build()
                    .getUserResponse();
            List<UserResponse> usersList = List.of(userResponse);
            Page<UserResponse> page = PageableExecutionUtils.getPage(
                    usersList,
                    pageRequest,
                    usersList::size);
            when(userService.findAll(pageRequest))
                    .thenReturn(page);

            // when, then
            MvcResult mvcResult = mockMvc.perform(get(URL_USERS + "?page=0&size=15"))
                    .andExpect(status().isOk())
                    .andReturn();
            MockHttpServletResponse response = mvcResult.getResponse();
            JSONObject jsonObject = new JSONObject(response.getContentAsString());
            assertThat(jsonObject.get("totalPages")).isEqualTo(1);
            assertThat(jsonObject.get("totalElements")).isEqualTo(1);
            assertThat(jsonObject.get("number")).isEqualTo(0);
            assertThat(jsonObject.get("size")).isEqualTo(15);
            assertThat(jsonObject.get("content")).isNotNull();
        }

        @Test
        @WithMockUser(authorities = "ADMIN")
        void shouldReturnEmptyJsonAndStatus200() throws Exception {
            // given
            PageRequest pageRequest = DEFAULT_PAGE_REQUEST_FOR_IT;
            List<UserResponse> houseList = Collections.emptyList();
            Page<UserResponse> page = PageableExecutionUtils.getPage(
                    houseList,
                    pageRequest,
                    houseList::size);
            when(userService.findAll(pageRequest))
                    .thenReturn(page);

            // when, then
            mockMvc.perform(get(URL_USERS + "?page=0&size=15"))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.content").isEmpty());
        }
    }

    @Nested
    class FindByIdGetEndpointTest {

        @Test
        @WithMockUser(authorities = "ADMIN")
        void shouldReturnExpectedJsonAndStatus200() throws Exception {
            // given
            UUID userId = ID_JOURNALIST;
            UserResponse userResponse = UserTestData.builder()
                    .build()
                    .getUserResponse();
            when(userService.findUserById(userId))
                    .thenReturn(userResponse);

            // when, then
            mockMvc.perform(get(URL_USERS + "/" + userId))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.id").value(userId.toString()));
        }

        @Test
        @WithMockUser(authorities = "ADMIN")
        void shouldReturnThrowExceptionAndStatus404() throws Exception {
            // given
            UUID userId = ID_NOT_EXIST;
            when(userService.findUserById(userId))
                    .thenThrow(NotFoundException.of(User.class, userId));

            // when, then
            mockMvc.perform(get(URL_USERS + "/" + userId))
                    .andExpect(status().isNotFound());
        }
    }

    @Nested
    class UpdatePutEndpointTest {

//        @Test
//        @WithMockUser(authorities = "ADMIN")
//        void shouldReturnExpectedJsonAndStatus200() throws Exception {
//            // given
//            UUID userId = ID_JOURNALIST;
//            UserUpdateRequest updateRequest = UserTestData.builder()
//                    .build()
//                    .getUserUpdateRequest();
//            UserResponse userResponse = UserTestData.builder()
//                    .build()
//                    .getUserResponse();
//            String json = objectMapper.writeValueAsString(userResponse);
//            when(userService.update(userId, updateRequest))
//                    .thenReturn(userResponse);
//
//            // when, then
//            mockMvc.perform(put(URL_USERS + "/" + userId)
//                            .header(CONTENT_TYPE, APPLICATION_JSON_VALUE)
//                            .content(json))
//                    .andExpect(status().isOk())
//                    .andExpectAll(jsonPath("$.id").value(userId.toString()),
//                            jsonPath("$.firstName").value(userResponse.getFirstName()),
//                            jsonPath("$.lastName").value(userResponse.getLastName()),
//                            jsonPath("$.email").value(userResponse.getEmail()));
//        }
    }


    @Nested
    class DeactivateUserPathEndpointTest {

//        @Test
//        @WithMockUser(authorities = "ADMIN")
//        void shouldDeactivateUser() throws Exception {
//            // given
//            UUID userId = ID_JOURNALIST;
//
//            // when
//            doNothing().when(userService).deactivateUser(userId, TOKEN_ADMIN);
//
//            // then
//            mockMvc.perform(patch(URL_USERS + "/deactivate/{id}", userId)
//                            .header(AUTHORIZATION, BEARER + TOKEN_ADMIN)
//                            .contentType(APPLICATION_JSON))
//                    .andExpect(status().isOk());
//            verify(userService, times(1)).deactivateUser(userId, TOKEN_ADMIN);
//        }

//        @Test
//        @WithMockUser(authorities = "ADMIN")
//        void shouldThrowExceptionWhenDeactivateAdminUser() throws Exception {
//            // given
//            UUID userId = ID_JOURNALIST;
//            String token = ACCESS_TOKEN;
//
//            // when
//            doThrow(new InformationChangeStatusUserException("You cannot change the status of a user with the ADMIN role."))
//                    .when(userService).deactivateUser(userId, BEARER + token);
//
//            // then
//            mockMvc.perform(patch(URL_USERS + "/deactivate/" + userId))
//                    .andExpect(status().isConflict());
//        }
    }

    @Nested
    class DeleteUserPathEndpointTest {

//        @Test
//        @WithMockUser(authorities = "ADMIN")
//        void shouldDeleteUser() throws Exception {
//            // given
//            UUID userId = ID_SUBSCRIBER;
//
//            // when
//            doNothing().when(userService).deleteUser(userId, TOKEN_ADMIN);
//
//            // then
//            mockMvc.perform(patch(URL_USERS + "/delete/" + userId)
//                            .header(AUTHORIZATION, BEARER + TOKEN_ADMIN))
//                    .andExpect(status().isOk());
//
//            verify(userService, times(1)).deleteUser(userId, TOKEN_ADMIN);
//        }

//        @Test
//        @WithMockUser(authorities = "ADMIN")
//        void shouldThrowExceptionWhenDeactivateAdminUser() throws Exception {
//            // given
//            UUID userId = ID_SUBSCRIBER;
//            String token = ACCESS_TOKEN;
//
//            // when
//            doThrow(new InformationChangeStatusUserException("You cannot change the status of a user with the ADMIN role."))
//                    .when(userService).deactivateUser(userId, token);
//
//            // then
//            mockMvc.perform(patch(URL_USERS + "/delete/" + userId))
//                    .andExpect(status().isConflict());
//        }
    }
}
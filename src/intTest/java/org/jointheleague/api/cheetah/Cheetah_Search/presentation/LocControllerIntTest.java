package org.jointheleague.api.cheetah.Cheetah_Search.presentation;

import org.jointheleague.api.cheetah.Cheetah_Search.repository.dto.LocResponse;
import org.jointheleague.api.cheetah.Cheetah_Search.repository.dto.Result;
import org.jointheleague.api.cheetah.Cheetah_Search.service.LocService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.util.Collections;
import java.util.List;

import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@WebMvcTest(LocController.class)
class LocControllerIntTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private LocService locService;

    @Test
    public void whenHome_ThenReturnMovedPermanentlyAndRedirect() throws Exception {
        //given
        String query = "Java";
        LocResponse expectedLocResponse = new LocResponse();
        Result result = new Result();
        String title = "Java: A Drink, an Island, and a Programming Language";
        result.setTitle(title);
        String author = "AUTHOR";
        result.setAuthors(Collections.singletonList(author));
        String link = "LINK";
        result.setLink(link);
        expectedLocResponse.setResults(Collections.singletonList(result));

        when(locService.getResults(query)).thenReturn(expectedLocResponse);

        //when
        //then
        MvcResult mvcResult = mockMvc.perform(get("/searchLocResults?q=" + query))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.results[0].title", is(title)))
                .andExpect(jsonPath("$.results[0].authors[0]", is(author)))
                .andExpect(jsonPath("$.results[0].link", is(link)))
                .andReturn();

        assertEquals(MediaType.APPLICATION_JSON_VALUE, mvcResult.getResponse().getContentType());
    }

}

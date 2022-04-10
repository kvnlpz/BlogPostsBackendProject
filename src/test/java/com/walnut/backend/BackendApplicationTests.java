package com.walnut.backend;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;

import java.io.FileInputStream;
import java.util.Scanner;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class BackendApplicationTests {

    @Autowired
    private MockMvc mockMvc;

    @Test
    public void getPing() throws Exception {
        this.mockMvc.perform(get("/api/ping")).andDo(print()).andExpect(status().isOk());
    }


    @Test
    public void getPostsById() throws Exception {
        Scanner reader = new Scanner(new FileInputStream("src/test/resources/jsonSortById.json"));
        getPosts(reader, "id");
    }


    @Test
    public void getPostsByLikes() throws Exception {
        Scanner reader = new Scanner(new FileInputStream("src/test/resources/jsonSortByLikes.json"));
        getPosts(reader, "likes");
    }


    @Test
    public void getPostsByPopularity() throws Exception {
        Scanner reader = new Scanner(new FileInputStream("src/test/resources/jsonSortByPopularity.json"));
        getPosts(reader, "popularity");
    }


    @Test
    public void getPostsByReads() throws Exception {
        Scanner reader = new Scanner(new FileInputStream("src/test/resources/jsonSortByReads.json"));
        getPosts(reader, "reads");
    }


    @Test
    public void getPostsWithoutTags() throws Exception {
        this.mockMvc.perform(get("/api/posts?sortBy=id&direction=asc"))
                .andDo(print())
                .andDo(MockMvcResultHandlers.print())
                .andExpect(content().json("{\"error\":\"Tags parameter is required\"}"))
                .andExpect(status().isBadRequest())
                .andReturn();
    }


    @Test
    public void getPostsWithIncorrectSortingValue() throws Exception {
        this.mockMvc.perform(get("/api/posts?tags=science,tech,history&sortBy=population&direction=asc"))
                .andDo(print())
                .andDo(MockMvcResultHandlers.print())
                .andExpect(content().json("{\"error\":\"sortBy parameter is invalid\"}"))
                .andExpect(status().isBadRequest())
                .andReturn();
    }


    @Test
    void getPostsWithIncorrectDirection() throws Exception {
        this.mockMvc.perform(get("/api/posts?tags=science,tech,history&sortBy=id&direction=ascc"))
                .andDo(print())
                .andDo(MockMvcResultHandlers.print())
                .andExpect(content().json("{\"error\":\"Direction parameter is incorrect\"}"))
                .andExpect(status().isBadRequest())
                .andReturn();
    }


    private void getPosts(Scanner reader, String criterion) throws Exception {
        StringBuilder jsonString = new StringBuilder();
        while (reader.hasNext()) {
            jsonString.append(reader.next()).append(" ");
        }
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.configure(SerializationFeature.ORDER_MAP_ENTRIES_BY_KEYS, true);
        String content = this.mockMvc.perform(get("/api/posts?tags=science,tech,history&sortBy=" + criterion + "&direction=asc"))
                .andDo(print())
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();
        assertEquals(objectMapper.readTree(jsonString.toString()), objectMapper.readTree(content));
    }

}

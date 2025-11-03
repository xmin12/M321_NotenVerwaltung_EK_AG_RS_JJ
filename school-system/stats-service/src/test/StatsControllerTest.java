package com.example.stats;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(StatsController.class)
class StatsControllerTest {

@Autowired
private MockMvc mockMvc;

@MockBean
private StatsRepository statsRepository;

@MockBean
private StatsService statsService;

@Autowired
private ObjectMapper objectMapper;

private Stats stat1;
private Stats stat2;

@BeforeEach
void setUp() {
    stat1 = new Stats(1L, 101L, "Math", 6.0, 4.0, 5.0);
    stat2 = new Stats(2L, 101L, "History", 5.0, 2.0, 3.5);
}

@Test
void testGetAllStats() throws Exception {
    when(statsRepository.findAll()).thenReturn(List.of(stat1, stat2));

    mockMvc.perform(get("/api/stats"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$", hasSize(2)))
            .andExpect(jsonPath("$[0].highestGrade", is(6.0)))
            .andExpect(jsonPath("$[1].averageGrade", is(3.5)));
}

@Test
void testGetStatsByStudent() throws Exception {
    when(statsRepository.findByStudentId(101L)).thenReturn(List.of(stat1, stat2));

    mockMvc.perform(get("/api/stats/student/101"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$", hasSize(2)));
}

@Test
void testGetStatsBySubject() throws Exception {
    when(statsRepository.findBySubject("Math")).thenReturn(List.of(stat1));

    mockMvc.perform(get("/api/stats/subject/Math"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$", hasSize(1)))
            .andExpect(jsonPath("$[0].subject", is("Math")))
            .andExpect(jsonPath("$[0].highestGrade", is(6.0)));
}

@Test
void testRefreshStats() throws Exception {
    List<Stats> generatedStats = List.of(stat1, stat2);

    doNothing().when(statsRepository).deleteAll();
    when(statsService.generateStats()).thenReturn(generatedStats);
    when(statsRepository.saveAll(generatedStats)).thenReturn(generatedStats);

    mockMvc.perform(post("/api/stats/refresh")
                    .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$", hasSize(2)));

    verify(statsRepository).deleteAll();
    verify(statsService).generateStats();
    verify(statsRepository).saveAll(generatedStats);
}

}

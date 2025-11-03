package com.example.stats;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class StatsRepositoryTest {

@Autowired
private StatsRepository statsRepository;

private Stats stat1;
private Stats stat2;
private Stats stat3;

@BeforeEach
void setUp() {
    statsRepository.deleteAll();

    stat1 = new Stats(null, 101L, "Math", 6.0, 4.0, 5.0);
    stat2 = new Stats(null, 101L, "History", 5.0, 2.0, 3.5);
    stat3 = new Stats(null, 102L, "Math", 4.0, 3.0, 3.5);

    statsRepository.saveAll(List.of(stat1, stat2, stat3));
}

@Test
void testFindAll() {
    List<Stats> stats = statsRepository.findAll();
    assertThat(stats).hasSize(3);
    assertThat(stats).extracting("subject")
            .containsExactlyInAnyOrder("Math", "History", "Math");
}

@Test
void testFindByStudentId() {
    List<Stats> stats = statsRepository.findByStudentId(101L);
    assertThat(stats).hasSize(2);
    assertThat(stats).extracting("subject").containsExactlyInAnyOrder("Math", "History");

    List<Stats> noStats = statsRepository.findByStudentId(999L);
    assertThat(noStats).isEmpty();
}

@Test
void testFindBySubject() {
    List<Stats> mathStats = statsRepository.findBySubject("Math");
    assertThat(mathStats).hasSize(2);
    assertThat(mathStats).extracting("studentId").containsExactlyInAnyOrder(101L, 102L);

    List<Stats> historyStats = statsRepository.findBySubject("History");
    assertThat(historyStats).hasSize(1);
    assertThat(historyStats.get(0).getStudentId()).isEqualTo(101L);

    List<Stats> noStats = statsRepository.findBySubject("Biology");
    assertThat(noStats).isEmpty();
}

}

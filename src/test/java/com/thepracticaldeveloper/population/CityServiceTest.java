package com.thepracticaldeveloper.population;

import java.util.concurrent.ThreadLocalRandom;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.*;

@RunWith(MockitoJUnitRunner.class)
public class CityServiceTest {

    @Mock
    private CityRepository cityRepository;

    @Mock
    private PopulationService populationService;

    private CityService cityService;

    @Before
    public void setup() {
        cityService = new CityService(cityRepository, populationService);
    }

    @Test
    public void enrichCity() {
        final String testCityName = "Malaga";
        final int testPopulation = 569_000;
        final City inputCity = new City(null, testCityName, null);

        // Given
        given(populationService.forCity(testCityName))
            .willReturn(testPopulation);
        given(cityRepository.save(any(City.class)))
            .willAnswer(answer -> ((City) answer.getArgument(0)).copyWithId(randomLong()));

        // When
        final City actualCity = cityService.enrichAndSaveCity(inputCity);

        // Then
        final City expectedCity = new City(null, testCityName, testPopulation);
        assertThat(actualCity.getId()).as("City ID should be set")
            .isNotNull();
        assertThat(actualCity).as("City should be enriched")
            .isEqualToIgnoringGivenFields(expectedCity, "id");
    }

    private long randomLong() {
        return ThreadLocalRandom.current().nextLong(1000L);
    }
}

package com.thepracticaldeveloper.population;

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
        given(populationService.forCity(testCityName)).willReturn(testPopulation);
        given(cityRepository.save(any(City.class)))
            .willAnswer(answer -> ((City) answer.getArgument(0)).copyWithId(1L));

        // When
        final City actualCity = cityService.enrichAndSaveCity(inputCity);

        // Then
        final City expectedCity = new City(1L, testCityName, testPopulation);
        assertThat(actualCity).as("City should be enriched and stored").isEqualTo(expectedCity);
    }
}

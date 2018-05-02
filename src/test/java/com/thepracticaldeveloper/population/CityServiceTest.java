package com.thepracticaldeveloper.population;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;

@RunWith(MockitoJUnitRunner.class)
public class CityServiceTest {

    @Mock
    private CityRepository cityRepository;

    @Mock
    private PopulationService populationService;

    private CityService cityService;

    @Test
    public void enrichCity() {
        final String testCityName = "Malaga";
        final int testPopulation = 569_000;
        final City testCity = new City(testCityName, testPopulation);

        given(populationService.forCity(testCityName)).willReturn(testPopulation);
        given(cityRepository.save(testCity)).willReturn(cityWithId);

        final City storedCity = cityService.enrichAndSaveCity(testCity);

        assertThat(storedCity).isEqualToIgnoringGivenFields(testCity, "id");
    }
}

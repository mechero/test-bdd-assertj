package com.thepracticaldeveloper.population;

import org.assertj.core.api.Assertions;
import org.assertj.core.api.SoftAssertions;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.ThreadLocalRandom;

import static org.assertj.core.api.Assertions.catchThrowable;
import static org.assertj.core.api.BDDAssertions.then;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

@RunWith(MockitoJUnitRunner.class)
public class CityServiceTest {

  @Mock
  private CityRepository cityRepository;

  @Mock
  private PopulationService populationService;

  private CityService cityService;
  private static final String MALAGA = "Malaga";
  private static final String AMSTERDAM = "Amsterdam";
  private static final String BARCELONA = "Barcelona";
  private static final int MALAGA_POPULATION = 569_000;
  private static final int BARCELONA_POPULATION = 1_609_000;

  @Before
  public void setup() {
    cityService = new CityService(cityRepository, populationService);
    Assertions.useRepresentation(new CityRepresentation());
  }

  @Test
  public void createCity() {
    // Given
    final City inputCity = new City(null, MALAGA, null);
    given(populationService.forCity(MALAGA))
      .willReturn(Optional.of(MALAGA_POPULATION));
    given(cityRepository.save(any(City.class)))
      .willAnswer(answer -> ((City) answer.getArgument(0)).copyWithId(randomLong()));

    // When
    final City actualCity = cityService.enrichAndCreateCity(inputCity);

    // Then
    final City expectedCity = new City(null, MALAGA, MALAGA_POPULATION);
    then(actualCity.getId())
      .as("Check that City ID is set when stored.")
      .isNotNull();
    then(actualCity)
      .as("Check that City name is correct and city population is filled in.")
      .isEqualToIgnoringGivenFields(expectedCity, "id");
  }

  @Test
  public void createCityWithIdThrowsException() {
    // Given
    final City inputCity = new City(1L, MALAGA, null);

    // When
    final Throwable throwable = catchThrowable(() -> cityService.enrichAndCreateCity(inputCity));

    // Then
    then(throwable).as("An IAE should be thrown if a city with ID is passed")
      .isInstanceOf(IllegalArgumentException.class)
      .as("Check that message contains the city name")
      .hasMessageContaining(inputCity.getName());
  }

  @Test
  public void getCities() {
    // Given
    final City malaga = new City(1L, MALAGA, MALAGA_POPULATION);
    // let's say the service did not work for Amsterdam so it's stored without population...
    final City amsterdam = new City(2L, AMSTERDAM, null);
    final City barcelona = new City(3L, BARCELONA, BARCELONA_POPULATION);
    given(cityRepository.getAllCities()).willReturn(List.of(malaga, amsterdam, barcelona));

    // When
    final List<City> cities = cityService.getAllValidCities();

    // Then
    SoftAssertions.assertSoftly(softly -> {
      softly.assertThat(cities)
        .as("Should contain only two cities")
        .hasSize(2);
      softly.assertThat(cities).extracting("population")
        .as("Should not contain null populations")
        .doesNotContainNull();
      softly.assertThat(cities).extracting("name")
        .as("Should contain names in alphabetical order")
        .containsSequence(BARCELONA, MALAGA);
    });
  }

  private long randomLong() {
    return ThreadLocalRandom.current().nextLong(1000L);
  }
}

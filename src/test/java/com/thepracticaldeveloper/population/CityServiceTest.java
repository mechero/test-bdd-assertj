package com.thepracticaldeveloper.population;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.ThreadLocalRandom;

import org.assertj.core.api.Assertions;
import org.assertj.core.api.SoftAssertions;
import org.assertj.core.api.ThrowableAssert;
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
  public static final String MALAGA = "Malaga";
  public static final String AMSTERDAM = "Amsterdam";
  public static final String BARCELONA = "Barcelona";
  public static final int MALAGA_POPULATION = 569_000;
  public static final int BARCELONA_POPULATION = 1_609_000;

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
    assertThat(actualCity.getId())
      .as("Check that City ID is set when stored.")
      .isNotNull();
    assertThat(actualCity)
      .as("Check that City values are the expected ones")
      .isEqualToIgnoringGivenFields(expectedCity, "id");
  }

  @Test
  public void createCityWithIdThrowsException() {
    // Given
    final City inputCity = new City(1L, MALAGA, null);

    // When
    final ThrowableAssert.ThrowingCallable deferredCall = () -> cityService.enrichAndCreateCity(inputCity);

    // Then
    assertThatIllegalArgumentException()
      .as("Check that input city when creating can't have an ID")
      .isThrownBy(deferredCall)
      .as("Check that message contains the city name")
      .withMessageContaining(inputCity.getName());
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

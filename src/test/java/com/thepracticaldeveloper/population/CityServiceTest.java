package com.thepracticaldeveloper.population;

import org.assertj.core.api.Assertions;
import org.assertj.core.api.ThrowableAssert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.concurrent.ThreadLocalRandom;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatIllegalArgumentException;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

@RunWith(MockitoJUnitRunner.class)
public class CityServiceTest {

  @Mock
  private CityRepository cityRepository;

  @Mock
  private PopulationService populationService;

  private CityService cityService;
  public static final String TEST_CITY_NAME = "Malaga";
  public static final int TEST_POPULATION = 569_000;

  @Before
  public void setup() {
    cityService = new CityService(cityRepository, populationService);
    Assertions.useRepresentation(new CityRepresentation());
  }

  @Test
  public void createCity() {
    // Given
    final City inputCity = new City(null, TEST_CITY_NAME, null);
    given(populationService.forCity(TEST_CITY_NAME))
      .willReturn(TEST_POPULATION);
    given(cityRepository.save(any(City.class)))
      .willAnswer(answer -> ((City) answer.getArgument(0)).copyWithId(randomLong()));

    // When
    final City actualCity = cityService.enrichAndCreateCity(inputCity);

    // Then
    final City expectedCity = new City(null, TEST_CITY_NAME, TEST_POPULATION);
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
    final City inputCity = new City(1L, TEST_CITY_NAME, null);

    // When
    final ThrowableAssert.ThrowingCallable deferredCall = () -> cityService.enrichAndCreateCity(inputCity);

    // Then
    assertThatIllegalArgumentException()
      .as("Check that input city when creating can't have an ID")
      .isThrownBy(deferredCall)
      .as("Check that message contains the city name")
      .withMessageContaining(inputCity.getName());
  }

  private long randomLong() {
    return ThreadLocalRandom.current().nextLong(1000L);
  }
}

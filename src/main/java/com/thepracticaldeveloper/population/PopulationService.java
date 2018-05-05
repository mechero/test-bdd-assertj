package com.thepracticaldeveloper.population;

import java.util.Optional;

public interface PopulationService {
  /**
   * Retrieves the population for a given city name
   *
   * @param cityName the name of the city
   * @return an Optional containing the city population if available, otherwise empty.
   */
  Optional<Integer> forCity(final String cityName);
}

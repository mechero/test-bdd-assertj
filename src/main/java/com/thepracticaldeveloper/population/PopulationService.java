package com.thepracticaldeveloper.population;

import java.util.Optional;

public interface PopulationService {
  /**
   * Retrieves the population for a given city name
   *
   * @return its population
   */
  Optional<Integer> forCity(final String cityName);
}

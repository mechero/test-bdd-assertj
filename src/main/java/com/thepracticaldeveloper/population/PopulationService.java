package com.thepracticaldeveloper.population;

public interface PopulationService {
  /**
   * Retrieves the population for a given city name
   * @param cityName
   * @return its population
   */
  int forCity(final String cityName);
}

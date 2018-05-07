package com.thepracticaldeveloper.population;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class CityService {

  private final CityRepository repository;
  private final PopulationService populationService;

  public CityService(final CityRepository repository, final PopulationService populationService) {
    this.repository = repository;
    this.populationService = populationService;
  }

  public City enrichAndCreateCity(final City city) {
    // Validation
    if (city.getId() != null) {
      throw new IllegalArgumentException("City (" + city.getName() + ") can't be created with a predefined ID");
    }
    // External service call
    final Optional<Integer> population = populationService.forCity(city.getName());
    // Enrichment
    final City enrichedCity = new City(city.getId(), city.getName(), population.orElse(null));
    // Storing in repository
    return repository.save(enrichedCity);
  }

  public List<City> getAllValidCities() {
    // Retrieve from repository
    final List<City> storedCities = repository.getAllCities();
    // Remove those with empty population, and sort them alphabetically
    return storedCities.stream()
      // Comment only the following line to check how SoftAssertions work
      .filter(city -> city.getPopulation() != null)
      .sorted(Comparator.comparing(City::getName))
      .collect(Collectors.toList());
  }
}

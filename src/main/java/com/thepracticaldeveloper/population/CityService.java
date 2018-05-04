package com.thepracticaldeveloper.population;

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
    final int population = populationService.forCity(city.getName());
    // Enrichment
    final City enrichedCity = new City(city.getId(), city.getName(), population);
    // Storing in repository
    return repository.save(enrichedCity);
  }
}

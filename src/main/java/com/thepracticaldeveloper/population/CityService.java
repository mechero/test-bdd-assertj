package com.thepracticaldeveloper.population;

public class CityService {

  private final CityRepository repository;
  private final PopulationService populationService;

  public CityService(final CityRepository repository, final PopulationService populationService) {
    this.repository = repository;
    this.populationService = populationService;
  }

  public City enrichAndCreateCity(final City city) {
    final int population = populationService.forCity(city.getName());
    final City enrichedCity = new City(city.getId(), city.getName(), population);
    return repository.save(enrichedCity);
  }
}

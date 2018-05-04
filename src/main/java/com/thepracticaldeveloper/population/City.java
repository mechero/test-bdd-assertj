package com.thepracticaldeveloper.population;

import java.util.Objects;

public final class City {

  private final Long id;
  private final String name;
  private final Integer population;

  public City(final Long id, final String name, final Integer population) {
    this.id = id;
    this.name = name;
    this.population = population;
  }

  public Long getId() {
    return id;
  }

  public String getName() {
    return name;
  }

  public Integer getPopulation() {
    return population;
  }

  public City copyWithId(final Long id) {
    return new City(id, this.name, this.population);
  }

  @Override
  public boolean equals(final Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    final City city = (City) o;
    return Objects.equals(id, city.id) &&
      Objects.equals(name, city.name) &&
      Objects.equals(population, city.population);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id, name, population);
  }

}

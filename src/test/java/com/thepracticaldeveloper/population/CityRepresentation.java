package com.thepracticaldeveloper.population;

import org.assertj.core.presentation.StandardRepresentation;

public class CityRepresentation extends StandardRepresentation {

  @Override
  protected String fallbackToStringOf(Object object) {
    if (object instanceof City) {
      final City city = (City) object;
      return "{id:" + city.getId() + ", name:" + city.getName() + ", population:" + city.getPopulation() + "}";
    }
    return super.fallbackToStringOf(object);
  }
}

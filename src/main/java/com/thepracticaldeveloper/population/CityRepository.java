package com.thepracticaldeveloper.population;

import java.util.List;

public interface CityRepository {

  City save(City city);

  List<City> getAllCities();
}

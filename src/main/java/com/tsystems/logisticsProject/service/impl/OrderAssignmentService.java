package com.tsystems.logisticsProject.service.impl;

import com.tsystems.logisticsProject.dto.CombinationForOrderDto;
import com.tsystems.logisticsProject.entity.*;
import com.tsystems.logisticsProject.mapper.CombinationForOrderMapper;
import com.tsystems.logisticsProject.service.DriverService;
import com.tsystems.logisticsProject.utils.AssignmentParametersCalculator;
import com.tsystems.logisticsProject.utils.TimeCalculator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.*;

@Component
public class OrderAssignmentService {


    private List<CombinationForOrderDto> listOfCombinationForOrderDto;
    private long combinationId;
    private DriverService driverService;
    private CombinationForOrderMapper combinationForOrderMapper;
    private TimeCalculator timeCalculator;
    private AssignmentParametersCalculator assignmentParametersCalculator;

    @Autowired
    private void setDependencies(DriverService driverService, CombinationForOrderMapper
                                 combinationForOrderMapper, TimeCalculator timeCalculator,
                                 AssignmentParametersCalculator assignmentParametersCalculator) {
        this.driverService = driverService;
        this.assignmentParametersCalculator = assignmentParametersCalculator;
        this.combinationForOrderMapper = combinationForOrderMapper;
        this.timeCalculator = timeCalculator;
    }

    public List<CombinationForOrderDto> createListOfCombinationsForOrder(Long orderId) {
        listOfCombinationForOrderDto = new ArrayList<>();
        combinationId = 0;
        timeCalculator.calculateLeftTimeToEndMonth();
        Map<City, List<Truck>> mapOfTrucksForEveryCity = assignmentParametersCalculator.getMapOfTrucksForEveryCity(orderId);
        Set<City> setOfCity = mapOfTrucksForEveryCity.keySet();
        Map<City, Integer> mapOfMaxOptionalNumberOfDrivers = assignmentParametersCalculator.calculateMaxOptionalNumberOfDriversForOrderFromEveryCity(setOfCity, orderId);
        Map<City, Integer> mapOfHoursForOrderFromEveryCity = timeCalculator.calculateTimeForOrderFromEveryCity(setOfCity, orderId);

        // заходим в каждый город, где есть подходящая для выполнения заказа фура
        for (City city : setOfCity) {

            // получаем список фур, которые подходят для выполнения заказа в этом городе
            List<Truck> listOfTruckForCurrentOrderInEveryCity = mapOfTrucksForEveryCity.get(city);

            // получаем оптимальное количество водителей, необходимое для выполнения этого заказа
            int optionalMaxDriversForOrderFromThisCity = mapOfMaxOptionalNumberOfDrivers.get(city);

            // получаем количество часов, необходимое для выполнения данного заказа из этого города
            // с учетом расстояния от города старта и расстояния до города возвращеня домой
            int hoursForOrderFromThisCity = mapOfHoursForOrderFromEveryCity.get(city);

            //заходим в каждую фуру и подбираем ей список водителей, если таковые найдутся
            for (Truck truck : listOfTruckForCurrentOrderInEveryCity) {

                /** т.к вместимость фуры может быть либо 2 либо 3, а на заказ может потребоваться максимум 3 человека
                 * если вместимость фуры, меньше, чем требуемое количество человек,
                 * значит вместимость фуры 2, на заказ требуется 3 человека. 3 человека требуются только на длительные заказы,
                 * где каждый из водителей работает по 8 часов в день.
                 * Т.к фура вмещает только двоих, значит едем по 16 часов в день.
                 */
                if (truck.getCrewSize() < optionalMaxDriversForOrderFromThisCity) {
                    int numberOfDrivers = 2;
                    int workingHoursPerDayForEveryPerson = 8;
                    // количество дней на выполнение заказа = полное количество часов в пути / 16 часов в день ,округляем в большую сторону

                    /** считаем максимальное количество часов ,которое может уже быть потрачено водителем на момент назначения заказа, что бы
                     * ему хватило времени на выполнение заказа
                     */
                    int maxSpentTimeForDriver = timeCalculator.calculateMaxSpentTimeForDriver(hoursForOrderFromThisCity,
                            workingHoursPerDayForEveryPerson, numberOfDrivers);

                    /** осуществляем запрос, который отдас нам водителей, у которых сейчас нет назначенного заказа, которые находятся в том же городе,
                     * что и фура и у которых хватает часов на выполнение заказа.
                     */
                    List<Driver> listOfDrivers = driverService.findDriversForTruck(city, maxSpentTimeForDriver);

                    if (listOfDrivers != null && listOfDrivers.size() != 0) {

                        /** Т.к. вместимость фуры 2, как мы выяснили ранее ,а количество подходящих водителей меньше,
                         * значит мы нашли только 1 подходящего водителя, но он будет ехать все так же по 8 часов в день, тк заказ дальний
                         * таким, образом он будет ехать дольше, но такое же количество времени в день. Значит ему понадобится
                         * больше времени на выполнение заказа, если только месяц не закончится раньше. Пересчитаем необходимое количество времени еще раз
                         * и посмотрим, подходит ли нам все еще этот водителью
                         */
                        if (listOfDrivers.size() < truck.getCrewSize()) {
                            numberOfDrivers = 1;
                            maxSpentTimeForDriver = timeCalculator.calculateMaxSpentTimeForDriver(hoursForOrderFromThisCity,
                                    workingHoursPerDayForEveryPerson, numberOfDrivers);
                            // если у него хватит часов на выполнение заказа, то мы добавляем такой вариант в список возможных вариантов.
                            if (listOfDrivers.get(0).getHoursThisMonth() <= maxSpentTimeForDriver) {
                                addCombinationForOrderInList(truck, hoursForOrderFromThisCity, listOfDrivers, optionalMaxDriversForOrderFromThisCity);
                            }
                            // если количество найденных водителей = вместимости фуры, то просто добавим такой вариант в список возможных вариантов.
                        } else if (listOfDrivers.size() == truck.getCrewSize()) {
                            addCombinationForOrderInList(truck, hoursForOrderFromThisCity, listOfDrivers, optionalMaxDriversForOrderFromThisCity);
                        } else {
                            /**если количество найденных водителей больше, чем вмещает в себя фура, мы из представленного списка выберем тех,
                             * у кого до конца месяца осталось наименьше количество часов, что бы оптимальнее расходовать ресурсы и добавим данную комбинацию в
                             * возможных комбинаций.
                             */
                            List<Driver> listOfDriversForCombination =
                                    assignmentParametersCalculator.returnDriversWithMinValidTimeAsList(listOfDrivers, 2);
                            addCombinationForOrderInList(truck, hoursForOrderFromThisCity, listOfDriversForCombination, optionalMaxDriversForOrderFromThisCity);
                        }
                    }
                } else {
                    /**если необходимое число водителей для выполнения заказа == 1, значит заказ максимум на 12 часов ,
                     * значит один водитель будет выполнять весь заказ, и ему необходимо иметь полное кол-во часов на выполнение заказа
                     */
                    if (optionalMaxDriversForOrderFromThisCity == 1) {
                        int numberOfDrivers = 1;
                        int workingHoursPerDayForEveryPerson = 12;
                        int maxSpentTimeForDriver =
                                timeCalculator.calculateMaxSpentTimeForDriver(hoursForOrderFromThisCity, workingHoursPerDayForEveryPerson,
                                        numberOfDrivers);
                        List<Driver> listOfDrivers = driverService.findDriversForTruck(city, maxSpentTimeForDriver);

                        /** если мы нашли людей, подходящих под это  заказ, то из них выберем того, у кого до конца месяца осталось наименьшее кол-во
                         * свободных часов, что бы оптимальнее расходовать ресурсы.
                         */
                        if (listOfDrivers != null && listOfDrivers.size() != 0) {
                            List<Driver> listOfDriversToCombination =
                                    assignmentParametersCalculator.returnDriversWithMinValidTimeAsList(listOfDrivers, 1);
                            addCombinationForOrderInList(truck, hoursForOrderFromThisCity, listOfDriversToCombination, optionalMaxDriversForOrderFromThisCity);
                        }

                        /**
                         *   если оптимальное количество человек для выполнения заказа = 2, значит заказ от 24 до 48 часов, значит мы предполагаем ,что
                         *   водители будут работать по 12 часов в день, тк заказ при таком расклады выходит максимум на 2 дня.
                         */

                    } else if (optionalMaxDriversForOrderFromThisCity == 2) {
                        int numberOfDrivers = 2;
                        int workingHoursPerDayForEveryPerson = 12;
                        int maxSpentTimeForDriver = timeCalculator.calculateMaxSpentTimeForDriver(hoursForOrderFromThisCity,
                                workingHoursPerDayForEveryPerson, numberOfDrivers);
                        List<Driver> listOfDrivers = driverService.findDriversForTruck(city, maxSpentTimeForDriver);
                        if (listOfDrivers != null && listOfDrivers.size() != 0) {
                            /** если количество водителей в найденом списке, меньше 2 необходимых, значит
                             * з на такое кол-во времени нашелся только 1 водитель, а значит на выполнение заказа времени ему понадобится вдвое больше,
                             *  проверим, хватит ли ему времени в таком случае.
                             */
                            if (listOfDrivers.size() < 2) {
                                numberOfDrivers = 1;

                                int newMaxSpentTimeForDriver = timeCalculator.calculateMaxSpentTimeForDriver(hoursForOrderFromThisCity,
                                        workingHoursPerDayForEveryPerson, numberOfDrivers);

                                // если ему хватает часов на выполнение данного заказа, то мы добавляем данную комбинацию в список комбинаций
                                if (listOfDrivers.get(0).getHoursThisMonth() <= newMaxSpentTimeForDriver) {
                                    addCombinationForOrderInList(truck, hoursForOrderFromThisCity, listOfDrivers, optionalMaxDriversForOrderFromThisCity);
                                }
                                // если список найденных нами водителей = 2, значит мы просто добавим такую комбинацию в список всех комбинаций
                            } else if (listOfDrivers.size() == 2) {
                                addCombinationForOrderInList(truck, hoursForOrderFromThisCity, listOfDrivers, optionalMaxDriversForOrderFromThisCity);
                                /**
                                 * если количество найденных водителей больше ,чем нам необходимо, мы просто выберем из всех тех, у кого до конца месяца осталось наименьшее
                                 * количество часов, что бы оптимальнее использовать ресурсы и добавим такую комбинацию в список всех возможных комбинаций.
                                 */
                            } else {
                                List<Driver> listOfDriversForCombination =
                                        assignmentParametersCalculator.returnDriversWithMinValidTimeAsList(listOfDrivers, 2);

                                addCombinationForOrderInList(truck, hoursForOrderFromThisCity, listOfDriversForCombination, optionalMaxDriversForOrderFromThisCity);
                            }
                        }
                        // раз нам нужно 3 человека, значит заказ длительный, едем по 8 часов каждый
                    } else {
                        int numberOfDrivers = 3;
                        int workingHoursPerDayForEveryPerson = 8;
                        int maxSpentTimeForDriver = timeCalculator.calculateMaxSpentTimeForDriver(hoursForOrderFromThisCity,
                                workingHoursPerDayForEveryPerson, numberOfDrivers);
                        List<Driver> listOfDrivers = driverService.findDriversForTruck(city, maxSpentTimeForDriver);
                        if (listOfDrivers != null && listOfDrivers.size() != 0) {
                            // если мы нашли больше водителей, чем нам нужно ,выберем 3 с наименьшим кол-вом оставшегося времени....
                            if (listOfDrivers.size() > 3) {
                                List<Driver> listOfDriversForCombination =
                                        assignmentParametersCalculator.returnDriversWithMinValidTimeAsList(listOfDrivers, 3);
                                addCombinationForOrderInList(truck, hoursForOrderFromThisCity, listOfDriversForCombination, optionalMaxDriversForOrderFromThisCity);

                            } else if (listOfDrivers.size() == 3) {
                                addCombinationForOrderInList(truck, hoursForOrderFromThisCity, listOfDrivers, optionalMaxDriversForOrderFromThisCity);

                                // если список подходящих водителей меньше ,чем нам нужно, значит мы нашли либо 2 либо 1 водителя
                            } else {
                                if (listOfDrivers.size() == 1) {
                                    numberOfDrivers = 1;
                                    maxSpentTimeForDriver = timeCalculator.calculateMaxSpentTimeForDriver(hoursForOrderFromThisCity,
                                            workingHoursPerDayForEveryPerson, numberOfDrivers);
                                    // проверим ,хватит ли найденному водителю времени после перерасчета и если да, добавим комбинацию.
                                    if (listOfDrivers.get(0).getHoursThisMonth() < maxSpentTimeForDriver) {
                                        addCombinationForOrderInList(truck, hoursForOrderFromThisCity, listOfDrivers, optionalMaxDriversForOrderFromThisCity);
                                    }
                                } else {
                                    numberOfDrivers = 2;
                                    maxSpentTimeForDriver = timeCalculator.calculateMaxSpentTimeForDriver(hoursForOrderFromThisCity,
                                            workingHoursPerDayForEveryPerson, numberOfDrivers);
                                    // проверим хватит ли двум найденным водителя времени на заказ после перерасчета необходимого времени
                                    List<Driver> newListOfDrivers = new ArrayList<>();
                                    for (Driver driver : listOfDrivers) {
                                        if (driver.getHoursThisMonth() < maxSpentTimeForDriver) {
                                            newListOfDrivers.add(driver);
                                        }
                                    }
                                    // если в новом списке осталось 2 водителя, значит им обоим хватило времени, и мы добавляем комбинацию.
                                    if (newListOfDrivers.size() == 2) {

                                        addCombinationForOrderInList(truck, hoursForOrderFromThisCity, newListOfDrivers, optionalMaxDriversForOrderFromThisCity);
                                        /**если в новом списке остался только 1 водитель, значит времени на выполнение заказа после перерасчета хватило только ему
                                         * но тк заказ он будет в таком случае выполнять 1, нам нужно снова перерасчитать время и убедиться, что водителю его хватит.
                                         */
                                    } else if (newListOfDrivers.size() == 1) {
                                        numberOfDrivers = 1;
                                        maxSpentTimeForDriver = timeCalculator.calculateMaxSpentTimeForDriver(hoursForOrderFromThisCity,
                                                workingHoursPerDayForEveryPerson, numberOfDrivers);
                                        if (newListOfDrivers.get(0).getHoursThisMonth() < maxSpentTimeForDriver) {
                                            addCombinationForOrderInList(truck, hoursForOrderFromThisCity, newListOfDrivers, optionalMaxDriversForOrderFromThisCity);
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
        return listOfCombinationForOrderDto;
    }

    private void addCombinationForOrderInList(Truck truck, int hoursForOrderFromThisCity, List<Driver> listOfDriversForCombination, int optionalMaxDriversForOrderFromThisCity) {
        combinationId++;
        CombinationForOrder combinationForOrder = new CombinationForOrder();
        combinationForOrder.setId(combinationId);
        combinationForOrder.setListOfDrivers(listOfDriversForCombination);
        combinationForOrder.setTruck(truck);
        int numberOfDrivers = listOfDriversForCombination.size();
        int totalHours = timeCalculator.calculateTotalHoursForOrderFromThisCity(optionalMaxDriversForOrderFromThisCity,
                hoursForOrderFromThisCity, numberOfDrivers);
        combinationForOrder.setTotalHours(totalHours);
        combinationForOrder.setTotalBillableHours(hoursForOrderFromThisCity);
        listOfCombinationForOrderDto.add(combinationForOrderMapper.toDto(combinationForOrder));
    }

    public CombinationForOrderDto getCombinationForOrderByIndex(int index) {
        return listOfCombinationForOrderDto.get(index);
    }
}
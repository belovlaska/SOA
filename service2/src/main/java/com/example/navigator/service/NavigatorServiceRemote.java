package com.example.navigator.service;

import com.example.navigator.dto.RouteDto;

import jakarta.ejb.Remote;
import java.util.List;

/**
 * Remote-интерфейс бизнес-логики навигатора.
 * Предоставляет операции, описанные во втором веб-сервисе OpenAPI (/navigator/...).
 */
@Remote
public interface NavigatorServiceRemote {

    /**
     * Найти маршруты между двумя локациями и отсортировать по orderBy.
     *
     * @param idFrom  идентификатор начальной локации
     * @param idTo    идентификатор конечной локации
     * @param orderBy поле сортировки (id, name, distance, creationDate, опционально с префиксом "-")
     * @return список маршрутов, соответствующий OpenAPI-схеме Route
     */
    List<RouteDto> getRoutes(Long idFrom, Long idTo, String orderBy);

    /**
     * Добавить новый маршрут между указанными локациями.
     *
     * @param idFrom  идентификатор начальной локации
     * @param idTo    идентификатор конечной локации
     * @param distance расстояние (>= 2)
     * @return созданный маршрут
     */
    RouteDto addRoute(Long idFrom, Long idTo, Long distance);
}


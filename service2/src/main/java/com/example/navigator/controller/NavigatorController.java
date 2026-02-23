package com.example.navigator.controller;

import com.example.navigator.dto.ErrorResponseDto;
import com.example.navigator.dto.RouteDto;
import com.example.navigator.service.NavigatorServiceRemote;
import jakarta.ejb.EJB;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.UriInfo;

import java.net.URI;
import java.util.List;

/**
 * JAX-RS ресурс второго сервиса (/navigator),
 * строго соответствующий спецификации OpenAPI (SOA1.html).
 */
@Path("/navigator")
@Produces(MediaType.APPLICATION_JSON)
public class NavigatorController {

    @EJB
    private NavigatorServiceRemote navigatorService;

    /**
     * GET /navigator
     * Информационный endpoint.
     */
    @GET
    public Response info() {
        return Response.ok("HELLO").build();
    }

    /**
     * GET /navigator/routes/{id-from}/{id-to}/{order-by}
     * Найти маршруты между двумя точками.
     *
     * @param fromId  ID точки отправления (id-from)
     * @param toId    ID точки назначения (id-to)
     * @param orderBy поле сортировки (order-by): id, name, distance, creationDate, с опциональным префиксом "-"
     */
    @GET
    @Path("/routes/{id-from}/{id-to}/{order-by}")
    public Response getRoutes(@PathParam("id-from") Long fromId,
                              @PathParam("id-to") Long toId,
                              @PathParam("order-by") String orderBy) {
        try {
            if (!orderBy.matches("^-?(id|name|distance|creationDate)$")) {
                return Response.status(Response.Status.BAD_REQUEST)
                        .entity(new ErrorResponseDto(400,
                                "Bad Request: Invalid orderBy parameter. " +
                                        "Valid values: id, name, distance, creationDate (use '-' prefix for DESC)"))
                        .build();
            }

            List<RouteDto> routes = navigatorService.getRoutes(fromId, toId, orderBy);
            return Response.ok(routes).build();
        } catch (Exception e) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity(new ErrorResponseDto(400, "Bad Request: " + e.getMessage()))
                    .build();
        }
    }

    /**
     * POST /navigator/route/add/{id-from}/{id-to}/{distance}
     * Добавить маршрут между двумя точками.
     *
     * @param fromId   ID точки отправления (id-from)
     * @param toId     ID точки назначения (id-to)
     * @param distance дистанция (>= 2)
     */
    @POST
    @Path("/route/add/{id-from}/{id-to}/{distance}")
    public Response addRoute(@PathParam("id-from") Long fromId,
                             @PathParam("id-to") Long toId,
                             @PathParam("distance") Long distance,
                             @Context UriInfo uriInfo) {
        try {
            if (distance < 2) {
                return Response.status(Response.Status.BAD_REQUEST)
                        .entity(new ErrorResponseDto(400, "Bad Request: Distance must be >= 2"))
                        .build();
            }

            RouteDto route = navigatorService.addRoute(fromId, toId, distance);
            URI location = uriInfo.getAbsolutePathBuilder()
                    .path(String.valueOf(route.getId()))
                    .build();
            return Response.created(location).entity(route).build();
        } catch (Exception e) {
            return Response.status(Response.Status.CONFLICT)
                    .entity(new ErrorResponseDto(409, "Conflict: " + e.getMessage()))
                    .build();
        }
    }
}

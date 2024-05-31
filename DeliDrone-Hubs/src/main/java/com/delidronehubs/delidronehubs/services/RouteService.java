package com.delidronehubs.delidronehubs.services;

import com.delidronehubs.delidronehubs.classes.Route;
import com.delidronehubs.delidronehubs.repositories.RouteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class RouteService {
    private final RouteRepository routeRepository;

    @Autowired
    public RouteService(RouteRepository routeRepository) {
        this.routeRepository = routeRepository;
    }

    public List<Route> getAllRoutes() {
        return routeRepository.findAll();
    }

    public Route getRouteById(UUID id) {
        return routeRepository.findById(id).orElse(null);
    }

    public Route createRoute(Route route) {
        return routeRepository.save(route);
    }

    public Route updateRoute(UUID id, Route route) {
        if (routeRepository.existsById(id)) {
            route.setId(id);
            return routeRepository.save(route);
        }
        return null;
    }

    public void deleteRoute(UUID id) {
        routeRepository.deleteById(id);
    }
}

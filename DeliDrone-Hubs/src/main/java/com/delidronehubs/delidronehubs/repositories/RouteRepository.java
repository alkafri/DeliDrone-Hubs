package com.delidronehubs.delidronehubs.repositories;

import com.delidronehubs.delidronehubs.classes.Route;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface RouteRepository extends JpaRepository<Route, UUID> {

}

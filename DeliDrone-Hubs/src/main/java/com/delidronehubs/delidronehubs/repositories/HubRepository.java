package com.delidronehubs.delidronehubs.repositories;

import com.delidronehubs.delidronehubs.classes.Hub;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface HubRepository extends JpaRepository<Hub, UUID> {

}

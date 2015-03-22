package com.g200001.dutyapp.repository;

import com.g200001.dutyapp.domain.Incident;
import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the Incident entity.
 */
public interface IncidentRepository extends JpaRepository<Incident, String> {

}

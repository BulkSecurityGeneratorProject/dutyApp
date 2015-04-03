package com.g200001.dutyapp.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.g200001.dutyapp.domain.Incident;
import com.g200001.dutyapp.domain.Service;

/**
 * Spring Data JPA repository for the Incident entity.
 */
public interface IncidentRepository extends JpaRepository<Incident, String> {
	List<Incident> findAllByService(Service service);
}

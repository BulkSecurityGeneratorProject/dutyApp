package com.g200001.dutyapp.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.g200001.dutyapp.domain.Alert;
import com.g200001.dutyapp.domain.Incident;

/**
 * Spring Data JPA repository for the Alert entity.
 */
public interface AlertRepository extends JpaRepository<Alert, String> {

    //@Query("select alert from Alert alert where alert.user.login = ?#{principal.username}")
    //List<Alert> findAllForCurrentUser();
	
	List<Alert> findAllByIncident(Incident incident);

}

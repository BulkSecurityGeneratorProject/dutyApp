package com.g200001.dutyapp.repository;

import com.g200001.dutyapp.domain.Alert;
import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the Alert entity.
 */
public interface AlertRepository extends JpaRepository<Alert, String> {

    @Query("select alert from Alert alert where alert.user.login = ?#{principal.username}")
    List<Alert> findAllForCurrentUser();

}

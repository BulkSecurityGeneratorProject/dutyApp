package com.g200001.dutyapp.repository;

import com.g200001.dutyapp.domain.Service;
import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the Service entity.
 */
public interface ServiceRepository extends JpaRepository<Service, String> {

}

package com.g200001.dutyapp.repository;

import com.g200001.dutyapp.domain.Watchman;
import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the Watchman entity.
 */
public interface WatchmanRepository extends JpaRepository<Watchman, String> {

}

package com.g200001.dutyapp.repository;

import com.g200001.dutyapp.domain.EscalationPolicy;
import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the EscalationPolicy entity.
 */
public interface EscalationPolicyRepository extends JpaRepository<EscalationPolicy, String> {

}

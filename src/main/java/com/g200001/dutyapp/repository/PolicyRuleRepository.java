package com.g200001.dutyapp.repository;

import com.g200001.dutyapp.domain.EscalationPolicy;
import com.g200001.dutyapp.domain.PolicyRule;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the PolicyRule entity.
 */
public interface PolicyRuleRepository extends JpaRepository<PolicyRule, String> {
	PolicyRule  findOneByEscalationPolicyAndSequence(EscalationPolicy escalationpolicy,int sequence);
}

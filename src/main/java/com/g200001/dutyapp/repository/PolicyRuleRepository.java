package com.g200001.dutyapp.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.g200001.dutyapp.domain.EscalationPolicy;
import com.g200001.dutyapp.domain.PolicyRule;

/**
 * Spring Data JPA repository for the PolicyRule entity.
 */
public interface PolicyRuleRepository extends JpaRepository<PolicyRule, String> {	
	PolicyRule  findOneByEscalationPolicyAndSequence(EscalationPolicy escalationpolicy,int sequence);
	
	List<PolicyRule> findByEscalationPolicy(EscalationPolicy escalationpolicy);
	
	Page<PolicyRule> findByEscalationPolicy(EscalationPolicy escalationpolicy, Pageable pageable);
}

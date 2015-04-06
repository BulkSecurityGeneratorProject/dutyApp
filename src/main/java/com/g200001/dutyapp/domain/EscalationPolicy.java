package com.g200001.dutyapp.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

/**
 * A EscalationPolicy.
 */
@Entity
@Table(name = "T_ESCALATIONPOLICY")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class EscalationPolicy implements Serializable {

	@Id
    @GeneratedValue(generator = "uuid")
    @GenericGenerator(name = "uuid", strategy = "uuid")
    private String id;

    @Column(name = "policy_name")
    private String policy_name;

    @Column(name = "has_cycle")
    private Boolean has_cycle;

    @Column(name = "cycle_time")
    private Long cycle_time;

    @OneToMany(mappedBy = "escalationPolicy")
    @JsonIgnore
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<Service> services = new HashSet<>();

    //@OneToMany(mappedBy = "escalationPolicy")
    @OneToMany(mappedBy = "escalationPolicy",fetch=FetchType.EAGER, cascade=CascadeType.ALL )
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<PolicyRule> policyRules = new HashSet<>();

    //Empty Constructor
    public EscalationPolicy() {}
    
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPolicy_name() {
        return policy_name;
    }

    public void setPolicy_name(String policy_name) {
        this.policy_name = policy_name;
    }

    public Boolean getHas_cycle() {
        return has_cycle;
    }

    public void setHas_cycle(Boolean has_cycle) {
        this.has_cycle = has_cycle;
    }

    public Long getCycle_time() {
        return cycle_time;
    }

    public void setCycle_time(Long cycle_time) {
        this.cycle_time = cycle_time;
    }

    public Set<Service> getServices() {
        return services;
    }

    public void setServices(Set<Service> services) {
        this.services = services;
    }
    
    public Set<PolicyRule> getPolicyRules() {
    	return policyRules;
    }
    
    public void setPolicyRules(Set<PolicyRule> policyRules) {
    	this.policyRules = policyRules;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        EscalationPolicy escalationPolicy = (EscalationPolicy) o;

        if (id != null ? !id.equals(escalationPolicy.id) : escalationPolicy.id != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
    	return id==null? 0 : id.hashCode();
    }

    @Override
    public String toString() {
        return "EscalationPolicy{" +
                "id=" + id +
                ", policy_name='" + policy_name + "'" +
                ", has_cycle='" + has_cycle + "'" +
                ", cycle_time='" + cycle_time + "'" +
                ", policy_rules has '" + policyRules.size() +
                '}';
    }
}

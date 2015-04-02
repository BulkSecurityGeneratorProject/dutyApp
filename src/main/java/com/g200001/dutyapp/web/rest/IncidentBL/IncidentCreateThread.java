package com.g200001.dutyapp.web.rest.IncidentBL;
import javax.inject.Inject;

import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.g200001.dutyapp.domain.Alert;
import com.g200001.dutyapp.domain.EscalationPolicy;
import com.g200001.dutyapp.domain.Incident;
import com.g200001.dutyapp.domain.PolicyRule;
import com.g200001.dutyapp.domain.User;
import com.g200001.dutyapp.repository.PolicyRuleRepository;
import com.g200001.dutyapp.web.rest.AlertResource;
import com.g200001.dutyapp.web.rest.IncidentResource;

public class IncidentCreateThread extends Thread
    {
    	private final Logger log = LoggerFactory.getLogger(IncidentResource.class);
    	 
    	@Inject
    	private PolicyRuleRepository policyRuleRepository;
    	
    	private Incident _incident;
    	public IncidentCreateThread(Incident incident)
    	{
    		_incident = incident;
    	}
    	
    	public void run(){
    		
    		EscalationPolicy  escalationPolicy = _incident.getService().getEscalationPolicy();
    		if (escalationPolicy!=null)
    		{
    			int i=1;
    			PolicyRule rule=policyRuleRepository.findOneByEscalationPolicyAndSequence(escalationPolicy, i);
        		while(rule!=null)
        		{
        			CreateUserAlert(rule);
        			rule=policyRuleRepository.findOneByEscalationPolicyAndSequence(escalationPolicy, ++i);        			
        		}
        		
        		
    		}
    		else
    		{
    			//throw exception
    		}   		
    	}
    	public void CreateUserAlert(PolicyRule rule) 
    	{
    		AlertResource alertresource = new AlertResource();
    		if (rule != null)
    		{
    			try
    			{
    				Thread.currentThread();
					Thread.sleep( rule.getEscalate_time()*60*1000);
    			}
    			catch(Exception ex)
    			{
    				//throw exception thread sleep failed
    			}
    			for(User user:rule.getUsers()){
    				Alert alert=new Alert();
    				alert.setIncident(_incident);
    				alert.setUser(user);
    				alert.setAlert_time(DateTime.now());
    				try
    				{
    					alertresource.create(alert);
    				}
    				catch(Exception ex)
    				{
    					//throw exception
    				}
    				//do the Alert thing after each alert record created
    			}
    		}
    		else    			
    		{
    			//throw exception
    		}
    		
    	}
    }
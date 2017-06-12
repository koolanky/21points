package com.jhipster.health.web.rest.vm;

import java.util.List;

import com.jhipster.health.domain.Bloodpressure;

public class BloodpressureByPeriod {
	private String period;
	private List<Bloodpressure> readings;
	
	public BloodpressureByPeriod(String period, List<Bloodpressure> readings) {
		this.period = period;
		this.readings = readings;
		}

}

/* *********************************************************************** *
 * project: org.matsim.*
 *                                                                         *
 * *********************************************************************** *
 *                                                                         *
 * copyright       : (C) 2008 by the members listed in the COPYING,        *
 *                   LICENSE and WARRANTY file.                            *
 * email           : info at matsim dot org                                *
 *                                                                         *
 * *********************************************************************** *
 *                                                                         *
 *   This program is free software; you can redistribute it and/or modify  *
 *   it under the terms of the GNU General Public License as published by  *
 *   the Free Software Foundation; either version 2 of the License, or     *
 *   (at your option) any later version.                                   *
 *   See also COPYING, LICENSE and WARRANTY file                           *
 *                                                                         *
 * *********************************************************************** */

package org.matsim.households;

import org.matsim.api.basic.v01.Id;
import org.matsim.core.basic.v01.households.BasicHouseholdBuilderImpl;
import org.matsim.core.basic.v01.households.BasicIncome;
import org.matsim.core.basic.v01.households.HouseholdBuilder;
import org.matsim.core.basic.v01.households.BasicIncome.IncomePeriod;

/**
 * @author dgrether
 */
public class HouseholdBuilderImpl implements HouseholdBuilder {

	private BasicHouseholdBuilderImpl delegate;

	public HouseholdBuilderImpl() {
		this.delegate = new BasicHouseholdBuilderImpl();
	}

	public Household createHousehold(Id householdId) {
		Household h = new HouseholdImpl(householdId);
		return h;
	}


	public BasicIncome createIncome(double income, IncomePeriod period) {
		return this.delegate.createIncome(income, period);
	}

}

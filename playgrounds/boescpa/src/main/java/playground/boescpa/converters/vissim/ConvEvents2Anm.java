/*
 * *********************************************************************** *
 * project: org.matsim.*                                                   *
 *                                                                         *
 * *********************************************************************** *
 *                                                                         *
 * copyright       : (C) 2014 by the members listed in the COPYING,        *
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
 * *********************************************************************** *
 */

package playground.boescpa.converters.vissim;

import org.matsim.api.core.v01.Id;
import org.matsim.api.core.v01.network.Network;

import java.util.HashMap;

/**
 * Provides the environment to convert MATSim-Events to an ANMRoutes file importable in VISSIM.
 *
 * @author boescpa
 */
public class ConvEvents2Anm {

	private final NetworkMatcher networkMatcher;
	private final EventsConverter eventsConverter;
	private final AnmConverter anmConverter;
	private final TripMatcher tripMatcher;

	public ConvEvents2Anm(NetworkMatcher networkMatcher, EventsConverter eventsConverter,
						  AnmConverter anmConverter, TripMatcher tripMatcher) {
		this.networkMatcher = networkMatcher;
		this.eventsConverter = eventsConverter;
		this.anmConverter = anmConverter;
		this.tripMatcher = tripMatcher;
	}

	public static void main(String[] args) {
		ConvEvents2Anm convEvents2Anm = createDefaultConvEvents2Anm();
		convEvents2Anm.convert(args);
	}

	public static ConvEvents2Anm createDefaultConvEvents2Anm() {
		return null;
		//return new ConvEvents2Anm(new DefaultNetworkMatcher(), new DefaultEventsConverter(),
		//new DefaultAnmConverter(), new DefaultTripMatcher());
	}

	public void convert(String[] args) {
		String path2MATSimNetwork = args[0];
		String path2VissimNetwork = args[1];
		String path2EventsFile = args[2];
		String path2AnmFile = args[3];
		String path2NewAnmFile = args[4];

		Network matchedNetwork = this.networkMatcher.matchNetworks(path2MATSimNetwork, path2VissimNetwork);
		HashMap<Id, Long[]> msTrips = this.eventsConverter.convertEvents(matchedNetwork, path2EventsFile);
		HashMap<Id, Long[]> amTrips = this.anmConverter.convertRoutes(matchedNetwork, path2AnmFile);
		HashMap<Id, Integer> demandPerAnmTrip = this.tripMatcher.matchTrips(msTrips, amTrips);
		this.anmConverter.writeAnmRoutes(demandPerAnmTrip, path2AnmFile, path2NewAnmFile);
	}

	public interface NetworkMatcher {

		/**
		 * Match networks (<-> find mutual network representation).
		 *
		 * @param path2MATSimNetwork
		 * @param path2VissimNetwork
		 * @return A new gis data set (nodes, links) which represents both input networks jointly.
		 */
		public Network matchNetworks(String path2MATSimNetwork, String path2VissimNetwork);
	}

	public interface EventsConverter {

		/**
		 * Convert MATSim-Events to trips in matched network
		 *
		 * @param matchedNetwork
		 * @param path2EventsFile
		 * @return A HashMap which represents each trip (derived from events, assigned a trip Id) in the form of
		 * 			an id-array (Long[]) representing a sequence of elements of the matched network.
		 */
		public HashMap<Id,Long[]> convertEvents(Network matchedNetwork, String path2EventsFile);
	}

	public interface AnmConverter {

		/**
		 * Convert ANM-Routes to trips in matched network
		 *
		 * @param matchedNetwork
		 * @param path2AnmFile
		 * @return A HashMap which represents each trip (derived from AnmRoutes, assigned the AnmRoute Id) in the form
		 * 			of an id-array (Long[]) representing a sequence of elements of the matched network.
		 */
		public HashMap<Id,Long[]> convertRoutes(Network matchedNetwork, String path2AnmFile);

		/**
		 * Rewrite ANMRoutes file with new demand numbers for each ANM-Route
		 *
		 * @param demandPerAnmTrip
		 * @param path2AnmFile
		 * @param path2NewAnmFile At the specified location a new ANMRoutes-File will be created. It is an exact copy
		 *                        of the given AnmFile except for the demands stated at the routes. These are the new
		 *                        demands given in demandPerAnmTrip.
		 */
		public void writeAnmRoutes(HashMap<Id, Integer> demandPerAnmTrip, String path2AnmFile, String path2NewAnmFile);
	}

	public interface TripMatcher {

		/**
		 * Find for each MATSim-trip a best match in the ANM-trips
		 *
		 * @param msTrips
		 * @param amTrips
		 * @return A HashMap having for each ANM-trip (Id) the number (Integer) of MATSim-trips which were found to
		 * 			to match this ANM-trip best.
		 */
		HashMap<Id,Integer> matchTrips(HashMap<Id, Long[]> msTrips, HashMap<Id, Long[]> amTrips);
	}
}

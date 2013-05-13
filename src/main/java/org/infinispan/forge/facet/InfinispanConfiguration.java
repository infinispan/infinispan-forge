/**
 * Copyright 2012 Red Hat, Inc. and/or its affiliates.
 *
 * This is free software; you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation; either version 2.1 of
 * the License, or (at your option) any later version.
 *
 * This software is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA
 * 02110-1301 USA
 */
package org.infinispan.forge.facet;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Andre Dietisheim
 */
public enum InfinispanConfiguration {

	
	LOCAL("infinispan-local.xml"),
	CLUSTERED_TCP("infinispan-clustered-tcp.xml"),
	CLUSTERED_UDP("infinispan-clustered-udp.xml"),
	CLUSTERED_EC2("infinispan-clustered-ec2.xml");

	private static final String CONFIGURATIONS_FOLDER = "/configurations/";

	private InputStream inputStream;
	private String configurationName;

	private InfinispanConfiguration(String configurationName) {
		this.configurationName = toHumanReadable(configurationName);
		this.inputStream = getClass().getResourceAsStream(CONFIGURATIONS_FOLDER + configurationName);
	}

	public InputStream getResourceAsStream() {
		return inputStream;
	}

	public String getConfigurationName() {
		return configurationName;
	}

	private String toHumanReadable(String configurationName) {
		String humanReadableName = configurationName;
		int suffixIndex = configurationName.lastIndexOf('.');
		int prefixIndex = configurationName.indexOf('-');
		if (suffixIndex > -1) {
			if (prefixIndex > -1) {
				humanReadableName = configurationName.substring(prefixIndex + 1, suffixIndex);
			} else {
				humanReadableName = configurationName.substring(0, suffixIndex);
			}
		} else {
			if (prefixIndex > -1) {
				humanReadableName.substring(prefixIndex);
			} 
		}
		return humanReadableName;
	}

	public static List<String> getNames() {
		List<String> names = new ArrayList<String>();
		for(InfinispanConfiguration configuration : values()) {
			names.add(configuration.getConfigurationName());
		}
		return names;
	}
	
	public static String[] getNamesArray() {
		return getNames().toArray(new String[] {});
	}

	public static InfinispanConfiguration getByName(String configurationName) {
		if (configurationName == null
				|| configurationName.isEmpty()) {
			throw new IllegalArgumentException("Could not lookup empty configuration name");
		}
		InfinispanConfiguration matchingConfiguration = null;
		for (InfinispanConfiguration configuration : values()) {
			if (configurationName.equals(configuration.getConfigurationName())) {
				matchingConfiguration = configuration;
				break;
			}
		}
		return matchingConfiguration;
	}
}

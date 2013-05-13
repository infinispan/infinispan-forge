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
package org.infinispan.forge.util;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.util.Collection;

/**
 * @author Andre Dietisheim
 */
public class StringUtils {

	public static boolean isEmpty(String value) {
		return value == null
				|| value.isEmpty();
	}

	public static InputStream toInputStream(String value) throws UnsupportedEncodingException {
		if (value == null) {
			return null;
		}
		return new ByteArrayInputStream(value.getBytes("UTF-8"));
	}

	public static String toString(Collection<Object> collection) {
		if (collection == null) {
			return "";
		}
		StringBuilder builder = new StringBuilder();
		for (Object item : collection) {
			if (item == null) {
				continue;
			}
			builder.append(String.valueOf(item));
		}
		return builder.toString();
	}
	
}

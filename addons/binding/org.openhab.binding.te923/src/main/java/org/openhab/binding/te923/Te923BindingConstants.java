/**
 * Copyright (c) 2010-2017 by the respective copyright holders.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.te923;

import org.eclipse.jdt.annotation.NonNullByDefault;
import org.eclipse.smarthome.core.thing.ThingTypeUID;

/**
 * The {@link Te923BindingConstants} class defines common constants, which are
 * used across the whole binding.
 *
 * @author Gabriel Klein - Initial contribution
 */
@NonNullByDefault
public class Te923BindingConstants {

    private static final String BINDING_ID = "te923";

    // List of all Thing Type UIDs
    public static final ThingTypeUID THING_TYPE_WEATHER = new ThingTypeUID(BINDING_ID, "weather");

}

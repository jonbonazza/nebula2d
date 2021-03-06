/*
 * Nebula2D is a cross-platform, 2D game engine for PC, Mac, & Linux
 * Copyright (c) 2014 Jon Bonazza
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package com.suiton2d.editor.util;

import java.awt.Dimension;
import java.awt.Toolkit;
import java.io.File;


/**
 * Utility class for platform specific functionality
 */
public class PlatformUtil {

    /**
     * Determines whether or not the app is running on a Mac computer.
     *
     * @return Whether or not the app is running on a mac computer.
     */
    public static boolean isMac() {
        return System.getProperty("os.name").toLowerCase().contains("mac");
    }

    /**
     * Joins a number of paths into one path.
     * @param paths an array of paths.
     *
     * @return the conjoined path.
     */
    public static String pathJoin(String... paths) {
        StringBuilder sb = new StringBuilder();
        for (String path : paths) {
            if (!path.endsWith(File.separator))
                path += File.separator;
            sb.append(path);
        }

        return sb.toString();
    }

    public static Dimension getScreenSize() {
        Toolkit tk = Toolkit.getDefaultToolkit();
        return tk.getScreenSize();
    }
}

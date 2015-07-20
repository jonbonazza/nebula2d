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

package com.suiton2d.editor.framework;

import com.suiton2d.editor.ui.MainFrame;
import com.suiton2d.scene.GameObject;
import com.suiton2d.scene.Layer;
import com.suiton2d.scene.Scene;

import javax.swing.tree.DefaultMutableTreeNode;

public class SceneNode<T> extends DefaultMutableTreeNode {

    private String name;
    private T data;

    public SceneNode(String name) {
        this.name = name;
    }

    public SceneNode(String name, T data) {
        this(name);
        this.data = data;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public void remove() {
        SceneNode parent = (SceneNode) getParent();
        parent.remove(this);
        Object parentData = parent.getData();
        if (parentData instanceof Scene)
            ((Scene) parentData).removeLayer(((Layer) data));
        else if (parentData instanceof Layer)
            ((Layer) parentData).removeGameObject(((GameObject) data));
        else
            ((GameObject) parentData).removeActor(((GameObject) data));

        MainFrame.getSceneGraph().refresh();
    }

    @Override
    public String toString() {
        return name;
    }
}

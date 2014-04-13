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

package com.nebula2d.editor.framework.components;

import com.nebula2d.avocado.SaveListener;
import com.nebula2d.avocado.ScriptEditor;
import com.nebula2d.editor.framework.assets.Script;
import com.nebula2d.editor.ui.ComponentsDialog;
import com.nebula2d.editor.util.FullBufferedReader;
import com.nebula2d.editor.util.FullBufferedWriter;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

public class Behaviour extends Component {

    //region members
    protected Script script;
    //endregion

    //region constructor
    public Behaviour(String name) {
        super(name);
        componentType = ComponentType.BEHAVE;
    }

    public Behaviour() {
        this("");
    }
    //endregion

    //region overrided methods from Component
    @Override
    public JPanel forgeComponentContentPanel(final ComponentsDialog parent) {
        final JLabel nameLbl = new JLabel("Name:");
        final JLabel fileLbl = new JLabel("File:");
        final JLabel filePathLbl = new JLabel("");
        final JTextField nameTf = new JTextField(name, 20);
        final JCheckBox enabledCb = new JCheckBox("Enabled", enabled);
        final JButton browseBtn = new JButton("...");
        final JButton editBtn = new JButton("edit");
        editBtn.setEnabled(false);
        final JButton newBtn = new JButton("new");

        newBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                script = new Script();
                ScriptEditor editor = new ScriptEditor("new script*", Script.NEW_SCRIPT_CONTENT);
                editor.setSaveCallback(new SaveListener() {
                    @Override
                    public void onSave(String content, String path) {
                        script.setContent(content);
                        script.setPath(path);
                        filePathLbl.setText(path);
                        editBtn.setEnabled(true);
                    }
                });
                editor.setVisible(true);
            }
        });

        enabledCb.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                enabled = enabledCb.isSelected();
            }
        });

        editBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                ScriptEditor editor = new ScriptEditor(script.getName(), script.getPath(), script.getContent());
                editor.setSaveCallback(new SaveListener() {
                    @Override
                    public void onSave(String content, String path) {
                        script.setContent(content);
                        script.setPath(path);
                    }
                });
                editor.setVisible(true);
            }
        });

        browseBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                final JFileChooser fc = new JFileChooser();
                fc.setDialogTitle("Select a file.");

                if (fc.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {
                    final String path = fc.getSelectedFile().getAbsolutePath();
                    script = new Script(path);
                    filePathLbl.setText(path);
                    editBtn.setEnabled(true);
                }
            }
        });

        if (script != null) {
            filePathLbl.setText(script.getPath());
        }

        nameTf.setText(name != null ? name : "");

        enabledCb.setSelected(enabled);

        JPanel panel = new JPanel();
        GroupLayout layout = new GroupLayout(panel);
        panel.setLayout(layout);

        layout.setAutoCreateContainerGaps(true);
        layout.setAutoCreateGaps(true);

        GroupLayout.ParallelGroup hGroup = layout.createParallelGroup(GroupLayout.Alignment.LEADING);
        hGroup.addGroup(layout.createSequentialGroup().
                addComponent(nameLbl).
                addComponent(nameTf, 0, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE));
        hGroup.addGroup(layout.createSequentialGroup().
                addComponent(fileLbl).
                addComponent(filePathLbl, 0, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE).
                addComponent(browseBtn).
                addComponent(editBtn).
                addComponent(newBtn));
        hGroup.addGroup(layout.createSequentialGroup().
                addComponent(enabledCb, 0, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE));
        layout.setHorizontalGroup(hGroup);

        GroupLayout.SequentialGroup vGroup = layout.createSequentialGroup();
        vGroup.addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE).addComponent(nameLbl).
                addComponent(nameTf));
        vGroup.addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE).addComponent(fileLbl).
                addComponent(filePathLbl).addComponent(browseBtn).addComponent(editBtn).addComponent(newBtn));
        vGroup.addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE).addComponent(enabledCb));
        layout.setVerticalGroup(vGroup);

        return panel;
    }

    @Override
    public void load(FullBufferedReader fr) throws IOException {
        super.load(fr);

        int tmp = fr.readIntLine();
        if (tmp != 0) {
            String path = fr.readLine();
            script = new Script(path);
            script.load(fr);
        }
    }

    @Override
    public void save(FullBufferedWriter fw) throws IOException {
        super.save(fw);

        if (script == null) {
            fw.writeIntLine(0);
        } else {
            fw.writeIntLine(1);
            script.save(fw);
        }
    }
    //endregion
}

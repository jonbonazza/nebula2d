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

import com.badlogic.gdx.Gdx;
import com.nebula2d.editor.framework.assets.MusicTrack;
import com.nebula2d.editor.ui.ComponentsDialog;
import com.nebula2d.editor.util.FullBufferedReader;
import com.nebula2d.editor.util.FullBufferedWriter;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

public class MusicSource extends Component {

    private MusicTrack musicTrack;

    public MusicSource(String name) {
        super(name);
        componentType = ComponentType.MUSIC;
    }

    @Override
    public JPanel forgeComponentContentPanel(ComponentsDialog parent) {

        final JLabel nameLbl = new JLabel("Name:");
        final JLabel fileLbl = new JLabel("File:");
        final JLabel filePathLbl = new JLabel("");
        final JTextField nameTf = new JTextField(name, 20);
        final JCheckBox enabledCb = new JCheckBox("Enabled", enabled);
        final JCheckBox loopCb = new JCheckBox("Loop", musicTrack != null && musicTrack.isLooping());
        final JButton browseBtn = new JButton("...");
        final JButton mediaBtn = new JButton("Play");
        mediaBtn.setEnabled(musicTrack != null);
        if (musicTrack != null) {
            filePathLbl.setText(musicTrack.getPath());
        }
        browseBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                final JFileChooser fc = new JFileChooser();
                fc.setDialogTitle("Select a file.");

                if (fc.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {
                    final String path = fc.getSelectedFile().getAbsolutePath();
                    filePathLbl.setText(path);
                    Gdx.app.postRunnable(new Runnable() {
                        @Override
                        public void run() {
                            MusicSource.this.musicTrack = new MusicTrack(path);
                        }
                    });
                    mediaBtn.setEnabled(true);
                }
            }
        });
        final com.badlogic.gdx.audio.Music.OnCompletionListener onMusicCompleteListener =
                new com.badlogic.gdx.audio.Music.OnCompletionListener() {
            @Override
            public void onCompletion(com.badlogic.gdx.audio.Music music) {
                mediaBtn.setText("Play");
            }
        };

        enabledCb.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                enabled = enabledCb.isSelected();
            }
        });
        loopCb.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                musicTrack.setLoop(loopCb.isSelected());
            }
        });
        mediaBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JButton btn = (JButton) e.getSource();
                if (btn.getText().equals("Play")) {
                    btn.setText("Stop");
                    Gdx.app.postRunnable(new Runnable() {
                        @Override
                        public void run() {
                            musicTrack.play();
                            musicTrack.setOnCompleteListener(onMusicCompleteListener);
                        }
                    });
                } else {
                    btn.setText("Play");
                    Gdx.app.postRunnable(new Runnable() {
                        @Override
                        public void run() {
                            musicTrack.stop();
                        }
                    });
                }
            }
        });

        JPanel panel = new JPanel();
        GroupLayout layout = new GroupLayout(panel);
        panel.setLayout(layout);

        layout.setAutoCreateContainerGaps(true);
        layout.setAutoCreateGaps(true);

        GroupLayout.SequentialGroup hGroup = layout.createSequentialGroup();
        hGroup.addGroup(layout.createParallelGroup().addComponent(nameLbl).
                addComponent(fileLbl).addComponent(enabledCb).addComponent(loopCb));
        hGroup.addGroup(layout.createParallelGroup().addComponent(nameTf).addComponent(filePathLbl));//.
        hGroup.addGroup(layout.createParallelGroup().addComponent(browseBtn));
        hGroup.addGroup(layout.createParallelGroup().addComponent(mediaBtn));
        layout.setHorizontalGroup(hGroup);

        GroupLayout.SequentialGroup vGroup = layout.createSequentialGroup();
        vGroup.addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE).addComponent(nameLbl).
                addComponent(nameTf));//.addComponent(animationPanel));
        vGroup.addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE).addComponent(fileLbl).
                addComponent(filePathLbl).addComponent(browseBtn).addComponent(mediaBtn));
        vGroup.addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE).addComponent(enabledCb).
                addComponent(loopCb));
        layout.setVerticalGroup(vGroup);

        return panel;
    }

    @Override
    public void load(FullBufferedReader fr) throws IOException {
        super.load(fr);

        int tmp = fr.readIntLine();
        if (tmp != 0) {
            String path = fr.readLine();
            musicTrack = new MusicTrack(path);
            musicTrack.load(fr);
        }
    }

    @Override
    public void save(FullBufferedWriter fw) throws IOException {
        super.save(fw);

        if (musicTrack == null) {
            fw.writeIntLine(0);
        } else {
            fw.writeIntLine(1);
            musicTrack.save(fw);
        }
    }
}

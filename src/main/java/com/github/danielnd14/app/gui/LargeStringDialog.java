package com.github.danielnd14.app.gui;

import javax.swing.*;
import java.awt.*;

public class LargeStringDialog extends JDialog {

	public LargeStringDialog(JFrame parent, String title, String largeString) {
		super(parent, title, true);
		SwingUtilities.invokeLater(() -> initComponents(largeString, parent));
	}

	private void initComponents(String largeString, JFrame parent) {
		// Variables declaration - do not modify
		javax.swing.JScrollPane jScrollPane1 = new javax.swing.JScrollPane();
		javax.swing.JTextArea jTextArea1 = new javax.swing.JTextArea();
		setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
		jTextArea1.setColumns(20);
		jTextArea1.setLineWrap(true);
		jTextArea1.setRows(5);
		jTextArea1.setText(largeString);
		jTextArea1.setWrapStyleWord(true);
		jScrollPane1.setViewportView(jTextArea1);

		javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
		getContentPane().setLayout(layout);
		layout.setHorizontalGroup(
				layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
						.addGroup(layout.createSequentialGroup()
								.addContainerGap()
								.addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 519, Short.MAX_VALUE)
								.addContainerGap())
		);
		layout.setVerticalGroup(
				layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
						.addGroup(layout.createSequentialGroup()
								.addContainerGap()
								.addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 406, Short.MAX_VALUE)
								.addContainerGap())
		);
		setMinimumSize(new Dimension(600, parent.getHeight()));
		setLocationRelativeTo(parent);
		jTextArea1.setEnabled(false);
		setVisible(true);
	}
}
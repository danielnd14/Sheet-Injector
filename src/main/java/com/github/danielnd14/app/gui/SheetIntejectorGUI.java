package com.github.danielnd14.app.gui;

import com.github.danielnd14.app.cronometro.BasicUnitTimer;
import com.github.danielnd14.app.cronometro.UnitTimerImpl;
import com.github.danielnd14.app.processing.Injector;
import com.github.danielnd14.app.processing.RecursiveLister;
import com.github.danielnd14.app.threadservice.PoolService;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.ExecutorService;
import java.util.stream.Collectors;

import static java.util.stream.IntStream.range;

public final class SheetIntejectorGUI extends JFrame {
	private static JFrame instance;
	private final Color redAccentColor = new Color(138, 35, 35);
	private final Color greenAccentColor = new Color(34, 114, 0);
	private BasicUnitTimer chronometer;
	private JTextField fieldSheet;
	private JTable table;
	private JProgressBar bar;
	private JButton buttonStart;
	private JButton buttonLess;
	private JButton buttonPlus;
	private ExecutorService pool;

	private SheetIntejectorGUI() {
		super("Sheet Injector");
		if (instance != null)
			throw new RuntimeException("Singleton must be a singleton {" + this.getClass() + "}");
		initComponents();
	}

	public synchronized static JFrame getInstance() {
		if (instance == null) instance = new SheetIntejectorGUI();
		return instance;
	}

	private static void showListMessages(List<Message> listErrorFiles, String title) {
		var dialog = new JDialog(instance, true);
		dialog.setTitle(title);
		var t = new JTable();
		var s = new JScrollPane();
		s.setViewportView(t);
		var model = new DefaultTableModel(
				new Object[][]{},
				new String[]{
						"MENSAGEM", "ITEM"
				}
		) {
			final Class<?>[] types = new Class[]{
					String.class, String.class
			};
			final boolean[] canEdit = new boolean[]{
					false, false
			};

			public Class<?> getColumnClass(int columnIndex) {
				return types[columnIndex];
			}

			public boolean isCellEditable(int rowIndex, int columnIndex) {
				return canEdit[columnIndex];
			}
		};
		listErrorFiles.forEach(message -> model.addRow(message.toArray()));
		t.setModel(model);
		dialog.add(s);
		dialog.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		dialog.pack();
		dialog.setSize(instance.getWidth() * 2, instance.getHeight());
		dialog.setLocationRelativeTo(instance);
		dialog.setVisible(true);
	}

	@Override
	public void setTitle(String title) {
		super.setTitle("Sheet-Injector - " + title);
	}

	private static DefaultTableModel getDataModel() {
		return new DefaultTableModel(
				new Object[][]{
						{null, null}
				},
				new String[]{
						"COLUNA", "FORMULA"
				}
		) {
			final Class<?>[] types = new Class[]{
					String.class, String.class
			};

			public Class<?> getColumnClass(int columnIndex) {
				return types[columnIndex];
			}
		};
	}

	private void actionMAIS() {
		var model = (DefaultTableModel) table.getModel();
		model.addRow(new Object[]{"", ""});
	}

	private void actionMENOS() {
		final var model = (DefaultTableModel) table.getModel();
		final var idxLastRow = model.getRowCount() - 1;
		if (idxLastRow > 0) model.removeRow(idxLastRow);
	}

	private void initComponents() {
		pool = PoolService.getInstance();
		fieldSheet = new JTextField();
		JLabel labelChrono = new JLabel("00:00:00");
		chronometer = new UnitTimerImpl(labelChrono, redAccentColor, greenAccentColor);
		JScrollPane jScrollPane1 = new JScrollPane();
		table = new JTable();
		buttonPlus = new JButton();
		buttonStart = new JButton();
		buttonLess = new JButton();
		buttonPlus.setText("+");
		buttonStart.setText("START");
		buttonLess.setText("-");
		fieldSheet.setText("Digite o nome da aba");
		table.setModel(getDataModel());
		table.getColumnModel().getColumn(0).setMinWidth(100);
		table.getColumnModel().getColumn(1).setMinWidth(500);
		bar = new JProgressBar();
		jScrollPane1.setViewportView(table);
		javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
		getContentPane().setLayout(layout);
		layout.setHorizontalGroup(
				layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
						.addGroup(layout.createSequentialGroup()
								.addContainerGap()
								.addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
										.addComponent(bar, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
										.addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 376, Short.MAX_VALUE)
										.addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
												.addComponent(buttonLess)
												.addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
												.addComponent(buttonPlus)
												.addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
												.addComponent(labelChrono)
												.addGap(18, 18, 18)
												.addComponent(buttonStart))
										.addComponent(fieldSheet))
								.addContainerGap())
		);
		layout.setVerticalGroup(
				layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
						.addGroup(layout.createSequentialGroup()
								.addContainerGap()
								.addComponent(fieldSheet, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
								.addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
								.addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 364, Short.MAX_VALUE)
								.addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
								.addComponent(bar, javax.swing.GroupLayout.PREFERRED_SIZE, 8, javax.swing.GroupLayout.PREFERRED_SIZE)
								.addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
								.addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
										.addComponent(buttonStart)
										.addComponent(buttonPlus)
										.addComponent(buttonLess)
										.addComponent(labelChrono))
								.addContainerGap())
		);

		pack();
		this.setSize(625, this.getHeight());
		this.setDefaultCloseOperation(EXIT_ON_CLOSE);
		this.setLocationRelativeTo(null);
		this.setResizable(false);

		buttonPlus.addActionListener(e -> actionMAIS());
		buttonLess.addActionListener(e -> actionMENOS());
		buttonStart.addActionListener(e -> actionSTART());
		fieldSheet.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				super.mouseClicked(e);
				if (fieldSheet.getText().equalsIgnoreCase("Digite o nome da aba"))
					SwingUtilities.invokeLater(() -> fieldSheet.setText(""));
			}
		});
		fieldSheet.addFocusListener(new FocusAdapter() {
			@Override
			public void focusLost(FocusEvent e) {
				super.focusLost(e);
				if (fieldSheet.getText().isBlank())
					SwingUtilities.invokeLater(() -> fieldSheet.setText("Digite o nome da aba"));
			}
		});
	}// </editor-fold>

	private void actionSTART() {
		UIManager.getUI(table);
		var model = table.getModel();
		var rowCount = model.getRowCount();
		var tupleList = range(0, rowCount)
				.mapToObj(row -> new TableTuple(model.getValueAt(row, 0) + "", model.getValueAt(row, 1) + ""))
				.filter(TableTuple::isValid).sorted()
				.collect(Collectors.toUnmodifiableList());

		var sheetName = fieldSheet.getText().trim();
		if (tupleList.isEmpty()) {
			JOptionPane.showMessageDialog(instance, "Não há formulas ou colunas válidas");
			return;
		}
		if (sheetName.isBlank() || sheetName.equalsIgnoreCase("Digite o nome da aba")) {
			JOptionPane.showMessageDialog(instance, "Digite o nome da aba");
			return;
		}

		var sheetFiles = new RecursiveLister().list();
		if (sheetFiles.isEmpty()) {
			return;
		}
		new ConfirmPreInject(sheetFiles, sheetName, tupleList);
	}

	private void lockButtons() {
		this.buttonLess.setEnabled(false);
		this.buttonPlus.setEnabled(false);
		this.buttonStart.setEnabled(false);
		this.fieldSheet.setEnabled(false);
		this.table.setEnabled(false);
	}

	private void unlockButtons() {
		this.buttonLess.setEnabled(true);
		this.buttonPlus.setEnabled(true);
		this.buttonStart.setEnabled(true);
		this.fieldSheet.setEnabled(true);
		this.table.setEnabled(true);
	}

	private void initInject(List<Path> sheetsToInject, String sheetName, List<TableTuple> tupleList) {
		pool.submit(() -> {
			lockButtons();
			var listErrorFiles = new ArrayList<Message>();
			bar.setMinimum(0);
			bar.setMaximum(sheetsToInject.size());
			bar.setValue(0);
			chronometer.start();
			sheetsToInject.forEach(sheet -> {
				try {
					instance.setTitle(sheet.getFileName().toString());
					Injector.inject(sheetName, tupleList, sheet.toFile());
				} catch (Exception e) {
					e.printStackTrace();
					listErrorFiles.add(new Message(e.getMessage(), sheet.toAbsolutePath().toString()));
				} finally {
					bar.setValue(bar.getValue() + 1);
					System.gc();
				}
			});
			chronometer.stop();
			var title = "Pronto!";
			JOptionPane.showMessageDialog(instance, title);
			instance.setTitle(title);
			if (!listErrorFiles.isEmpty())
				showListMessages(listErrorFiles, "Não consegui injetar nos " + listErrorFiles.size() + " arquivos abaixo");
			unlockButtons();
		});
	}

	private class ConfirmPreInject extends JDialog {
		private final List<Path> sheets;
		private final String nameTab;
		private final List<TableTuple> tupleList;
		private boolean valueBtnUnmark = false;

		private ConfirmPreInject(List<Path> sheets, String nameTab, List<TableTuple> tupleList) {
			super(instance, true);
			this.nameTab = nameTab;
			this.tupleList = tupleList;
			var title = sheets.size() > 1 ? "Eu encontrei os " + sheets.size() + " arquivos listados abaixo, quer que eu injete em todos?" :
					"Eu econtrei o seguinte arquivo exibido abaixo, quer que eu injete nele?";
			setTitle(title);
			this.sheets = sheets;
			SwingUtilities.invokeLater(this::initComponents);
		}

		private void initComponents() {

			var scrollPane = new javax.swing.JScrollPane();
			var confirmTable = new javax.swing.JTable();
			var buttonInject = new javax.swing.JButton();
			var buttonUnmark = new javax.swing.JButton();
			var buttonInvert = new javax.swing.JButton();
			setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
			var model = new javax.swing.table.DefaultTableModel(
					new Object[][]{
					},
					new String[]{
							"Injetar?", "Arquivo"
					}
			) {
				final Class<?>[] types = new Class[]{
						Boolean.class, Path.class
				};

				public Class<?> getColumnClass(int columnIndex) {
					return types[columnIndex];
				}
			};
			sheets.forEach(path -> model.addRow(new Object[]{true, path}));
			confirmTable.setModel(model);
			scrollPane.setViewportView(confirmTable);
			buttonInject.setText("Injetar");
			buttonUnmark.setText("Desmarcar todos");
			buttonInvert.setText("Inverter seleção");

			javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
			getContentPane().setLayout(layout);
			layout.setHorizontalGroup(
					layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
							.addGroup(layout.createSequentialGroup()
									.addContainerGap()
									.addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
											.addComponent(scrollPane, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
											.addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
													.addComponent(buttonUnmark)
													.addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
													.addComponent(buttonInvert)
													.addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 12, Short.MAX_VALUE)
													.addComponent(buttonInject)))
									.addContainerGap())
			);
			layout.setVerticalGroup(
					layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
							.addGroup(layout.createSequentialGroup()
									.addContainerGap()
									.addComponent(scrollPane, javax.swing.GroupLayout.DEFAULT_SIZE, 269, Short.MAX_VALUE)
									.addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
									.addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
											.addComponent(buttonInject)
											.addComponent(buttonUnmark)
											.addComponent(buttonInvert))
									.addContainerGap())
			);

			pack();
			buttonInject.addActionListener(e -> {
				final var data = confirmTable.getModel();
				final var rowCount = data.getRowCount();
				final var sheetsToInject = range(0, rowCount).mapToObj(i -> {
					if ((boolean) data.getValueAt(i, 0))
						return (Path) data.getValueAt(i, 1);
					return null;
				}).filter(Objects::nonNull).sorted(Comparator.comparingLong(o -> o.toFile().length()))
						.collect(Collectors.toUnmodifiableList());
				if (!sheetsToInject.isEmpty())
					initInject(sheetsToInject, nameTab, tupleList);
				this.dispose();
			});
			buttonInvert.addActionListener(e -> {
				var data = confirmTable.getModel();
				var rowCount = data.getRowCount();
				SwingUtilities.invokeLater(() -> range(0, rowCount)
						.forEach(i -> data.setValueAt(!(boolean) data.getValueAt(i, 0), i, 0)));
			});
			buttonUnmark.addActionListener(e -> {
				var data = confirmTable.getModel();
				var rowCount = data.getRowCount();
				SwingUtilities.invokeLater(() -> {
					range(0, rowCount)
							.forEach(i -> data.setValueAt(valueBtnUnmark, i, 0));
					valueBtnUnmark = !valueBtnUnmark;
					buttonUnmark.setText(valueBtnUnmark ? "Marcar todos" : "Desmarcar todos");
				});
			});
			setSize(instance.getWidth() * 2, instance.getHeight());
			setLocationRelativeTo(instance);
			confirmTable.getColumnModel().getColumn(0).setMaxWidth(80);
			setResizable(false);
			setVisible(true);
		}
	}
}

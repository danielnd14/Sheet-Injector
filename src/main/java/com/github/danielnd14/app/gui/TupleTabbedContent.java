package com.github.danielnd14.app.gui;

import com.github.danielnd14.app.dto.TableDTO;
import com.github.danielnd14.app.repository.ColorRepository;
import com.github.danielnd14.app.repository.ValidationRepository;
import com.github.danielnd14.app.validation.ValidatorByRegex;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import static java.util.stream.IntStream.range;

public final class TupleTabbedContent extends JPanel implements AutoCloseable, TableDTOFont {
	private JTabbedPane parent;
	private JTable table;
	private final int myIDX;
	private JTextField fieldSheet;

	public TupleTabbedContent(final TupleTabbedPane parent) {
		Objects.requireNonNull(parent);
		this.parent = parent;
		myIDX = parent.getNextIDX();
		SwingUtilities.invokeLater(this::initComponents);
	}

	@Override
	public void setEnabled(boolean enabled) {
		super.setEnabled(enabled);
		this.fieldSheet.setEnabled(enabled);
		this.table.setEnabled(enabled);
	}

	protected String getSheetTabName() {
		return fieldSheet.getText();
	}

	private void initComponents() {
		fieldSheet = new JTextField("Digite o nome da aba");
		final var jScrollPane1 = new JScrollPane();
		final var buttonLess = new JButton("-");
		final var buttonPlus = new JButton("+");
		final var font = new Font("Fira Sans", Font.BOLD, 15);
		table = new JTable();
		table.setFont(font);
		fieldSheet.setFont(font);
		table.setModel(new javax.swing.table.DefaultTableModel(
				new Object[][]{
						{"", "", "ALL-[0]"}
				},
				new String[]{
						"COLUMN", "FORMULA", "ROW"
				}
		) {
			final Class<?>[] types = new Class<?>[]{
					java.lang.String.class, java.lang.String.class, java.lang.String.class
			};

			public Class<?> getColumnClass(int columnIndex) {
				return types[columnIndex];
			}
		});
		jScrollPane1.setViewportView(table);
		javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(this);
		setLayout(jPanel1Layout);
		jPanel1Layout.setHorizontalGroup(
				jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
						.addGroup(jPanel1Layout.createSequentialGroup()
								.addContainerGap()
								.addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
										.addGroup(jPanel1Layout.createSequentialGroup()
												.addComponent(buttonLess)
												.addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
												.addComponent(buttonPlus)
												.addGap(0, 0, Short.MAX_VALUE))
										.addComponent(fieldSheet, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
										.addComponent(jScrollPane1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE))
								.addContainerGap())
		);
		jPanel1Layout.setVerticalGroup(
				jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
						.addGroup(jPanel1Layout.createSequentialGroup()
								.addContainerGap()
								.addComponent(fieldSheet, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
								.addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
								.addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 268, Short.MAX_VALUE)
								.addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
								.addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
										.addComponent(buttonLess)
										.addComponent(buttonPlus))
								.addContainerGap())
		);
		//listeners
		buttonPlus.addActionListener(e -> SwingUtilities.invokeLater(() -> {
			if (isEnabled()) {
				var model = (DefaultTableModel) table.getModel();
				model.addRow(new Object[]{"", "", "ALL-[0]"});
			}
		}));
		buttonLess.addActionListener(e -> SwingUtilities.invokeLater(() -> {
			if (isEnabled()) {
				final var model = (DefaultTableModel) table.getModel();
				final var idxLastRow = model.getRowCount() - 1;
				if (idxLastRow > 0) model.removeRow(idxLastRow);
			}
		}));
		var colorRepository = ColorRepository.instance();
		fieldSheet.setForeground(colorRepository.getRedAccent());
		fieldSheet.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				super.mouseClicked(e);
				if (fieldSheet.getText().trim().equalsIgnoreCase("Digite o nome da aba"))
					SwingUtilities.invokeLater(() -> fieldSheet.setText(""));
			}
		});
		fieldSheet.addFocusListener(new FocusAdapter() {
			@Override
			public void focusLost(FocusEvent e) {
				super.focusLost(e);
				var sheetName = fieldSheet.getText().trim();
				if (sheetName.isBlank()) {
					SwingUtilities.invokeLater(() -> {
						fieldSheet.setText("Digite o nome da aba");
						parent.setTitleAt(myIDX, "Sem nome");
						fieldSheet.setForeground(colorRepository.getRedAccent());
					});
				} else {
					SwingUtilities.invokeLater(() -> {
						parent.setTitleAt(myIDX, sheetName);
						fieldSheet.setForeground(colorRepository.getPurpleAccent());
					});
				}
			}
		});
		//table-render
		table.getColumnModel().getColumn(0).setCellRenderer(new CellRender(ValidationRepository.instance().getColumnValidator()));
		table.getColumnModel().getColumn(1).setCellRenderer(new CellRender());
		table.getColumnModel().getColumn(2).setCellRenderer(new CellRender(ValidationRepository.instance().getRowValidator()));
		table.getColumnModel().getColumn(0).setMinWidth(150);
		table.getColumnModel().getColumn(1).setMinWidth(500);
		table.getColumnModel().getColumn(2).setMinWidth(150);
		setMinimumSize(new Dimension(600, 450));
	}

	@Override
	public void close() {
		parent = null;
	}

	@Override
	public List<TableDTO> listTuple() {
		var model = table.getModel();
		var rowCount = model.getRowCount();
		return range(0, rowCount).mapToObj(row -> {
			var col = model.getValueAt(row, 0).toString().replaceAll("\\s+", "");
			var formula = model.getValueAt(row, 1).toString().trim();
			var rows = model.getValueAt(row, 2).toString().replaceAll("\\s+", "");
			return new TableDTO(col, formula, rows);
		}).filter(TableDTO::isValid).collect(Collectors.toUnmodifiableList());
	}

	private static class CellRender extends DefaultTableCellRenderer {
		private final ValidatorByRegex validator;
		private final ColorRepository colors;

		public CellRender(ValidatorByRegex validator) {
			this.validator = validator;
			colors = ColorRepository.instance();
		}

		public CellRender() {
			validator = null;
			this.colors = ColorRepository.instance();
		}

		@Override
		public Component getTableCellRendererComponent(JTable table, Object value,
		                                               boolean isSelected, boolean hasFocus, int row, int column) {
			final var render = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
			final var rowsInfo = (String) value;
			if (validator != null) {
				if (validator.valid(rowsInfo.replaceAll("\\s+", "")))
					render.setForeground(colors.getGreenAccent());
				else render.setForeground(colors.getRedAccent());
			} else render.setForeground(colors.getPurpleAccent());
			return render;
		}
	}
}

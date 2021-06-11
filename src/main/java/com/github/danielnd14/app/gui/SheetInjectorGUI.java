package com.github.danielnd14.app.gui;

import com.github.danielnd14.app.dto.TabbedDTO;
import com.github.danielnd14.app.dto.TupleMessageDTO;
import com.github.danielnd14.app.gui.cronometro.UnitTimerImpl;
import com.github.danielnd14.app.processing.Injector;
import com.github.danielnd14.app.processing.RecursiveLister;
import com.github.danielnd14.app.repository.ColorRepository;
import com.github.danielnd14.app.threadservice.PoolService;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.io.File;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static java.util.stream.IntStream.range;

public final class SheetInjectorGUI extends JFrame {
	private static JFrame instance;
	private JProgressBar bar;
	private TupleTabbedPane tabbedPane;
	private UnitTimerImpl chronometer;
	private JButton buttonStart;
	private JButton buttonTabLess;
	private JButton buttonTabPlus;

	private SheetInjectorGUI() {
		super("Sheet-Injector");
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		initComponents();
	}

	public synchronized static JFrame instance() {
		if (instance == null) instance = new SheetInjectorGUI();
		return instance;
	}

	private static void showListMessages(List<TupleMessageDTO> listErrorFiles, String title) {
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
		listErrorFiles.forEach(tupleMessageDTO -> model.addRow(tupleMessageDTO.toArray()));
		t.setModel(model);
		dialog.add(s);
		dialog.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		dialog.pack();
		dialog.setSize(instance.getWidth(), instance.getHeight());
		dialog.setLocationRelativeTo(instance);
		dialog.setVisible(true);
	}

	private static String getLicenseString() {
		return "                                 Apache License\n" +
				"                           Version 2.0, January 2004\n" +
				"                        http://www.apache.org/licenses/\n" +
				"\n" +
				"   TERMS AND CONDITIONS FOR USE, REPRODUCTION, AND DISTRIBUTION\n" +
				"\n" +
				"   1. Definitions.\n" +
				"\n" +
				"      \"License\" shall mean the terms and conditions for use, reproduction,\n" +
				"      and distribution as defined by Sections 1 through 9 of this document.\n" +
				"\n" +
				"      \"Licensor\" shall mean the copyright owner or entity authorized by\n" +
				"      the copyright owner that is granting the License.\n" +
				"\n" +
				"      \"Legal Entity\" shall mean the union of the acting entity and all\n" +
				"      other entities that control, are controlled by, or are under common\n" +
				"      control with that entity. For the purposes of this definition,\n" +
				"      \"control\" means (i) the power, direct or indirect, to cause the\n" +
				"      direction or management of such entity, whether by contract or\n" +
				"      otherwise, or (ii) ownership of fifty percent (50%) or more of the\n" +
				"      outstanding shares, or (iii) beneficial ownership of such entity.\n" +
				"\n" +
				"      \"You\" (or \"Your\") shall mean an individual or Legal Entity\n" +
				"      exercising permissions granted by this License.\n" +
				"\n" +
				"      \"Source\" form shall mean the preferred form for making modifications,\n" +
				"      including but not limited to software source code, documentation\n" +
				"      source, and configuration files.\n" +
				"\n" +
				"      \"Object\" form shall mean any form resulting from mechanical\n" +
				"      transformation or translation of a Source form, including but\n" +
				"      not limited to compiled object code, generated documentation,\n" +
				"      and conversions to other media types.\n" +
				"\n" +
				"      \"Work\" shall mean the work of authorship, whether in Source or\n" +
				"      Object form, made available under the License, as indicated by a\n" +
				"      copyright notice that is included in or attached to the work\n" +
				"      (an example is provided in the Appendix below).\n" +
				"\n" +
				"      \"Derivative Works\" shall mean any work, whether in Source or Object\n" +
				"      form, that is based on (or derived from) the Work and for which the\n" +
				"      editorial revisions, annotations, elaborations, or other modifications\n" +
				"      represent, as a whole, an original work of authorship. For the purposes\n" +
				"      of this License, Derivative Works shall not include works that remain\n" +
				"      separable from, or merely link (or bind by name) to the interfaces of,\n" +
				"      the Work and Derivative Works thereof.\n" +
				"\n" +
				"      \"Contribution\" shall mean any work of authorship, including\n" +
				"      the original version of the Work and any modifications or additions\n" +
				"      to that Work or Derivative Works thereof, that is intentionally\n" +
				"      submitted to Licensor for inclusion in the Work by the copyright owner\n" +
				"      or by an individual or Legal Entity authorized to submit on behalf of\n" +
				"      the copyright owner. For the purposes of this definition, \"submitted\"\n" +
				"      means any form of electronic, verbal, or written communication sent\n" +
				"      to the Licensor or its representatives, including but not limited to\n" +
				"      communication on electronic mailing lists, source code control systems,\n" +
				"      and issue tracking systems that are managed by, or on behalf of, the\n" +
				"      Licensor for the purpose of discussing and improving the Work, but\n" +
				"      excluding communication that is conspicuously marked or otherwise\n" +
				"      designated in writing by the copyright owner as \"Not a Contribution.\"\n" +
				"\n" +
				"      \"Contributor\" shall mean Licensor and any individual or Legal Entity\n" +
				"      on behalf of whom a Contribution has been received by Licensor and\n" +
				"      subsequently incorporated within the Work.\n" +
				"\n" +
				"   2. Grant of Copyright License. Subject to the terms and conditions of\n" +
				"      this License, each Contributor hereby grants to You a perpetual,\n" +
				"      worldwide, non-exclusive, no-charge, royalty-free, irrevocable\n" +
				"      copyright license to reproduce, prepare Derivative Works of,\n" +
				"      publicly display, publicly perform, sublicense, and distribute the\n" +
				"      Work and such Derivative Works in Source or Object form.\n" +
				"\n" +
				"   3. Grant of Patent License. Subject to the terms and conditions of\n" +
				"      this License, each Contributor hereby grants to You a perpetual,\n" +
				"      worldwide, non-exclusive, no-charge, royalty-free, irrevocable\n" +
				"      (except as stated in this section) patent license to make, have made,\n" +
				"      use, offer to sell, sell, import, and otherwise transfer the Work,\n" +
				"      where such license applies only to those patent claims licensable\n" +
				"      by such Contributor that are necessarily infringed by their\n" +
				"      Contribution(s) alone or by combination of their Contribution(s)\n" +
				"      with the Work to which such Contribution(s) was submitted. If You\n" +
				"      institute patent litigation against any entity (including a\n" +
				"      cross-claim or counterclaim in a lawsuit) alleging that the Work\n" +
				"      or a Contribution incorporated within the Work constitutes direct\n" +
				"      or contributory patent infringement, then any patent licenses\n" +
				"      granted to You under this License for that Work shall terminate\n" +
				"      as of the date such litigation is filed.\n" +
				"\n" +
				"   4. Redistribution. You may reproduce and distribute copies of the\n" +
				"      Work or Derivative Works thereof in any medium, with or without\n" +
				"      modifications, and in Source or Object form, provided that You\n" +
				"      meet the following conditions:\n" +
				"\n" +
				"      (a) You must give any other recipients of the Work or\n" +
				"          Derivative Works a copy of this License; and\n" +
				"\n" +
				"      (b) You must cause any modified files to carry prominent notices\n" +
				"          stating that You changed the files; and\n" +
				"\n" +
				"      (c) You must retain, in the Source form of any Derivative Works\n" +
				"          that You distribute, all copyright, patent, trademark, and\n" +
				"          attribution notices from the Source form of the Work,\n" +
				"          excluding those notices that do not pertain to any part of\n" +
				"          the Derivative Works; and\n" +
				"\n" +
				"      (d) If the Work includes a \"NOTICE\" text file as part of its\n" +
				"          distribution, then any Derivative Works that You distribute must\n" +
				"          include a readable copy of the attribution notices contained\n" +
				"          within such NOTICE file, excluding those notices that do not\n" +
				"          pertain to any part of the Derivative Works, in at least one\n" +
				"          of the following places: within a NOTICE text file distributed\n" +
				"          as part of the Derivative Works; within the Source form or\n" +
				"          documentation, if provided along with the Derivative Works; or,\n" +
				"          within a display generated by the Derivative Works, if and\n" +
				"          wherever such third-party notices normally appear. The contents\n" +
				"          of the NOTICE file are for informational purposes only and\n" +
				"          do not modify the License. You may add Your own attribution\n" +
				"          notices within Derivative Works that You distribute, alongside\n" +
				"          or as an addendum to the NOTICE text from the Work, provided\n" +
				"          that such additional attribution notices cannot be construed\n" +
				"          as modifying the License.\n" +
				"\n" +
				"      You may add Your own copyright statement to Your modifications and\n" +
				"      may provide additional or different license terms and conditions\n" +
				"      for use, reproduction, or distribution of Your modifications, or\n" +
				"      for any such Derivative Works as a whole, provided Your use,\n" +
				"      reproduction, and distribution of the Work otherwise complies with\n" +
				"      the conditions stated in this License.\n" +
				"\n" +
				"   5. Submission of Contributions. Unless You explicitly state otherwise,\n" +
				"      any Contribution intentionally submitted for inclusion in the Work\n" +
				"      by You to the Licensor shall be under the terms and conditions of\n" +
				"      this License, without any additional terms or conditions.\n" +
				"      Notwithstanding the above, nothing herein shall supersede or modify\n" +
				"      the terms of any separate license agreement you may have executed\n" +
				"      with Licensor regarding such Contributions.\n" +
				"\n" +
				"   6. Trademarks. This License does not grant permission to use the trade\n" +
				"      names, trademarks, service marks, or product names of the Licensor,\n" +
				"      except as required for reasonable and customary use in describing the\n" +
				"      origin of the Work and reproducing the content of the NOTICE file.\n" +
				"\n" +
				"   7. Disclaimer of Warranty. Unless required by applicable law or\n" +
				"      agreed to in writing, Licensor provides the Work (and each\n" +
				"      Contributor provides its Contributions) on an \"AS IS\" BASIS,\n" +
				"      WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or\n" +
				"      implied, including, without limitation, any warranties or conditions\n" +
				"      of TITLE, NON-INFRINGEMENT, MERCHANTABILITY, or FITNESS FOR A\n" +
				"      PARTICULAR PURPOSE. You are solely responsible for determining the\n" +
				"      appropriateness of using or redistributing the Work and assume any\n" +
				"      risks associated with Your exercise of permissions under this License.\n" +
				"\n" +
				"   8. Limitation of Liability. In no event and under no legal theory,\n" +
				"      whether in tort (including negligence), contract, or otherwise,\n" +
				"      unless required by applicable law (such as deliberate and grossly\n" +
				"      negligent acts) or agreed to in writing, shall any Contributor be\n" +
				"      liable to You for damages, including any direct, indirect, special,\n" +
				"      incidental, or consequential damages of any character arising as a\n" +
				"      result of this License or out of the use or inability to use the\n" +
				"      Work (including but not limited to damages for loss of goodwill,\n" +
				"      work stoppage, computer failure or malfunction, or any and all\n" +
				"      other commercial damages or losses), even if such Contributor\n" +
				"      has been advised of the possibility of such damages.\n" +
				"\n" +
				"   9. Accepting Warranty or Additional Liability. While redistributing\n" +
				"      the Work or Derivative Works thereof, You may choose to offer,\n" +
				"      and charge a fee for, acceptance of support, warranty, indemnity,\n" +
				"      or other liability obligations and/or rights consistent with this\n" +
				"      License. However, in accepting such obligations, You may act only\n" +
				"      on Your own behalf and on Your sole responsibility, not on behalf\n" +
				"      of any other Contributor, and only if You agree to indemnify,\n" +
				"      defend, and hold each Contributor harmless for any liability\n" +
				"      incurred by, or claims asserted against, such Contributor by reason\n" +
				"      of your accepting any such warranty or additional liability.\n" +
				"\n" +
				"   END OF TERMS AND CONDITIONS\n" +
				"\n" +
				"   APPENDIX: How to apply the Apache License to your work.\n" +
				"\n" +
				"      To apply the Apache License to your work, attach the following\n" +
				"      boilerplate notice, with the fields enclosed by brackets \"[]\"\n" +
				"      replaced with your own identifying information. (Don't include\n" +
				"      the brackets!)  The text should be enclosed in the appropriate\n" +
				"      comment syntax for the file format. We also recommend that a\n" +
				"      file or class name and description of purpose be included on the\n" +
				"      same \"printed page\" as the copyright notice for easier\n" +
				"      identification within third-party archives.\n" +
				"\n" +
				"   Copyright [yyyy] [name of copyright owner]\n" +
				"\n" +
				"   Licensed under the Apache License, Version 2.0 (the \"License\");\n" +
				"   you may not use this file except in compliance with the License.\n" +
				"   You may obtain a copy of the License at\n" +
				"\n" +
				"       http://www.apache.org/licenses/LICENSE-2.0\n" +
				"\n" +
				"   Unless required by applicable law or agreed to in writing, software\n" +
				"   distributed under the License is distributed on an \"AS IS\" BASIS,\n" +
				"   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.\n" +
				"   See the License for the specific language governing permissions and\n" +
				"   limitations under the License.\n";
	}

	private static String getHelpString() {
		return "Instruções gerais:\n\n" +
				"1) Linhas das tabelas de injeção de dados/formulas que conterem alguma celula em branco ou com valores na cor vermelha serão ignoradas, fique atento.\n\n" +
				"2) Indices de colunas e linhas começam de 0.\n\n" +
				"3) Das opções gerais de injeção tempos:\n\n" +
				"3.1) É possível informar um intervalo para as colunas. Exemplo; imagine que sua intenção é injetar uma determinada formula da coluna 0 até coluna 25, basta informar o valor 0:25, na coluna de titulo \"COLUMN\". " +
				"É possível também informar uma lista de colunas a sintaxe é assim: [0,1,5,78], o programa entenderá que deverá trabalhar nas colunas 0, 1, 5 e 78.\n\n" +
				"3.2) Assim como na coluna de título \"COLUMN\", a coluna de titulo \"ROW\" também pode receber intervalos, porém ela é um pouco mais versátil, pois além dos intervalos citados no item 3.1, temos também a possibilidade de usar a palavra \"ALL\" para dizer que queremos em todas as linhas, ou usar \"ALL-[0]\" ou \"ALL-[0,1,2]\" ou ainda \"ALL-[10:34]\", para injetar a formula em todas as linhas exceto na primeira, ou exceto nas 3 primeiras ou em todas menos no intervalao 10:34.\n\n" +
				"4) Das fórmulas temos:\n\n" +
				"4.1) Formulas devem ser inseridas conforme a sintaxe e padrões dos estados unidos.\n\n" +
				"4.2) O símbolo '#' serve para dizer ao programa que a formula deve usar o valor ordinal da linha, exemplo: =(DR#*AB#)+A2, nessa formula as colunas DR e AB acompanharão suas respectivas linhas, pois o símbolo '#' será substituido pelo indice da linha atual onde a formula está sendo aplicada. Logo quando o programa aplicar a formula na linha 5 ele irá colocar a seguinte fórmula =(DR5*AB5)+A2\n\n" +
				"5) Das cores temos:\n\n" +
				"5.1) Roxo, cor para indicar que o programa não sabe avaliar de imediato se aquela informação é válida, ou seja, ele confia em você.\n\n" +
				"5.2) Vermelho, cor de reprovação, essa cor indica que o programa está entendendo que a informação inserida pelo usuário é inválida.\n\n" +
				"5.3) Verde, cor de aprovação, essa cor indica que o programa avaliou a informação inserida e entendeu ela como válida.\n\n" +
				"6) Cronômetro, as cores não têm o mesmo significado para ele, no cronômetro, laranja significa que ele está correndo, e verde que ele está parado.";
	}

	private void actionStart() {
		final var forms = tabbedPane.tabbedDTOList();
		if (forms.isEmpty()) return;
		final var sheetFiles = new RecursiveLister().list();
		if (sheetFiles.isEmpty()) {
			JOptionPane.showMessageDialog(instance, "Não encontrei nenhum arquivo");
			return;
		}
		new ConfirmPreInject(sheetFiles, forms);
	}

	private void initComponents() {
		bar = new JProgressBar();
		var font = new java.awt.Font("Fira Sans", Font.BOLD, 15);
		buttonStart = new JButton("START");
		buttonTabLess = new JButton("TAB-");
		buttonTabPlus = new JButton("TAB+");
		buttonStart.setFont(font);
		buttonStart.setForeground(ColorRepository.instance().getOrangeAccent());
		tabbedPane = new TupleTabbedPane();
		JLabel labelChrono = new JLabel("00:00:00");
		labelChrono.setFont(new java.awt.Font("Fira Sans", Font.BOLD, 15)); // NOI18N
		var colorRepo = ColorRepository.instance();
		chronometer = new UnitTimerImpl(labelChrono, colorRepo.getOrangeAccent(), colorRepo.getGreenAccent());
		var menuBar = new javax.swing.JMenuBar();
		var AboutMenu = new javax.swing.JMenu();
		var menuHelp = new javax.swing.JMenuItem();
		var menuLicence = new javax.swing.JMenuItem();
		AboutMenu.setText("About");
		menuHelp.setText("Help");
		menuHelp.addActionListener(e -> new LargeStringDialog(instance(), "Help?", getHelpString()));
		AboutMenu.add(menuHelp);
		menuLicence.setText("License");
		menuLicence.addActionListener(e -> new LargeStringDialog(instance(), "License", getLicenseString()));
		AboutMenu.add(menuLicence);
		menuBar.add(AboutMenu);
		setJMenuBar(menuBar);

		javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
		getContentPane().setLayout(layout);
		layout.setHorizontalGroup(
				layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
						.addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
								.addContainerGap()
								.addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
										.addComponent(tabbedPane)
										.addGroup(javax.swing.GroupLayout.Alignment.LEADING, layout.createSequentialGroup()
												.addComponent(labelChrono)
												.addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
												.addComponent(buttonTabLess)
												.addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
												.addComponent(buttonTabPlus)
												.addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
												.addComponent(buttonStart))
										.addComponent(bar, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
								.addContainerGap())
		);
		layout.setVerticalGroup(
				layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
						.addGroup(layout.createSequentialGroup()
								.addContainerGap()
								.addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
										.addComponent(buttonTabPlus)
										.addComponent(buttonTabLess)
										.addComponent(buttonStart)
										.addComponent(labelChrono, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
								.addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
								.addComponent(tabbedPane, javax.swing.GroupLayout.PREFERRED_SIZE, 356, javax.swing.GroupLayout.PREFERRED_SIZE)
								.addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
								.addComponent(bar, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
								.addContainerGap())
		);

		tabbedPane.addTab("Sem nome", new TupleTabbedContent(tabbedPane));

		//listener
		buttonTabPlus.addActionListener(e -> tabbedPane.addTab("Sem nome", new TupleTabbedContent(tabbedPane)));
		buttonTabLess.addActionListener(e -> {
			var idx = tabbedPane.getSelectedIndex();
			if (idx < 0) return;
			var tabContent = (TupleTabbedContent) tabbedPane.getComponentAt(idx);
			tabbedPane.remove(tabContent);
		});
		buttonStart.addActionListener(e -> actionStart());

		setMinimumSize(new Dimension(1000, 500));
		setResizable(false);
		pack();
		setLocationRelativeTo(null);
	}

	private void setEnabledAll(final boolean enabled) {
		SwingUtilities.invokeLater(() -> {
			var tabCount = tabbedPane.getTabCount();
			IntStream.range(0, tabCount).forEach(i -> tabbedPane.getComponentAt(i).setEnabled(enabled));
		});
		SwingUtilities.invokeLater(() -> {
			buttonStart.setEnabled(enabled);
			buttonTabPlus.setEnabled(enabled);
			buttonTabLess.setEnabled(enabled);
		});
	}

	private void lock() {
		setEnabledAll(false);
	}

	private void unlock() {
		setEnabledAll(true);

	}

	private void initInject(List<TabbedDTO> forms, List<Path> sheetFiles) {
		bar.setMinimum(0);
		bar.setValue(0);
		bar.setMaximum(sheetFiles.size());
		PoolService.instance().submit(() -> {
			var problems = new ArrayList<TupleMessageDTO>();
			chronometer.start();
			lock();
			sheetFiles.forEach(path -> {
				try {
					var title = "..." + File.separator +
							path.getParent().getFileName() +
							File.separator + path.getFileName();

					instance.setTitle(title);
					Injector.inject(forms, path.toFile());
				} catch (Exception e) {
					problems.add(new TupleMessageDTO(e.getMessage(), path.toString()));
					e.printStackTrace();
				} finally {
					bar.setValue(bar.getValue() + 1);
					System.gc();
				}
			});
			unlock();
			chronometer.stop();
			if (!problems.isEmpty())
				showListMessages(problems, "Não consegui injetar nos " + problems.size() + " arquivos abaixo");
			else
				JOptionPane.showMessageDialog(instance, "Pronto!");

			setTitle("Pronto!");
		});
	}

	@Override
	public void setTitle(String title) {
		super.setTitle("Sheet-Injector -> " + title);
	}

	private class ConfirmPreInject extends JDialog {
		private final List<Path> sheets;
		private final List<TabbedDTO> tupleList;
		private boolean valueBtnUnmark = false;

		private ConfirmPreInject(List<Path> sheets, List<TabbedDTO> tupleList) {
			super(instance, true);
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
					initInject(tupleList, sheetsToInject);
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

			setSize(instance.getWidth(), instance.getHeight());
			setLocationRelativeTo(instance);
			confirmTable.getColumnModel().getColumn(0).setMaxWidth(80);
			setResizable(false);
			setVisible(true);
		}
	}
}
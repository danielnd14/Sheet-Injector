package com.github.danielnd14.app.gui;

import com.github.danielnd14.app.dto.TabbedDTO;

import javax.swing.*;
import java.awt.*;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public final class TupleTabbedPane extends JTabbedPane {
	private int nextIDX = 0;

	@Override
	public void remove(Component component) {
		SwingUtilities.invokeLater(() -> super.remove(component));
		((TupleTabbedContent) component).close();
		nextIDX--;
	}

	@Override
	public void remove(int index) {
		//not supported operation
	}

	@Override
	public void removeAll() {
		super.removeAll();
		nextIDX = 0;
	}

	@Override
	public void addTab(String title, Icon icon, Component component, String tip) {
		if (!(component instanceof TupleTabbedContent))
			throw new IllegalArgumentException("Only " + TupleTabbedContent.class + "  is valid here");
		super.addTab(title, icon, component, tip);
		nextIDX++;
	}

	@Override
	public void addTab(String title, Icon icon, Component component) {
		if (!(component instanceof TupleTabbedContent))
			throw new IllegalArgumentException("Only " + TupleTabbedContent.class + "  is valid here");
		super.addTab(title, icon, component);
		nextIDX++;
	}

	@Override
	public void addTab(String title, Component component) {
		if (!(component instanceof TupleTabbedContent))
			throw new IllegalArgumentException("Only " + TupleTabbedContent.class + "  is valid here");
		super.addTab(title, component);
		nextIDX++;
	}

	@Override
	public Component add(Component component) {
		if (!(component instanceof TupleTabbedContent))
			throw new IllegalArgumentException("Only " + TupleTabbedContent.class + "  is valid here");
		nextIDX++;
		return super.add(component);
	}

	@Override
	public Component add(String title, Component component) {
		if (!(component instanceof TupleTabbedContent))
			throw new IllegalArgumentException("Only " + TupleTabbedContent.class + "  is valid here");
		nextIDX++;
		return super.add(title, component);
	}

	@Override
	public Component add(Component component, int index) {
		if (!(component instanceof TupleTabbedContent))
			throw new IllegalArgumentException("Only " + TupleTabbedContent.class + "  is valid here");
		nextIDX++;
		return super.add(component, index);
	}

	@Override
	public void add(Component component, Object constraints) {
		if (!(component instanceof TupleTabbedContent))
			throw new IllegalArgumentException("Only " + TupleTabbedContent.class + "  is valid here");
		nextIDX++;
		SwingUtilities.invokeLater(() -> super.add(component, constraints));
	}

	@Override
	public void add(Component component, Object constraints, int index) {
		if (!(component instanceof TupleTabbedContent))
			throw new IllegalArgumentException("Only " + TupleTabbedContent.class + "  is valid here");
		nextIDX++;
		SwingUtilities.invokeLater(() -> super.add(component, constraints, index));
	}


	public List<TabbedDTO> tabbedDTOList() {
		var tabCount = getTabCount();
		return IntStream.range(0, tabCount)
				.mapToObj(i -> {
					var tabContent = (TupleTabbedContent) getComponentAt(i);
					var title = tabContent.getSheetTabName();
					var tupleList = tabContent.listTuple();
					return new TabbedDTO(title, tupleList);
				}).filter(TabbedDTO::isValid).collect(Collectors.toUnmodifiableList());
	}

	public int getNextIDX() {
		return nextIDX;
	}
}

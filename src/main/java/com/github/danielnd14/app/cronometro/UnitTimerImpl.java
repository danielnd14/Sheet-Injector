package com.github.danielnd14.app.cronometro;

import javax.swing.*;
import java.awt.*;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.Timer;
import java.util.TimerTask;

public final class UnitTimerImpl implements BasicUnitTimer {
	private final JLabel label;
	private final DateTimeFormatter cronoFormat;
	private final Color stopedColor;
	private final Color runningColor;
	private LocalTime time;
	private Timer timer;

	public UnitTimerImpl(final JLabel label, final Color runningColor, final Color stopedColor) {
		this.label = label;
		this.stopedColor = stopedColor;
		this.runningColor = runningColor;
		timer = new Timer();
		cronoFormat = DateTimeFormatter.ofPattern("HH:mm:ss");
	}

	@Override
	public void start() {
		label.setForeground(runningColor);
		final var delta = 100;
		if (time == null)
			time = LocalTime.parse("00:00:00");
		final LocalTime[] tempo = {time};
		TimerTask tarefa = new TimerTask() {
			@Override
			public void run() {
				tempo[0] = tempo[0].plus(delta, ChronoUnit.MILLIS);
				SwingUtilities.invokeLater(() -> {
					label.setText(tempo[0].format(cronoFormat));
					Thread.onSpinWait();
				});
			}
		};
		timer.scheduleAtFixedRate(tarefa, 0, delta);
	}

	@Override
	public void stop() {
		timer.cancel();
		timer.purge();
		timer = new Timer();
		label.setForeground(stopedColor);
	}
}

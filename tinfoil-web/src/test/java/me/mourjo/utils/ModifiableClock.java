package me.mourjo.utils;

import java.time.Clock;
import java.time.Instant;
import java.time.OffsetDateTime;
import java.time.ZoneId;
import java.time.temporal.TemporalAmount;

public class ModifiableClock extends Clock {
	private Instant instant;

	private final ZoneId zone;

	public ModifiableClock(Instant instant, ZoneId zone) {
		this.instant = instant;
		this.zone = zone;
	}

	@Override
	public ZoneId getZone() {
		return zone;
	}

	@Override
	public Clock withZone(ZoneId zone) {
		return new ModifiableClock(instant, zone);
	}

	@Override
	public Instant instant() {
		return instant;
	}

	public void tickForward(TemporalAmount temporalAmount) {
		set(instant().plus(temporalAmount));
	}

	public void tickBackward(TemporalAmount temporalAmount) {
		set(instant().minus(temporalAmount));
	}

	private void set(Instant instant) {
		this.instant = instant;
	}

	public static ModifiableClock fixed(Instant instant, ZoneId zone) {
		return new ModifiableClock(instant, zone);
	}

	public static ModifiableClock fixed(OffsetDateTime offsetDateTime) {
		return fixed(offsetDateTime.toInstant(), offsetDateTime.getOffset());
	}
}

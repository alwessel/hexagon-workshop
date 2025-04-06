package de.pentacor.hexagon.workshop.app;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;

import java.time.*;
import java.time.format.DateTimeFormatter;

@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class TestClock extends Clock {

    private static final ZoneId ZONE_ID = ZoneId.systemDefault();
    private Clock clock;

    public static TestClock freeze(String formattedCurrentDateTime) {
        var now = LocalDateTime.parse(formattedCurrentDateTime, DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm"));
        return new TestClock(Clock.fixed(now.atZone(ZONE_ID).toInstant(), ZONE_ID));
    }

    public void passTime(Duration duration) {
        var changedNow = instant().plus(duration);
        clock = Clock.fixed(changedNow.atZone(ZONE_ID).toInstant(), ZONE_ID);
    }

    @Override
    public ZoneId getZone() {
        return clock.getZone();
    }

    @Override
    public Clock withZone(ZoneId zone) {
        return clock.withZone(zone);
    }

    @Override
    public Instant instant() {
        return clock.instant();
    }

    public LocalDateTime now() {
        return LocalDateTime.now(clock);
    }
}

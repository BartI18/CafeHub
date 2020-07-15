package org.boka.cafe;

import org.boka.cafe.db.DatabaseManipulation;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.temporal.ChronoUnit;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class SchedulerAmountAttemptsUser {

    public void scheduler() {
        ExecutorService executorService = Executors.newSingleThreadExecutor();
        executorService.submit(() -> {
            while (true) {
                try {
                    LocalDateTime dateTimeNow = LocalDateTime.now();
                    LocalDateTime localDateTime = dateTimeNow.truncatedTo(ChronoUnit.DAYS).plus(1, ChronoUnit.DAYS);
                    System.out.println("Go to sleep");
                    long delta = (localDateTime.toEpochSecond(ZoneOffset.UTC) - dateTimeNow.toEpochSecond(ZoneOffset.UTC)) * 1000;
                    Thread.sleep(delta); //иду спать до 12 часов ночи
                } catch (InterruptedException e) {
                    System.out.println(e.getMessage());
                    return;
                }
                int amountUser = DatabaseManipulation.resetAmountRequest();
                System.out.println(String.format("SuccessReset! Amount user: %d", amountUser));
            }
        });
    }

}

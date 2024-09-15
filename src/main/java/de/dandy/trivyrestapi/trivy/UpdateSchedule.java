package de.dandy.trivyrestapi.trivy;

import de.dandy.trivyrestapi.trivy.command.UpdateDbCommand;
import de.dandy.trivyrestapi.trivy.command.UpdateJavaDbCommand;
import org.springframework.context.SmartLifecycle;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Service
public class UpdateSchedule implements SmartLifecycle {


    private final Trivy trivy;

    public UpdateSchedule(Trivy trivy) {
        this.trivy = trivy;
    }

    @Scheduled(cron = "0 0 0 * * *")
    public void update() {
        start();
    }


    @Override
    public void start() {
        Thread.startVirtualThread(() -> {
            System.out.println("Update db");
            trivy.call(new UpdateDbCommand());
            System.out.println("Update db finished");
        });
        Thread.startVirtualThread(() -> {
            System.out.println("Update java db");
            trivy.call(new UpdateJavaDbCommand());
            System.out.println("Update java db finished");
        });
    }

    @Override
    public void stop() {
    }

    @Override
    public boolean isRunning() {
        return false;
    }
}

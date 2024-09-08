package de.dandy.trivyrestapi.trivy;

import ch.qos.logback.core.net.SyslogOutputStream;
import de.dandy.trivyrestapi.trivy.command.UpdateCommand;
import org.springframework.context.SmartLifecycle;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.io.IOException;

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
            try {
                System.out.println("Update");
                trivy.call(new UpdateCommand());
                System.out.println("Update finished");
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
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

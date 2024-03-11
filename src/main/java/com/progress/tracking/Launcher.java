package com.progress.tracking;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;

import java.awt.*;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

@SpringBootApplication
public class Launcher {

    public static void main(String[] args) {
        SpringApplication.run(Launcher.class, args);
    }

    @EventListener(ApplicationReadyEvent.class)
    public static void browse() {
        if (true)
            return;
        System.setProperty("java.awt.headless", "false");

        if (!Desktop.isDesktopSupported()) {
            System.err.println("Desktop not supported!");
            return;
        }
        Desktop desktop = Desktop.getDesktop();
        try {
            desktop.browse(new URI("http://localhost:8080"));
        } catch (IOException | URISyntaxException e) {
            e.printStackTrace();
        }
    }

}

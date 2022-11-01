package Logger;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * The message logger.
 */
public class Logger {
    /** The log file path. */
    private String logPath = "system.log";
    /** The file writer. */
    private BufferedWriter writer;
    /** The message type. */
    public enum MessageType {
        LOG, WARNING, ERROR
    }

    /**
     * Constructs a new default Logger instance.
     */
    protected Logger() {

    }

    /**
     * Constructs a new custom Logger instance.
     * @param logPath String - The log file path.
     */
    public Logger(String logPath) {
        this.logPath = logPath;
    }

    /**
     * Logs the message.
     * @param message String - The message to log.
     */
    public void log(String message, MessageType type) {
        try {
            this.writer = new BufferedWriter(new FileWriter(this.logPath, true));

            DateTimeFormatter dtf = DateTimeFormatter.ofPattern("[yyyy-MM-dd HH:mm:ss]");

            this.writer.append(dtf.format(LocalDateTime.now()));
            this.writer.append(' ');

            switch (type) {
                case LOG -> this.writer.append("Log: ");
                case WARNING -> this.writer.append("Warning: ");
                case ERROR -> this.writer.append("Error: ");
            }

            this.writer.append(message);
            this.writer.append(System.lineSeparator());

            for (int i = 0; i < 80; i++) {
                this.writer.append('=');
            }

            this.writer.append(System.lineSeparator());
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                writer.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

}

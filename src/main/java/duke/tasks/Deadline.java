package duke.tasks;
import duke.DateTimeParser;
import duke.exceptions.InvalidDeadlineException;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
public class Deadline extends Task {

    protected LocalDateTime parsedDateTime;

    public Deadline(String description, String by) throws InvalidDeadlineException {
        super(description);

        parsedDateTime = DateTimeParser.parseDateTime(by);

        if (parsedDateTime == null) {
            throw new InvalidDeadlineException("Your deadline is invalid. ");
        }
    }

    public String getBy() {
        return parsedDateTime.format(DateTimeFormatter.ofPattern("MMM d yyyy, h:mm a"));
    }

    public boolean occurring(LocalDateTime taskDate) {
        return taskDate != null && taskDate.equals(this.parsedDateTime);
    }

    @Override
    public String toString() {
        return "[D]" + " [" + this.getStatusIcon() + "] " + super.toString() + " (by: " + this.getBy() + ")";
    }
}

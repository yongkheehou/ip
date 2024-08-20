package duke;

import duke.exceptions.EmptyTodoDescriptionException;
import duke.exceptions.UnknownMessageException;
import duke.tasks.Deadline;
import duke.tasks.Event;
import duke.tasks.Task;
import duke.tasks.ToDos;

import java.util.Scanner;
import java.util.ArrayList;

public class DailyTasks {
    public static final String BOT_NAME = "DailyTasks";
    public static final String GREETING = "Hello! I'm " + DailyTasks.BOT_NAME + ", your awesome task planner!";
    public static final String GOODBYE = "Bye. Hope to see you again soon!";

    public ArrayList<Task> tasks;

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        
        DailyTasks dailyTasks = new DailyTasks();
        dailyTasks.tasks = new ArrayList<Task>();

        System.out.println(Formatter.formatOutputMessage(DailyTasks.GREETING));

        while (scanner.hasNextLine()) {
            String userInput = scanner.nextLine();

            if (userInput.equals("list")) {
                System.out.println(Formatter.formatTaskListings(dailyTasks.tasks));
            } else if (userInput.contains("unmark")) {
                int index = Integer.parseInt(userInput.split(" ", 2)[1]) - 1; // minus 1 because array is 0-indexed

                dailyTasks.tasks.get(index).setNotDone();
                System.out.println(Formatter.formatUnmarkTask(dailyTasks.tasks.get(index)));
            } else if (userInput.contains("mark")) {
                int index = Integer.parseInt(userInput.split(" ", 2)[1]) - 1;

                dailyTasks.tasks.get(index).setDone();
                System.out.println(Formatter.formatMarkTask(dailyTasks.tasks.get(index)));
            } else if (userInput.contains("delete")) {
                int index = Integer.parseInt(userInput.split(" ", 2)[1]) - 1;

                Task task = dailyTasks.tasks.get(index);
                dailyTasks.tasks.remove(index);
                System.out.println(Formatter.formatDeleteTask(task, dailyTasks.tasks.size()));
            } else if (userInput.contains("bye")) {
                System.out.println(Formatter.formatOutputMessage(DailyTasks.GOODBYE));
                return;
            } else {
                try {
                    if (userInput.contains("todo")) {
                        try {
                            String description = userInput.split(" ", 2)[1];
                            dailyTasks.tasks.add(new ToDos(description));
                        } catch (ArrayIndexOutOfBoundsException e) {
                            throw new EmptyTodoDescriptionException(e.toString());
                        }
                    } else if (userInput.contains("deadline")) {
                        String[] deadlineInformation = userInput.split("/by");
                        String description = deadlineInformation[0].replace("deadline", "").trim();
                        String date = deadlineInformation[1].trim();

                        dailyTasks.tasks.add(new Deadline(description, date));
                    } else if (userInput.contains("event")) {
                        String[] removeFrom = userInput.split("/from");
                        String description = removeFrom[0].replace("event", "").trim();

                        String[] removeTo = removeFrom[1].split("/to");
                        String start = removeTo[0].trim();
                        String end = removeTo[1].trim();

                        dailyTasks.tasks.add(new Event(description, start, end));
                    } else {
                        throw new UnknownMessageException();
                    }
                } catch (EmptyTodoDescriptionException e) {
                    System.out.println(Formatter.formatOutputMessage("Please include a description of your todo task!!!"));
                    return;
                } catch (UnknownMessageException e) {
                    System.out.println(Formatter.formatOutputMessage("Please enter a valid task!"));
                    return;
                }

                String formattedTask = Formatter.formatAddTask(dailyTasks.tasks.size(), dailyTasks.tasks.get(dailyTasks.tasks.size() - 1));
                System.out.println(formattedTask);
            }
        }
    }
}

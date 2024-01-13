package main.commands;

import main.output.CommandOutput;
import org.checkerframework.checker.units.qual.C;

public class NullCommand extends Command {

    @Override
    public CommandOutput execute() {
        return null;
    }
}

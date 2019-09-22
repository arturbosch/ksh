package io.gitlab.arturbosch.ksh.java;

import io.gitlab.arturbosch.ksh.api.ShellClass;
import io.gitlab.arturbosch.ksh.api.ShellMethod;
import io.gitlab.arturbosch.ksh.api.ShellOption;
import io.gitlab.arturbosch.ksh.api.provider.ShellClassesProvider;
import io.gitlab.arturbosch.kutils.Container;
import org.jetbrains.annotations.NotNull;

import java.util.Collections;
import java.util.List;

public class CommandProvider implements ShellClassesProvider {

    @Override
    public List<? extends ShellClass> provide(@NotNull Container container) {
        return Collections.singletonList(new Hello());
    }

    @Override
    public int getPriority() {
        return 0;
    }

    public static class Hello implements ShellClass {

        @NotNull
        @Override
        public String getHelp() {
            return "Prints hello world.";
        }

        @ShellMethod(value = {"test"})
        public String main(@ShellOption(value = {"name", "n", ""}) String name) {
            return String.format("Hello, %s!", name == null ? "World" : name);
        }
    }
}

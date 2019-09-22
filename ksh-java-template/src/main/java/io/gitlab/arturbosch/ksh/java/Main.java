package io.gitlab.arturbosch.ksh.java;

import io.gitlab.arturbosch.ksh.Bootstrap;
import io.gitlab.arturbosch.ksh.MainKt;
import io.gitlab.arturbosch.ksh.NoopContainer;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;

public final class Main {

    public static void main(String[] args) {
        List<String> arguments = Arrays.asList(args);
        NoopContainer container = new NoopContainer();
        Bootstrap bootstrap = MainKt.bootstrap(container);

        if (arguments.isEmpty()) {
            bootstrap.runLooping();
        } else {
            if (arguments.size() == 1 && Files.exists(Paths.get(arguments.get(0)))) {
                bootstrap.runFile(Paths.get(arguments.get(0)));
            } else {
                bootstrap.runOnce(String.join(" ", arguments));
            }
        }
    }
}

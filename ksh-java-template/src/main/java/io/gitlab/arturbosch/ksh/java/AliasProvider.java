package io.gitlab.arturbosch.ksh.java;

import io.gitlab.arturbosch.ksh.aliases.AliasesProvider;
import io.gitlab.arturbosch.kutils.Container;
import io.gitlab.arturbosch.kutils.ResourcesKt;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;

public class AliasProvider implements AliasesProvider {

    @Override
    public Map<String, ? extends String> provide(@NotNull Container container) {
        Path file = ResourcesKt.resourceAsPath("aliases.properties");
        HashMap<String, String> result = new HashMap<>();
        try {
            Files.lines(file)
                    .map(line -> line.split("="))
                    .forEach(parts -> result.put(parts[0], parts[1]));
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
        return result;
    }

    @Override
    public int getPriority() {
        return 0;
    }
}

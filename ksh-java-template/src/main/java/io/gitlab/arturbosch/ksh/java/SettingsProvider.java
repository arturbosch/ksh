package io.gitlab.arturbosch.ksh.java;

import io.gitlab.arturbosch.ksh.api.ShellSettings;
import io.gitlab.arturbosch.ksh.api.provider.ShellSettingsProvider;
import io.gitlab.arturbosch.kutils.Container;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.IOException;
import java.io.UncheckedIOException;

public class SettingsProvider implements ShellSettingsProvider {

    @Override
    public ShellSettings provide(@NotNull Container container) {
        try {
            String tmpFile = File.createTempFile("ksh-java-integration", null).toString();
            return new ShellSettings() {

                @Override
                public int getPriority() {
                    return 0;
                }

                @NotNull
                @Override
                public String getApplicationName() {
                    return "fromJava: ";
                }

                @NotNull
                @Override
                public String getHistoryFile() {
                    return tmpFile;
                }

                @NotNull
                @Override
                public String prompt() {
                    return getApplicationName();
                }
            };
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

    @Override
    public int getPriority() {
        return 0;
    }
}

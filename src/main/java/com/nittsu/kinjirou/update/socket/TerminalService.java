package com.nittsu.kinjirou.update.socket;

import com.nittsu.kinjirou.core.common.OSUtil;
import com.nittsu.kinjirou.core.common.ThreadUtil;
import com.pty4j.PtyProcess;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;

import java.io.*;
import java.util.Objects; 

@Component
@Scope("prototype")
public class TerminalService {
    private WebSocketSession session;
    private PtyProcess process;

    private BufferedReader inputReader;
    private BufferedReader errorReader;
    private BufferedWriter outputWriter;

    public void initialize() {
        ThreadUtil.start(() -> {
            try {
                if (OSUtil.isWindows()) {
                    process = PtyProcess.exec("cmd.exe".split("\\s+"));
                } else {
                    process = PtyProcess.exec("/bin/bash -i".split("\\s+"));
                }

                inputReader = new BufferedReader(new InputStreamReader(process.getInputStream()));
                errorReader = new BufferedReader(new InputStreamReader(process.getErrorStream()));

                outputWriter = new BufferedWriter(new OutputStreamWriter(process.getOutputStream()));

                ThreadUtil.start(() -> printReader(inputReader));

                ThreadUtil.start(() -> printReader(errorReader));

                process.waitFor();
            } catch (Exception e) {
                try {
                    session.sendMessage(new TextMessage(e.getMessage()));;
                } catch (IOException e1) {
                    e1.printStackTrace();
                }

                e.printStackTrace();
            }
        });
    }

    public TerminalService setSession(WebSocketSession session) {
        this.session = session;

        return this;
    }

    private void printReader(BufferedReader bufferedReader) {
        try {
            int nRead;
            char[] data = new char[1 * 1024];

            while ((nRead = bufferedReader.read(data, 0, data.length)) != -1) {
                StringBuilder builder = new StringBuilder(nRead);
                builder.append(data, 0, nRead);

                session.sendMessage(new TextMessage(builder.toString()));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void execCommand(String command) {
        if (Objects.isNull(command)) {
            return;
        }

        ThreadUtil.start(() -> {
            try {
                outputWriter.write(command);
                outputWriter.flush();
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }
}

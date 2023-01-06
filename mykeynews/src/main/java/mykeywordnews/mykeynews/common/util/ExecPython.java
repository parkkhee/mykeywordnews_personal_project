package mykeywordnews.mykeynews.common.util;

import org.apache.commons.exec.CommandLine;
import org.apache.commons.exec.DefaultExecutor;
import org.apache.commons.exec.PumpStreamHandler;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;

public class ExecPython {
    public static void execPython(List<String> command) throws IOException, InterruptedException {
        //python으로 실행
        CommandLine commandLine = CommandLine.parse(command.get(0));
//        for (int i = 1, n = command.length; i < n; i++) {
//            commandLine.addArgument(command[i]);
//        }
        commandLine.addArgument(command.get(1));
        commandLine.addArgument(command.get(2));
        commandLine.addArgument(command.get(3));
        commandLine.addArgument(command.get(4));


        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        PumpStreamHandler pumpStreamHandler = new PumpStreamHandler(outputStream);
        DefaultExecutor executor = new DefaultExecutor();
        executor.setStreamHandler(pumpStreamHandler);
        executor.execute(commandLine);

//        System.out.println("output: " + outputStream.toString());

    }
}

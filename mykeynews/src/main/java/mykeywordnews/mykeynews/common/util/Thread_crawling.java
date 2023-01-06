package mykeywordnews.mykeynews.common.util;

import mykeywordnews.mykeynews.dto.UserRequestDto;
import mykeywordnews.mykeynews.dto.UserResponseDto;
import org.apache.commons.exec.CommandLine;
import org.apache.commons.exec.DefaultExecutor;
import org.apache.commons.exec.PumpStreamHandler;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.List;

import static mykeywordnews.mykeynews.common.util.ExecPython.execPython;

public class Thread_crawling extends Thread{

    private int thread_number;
    private UserRequestDto requestDto;
    private UserResponseDto signupDto;

    public Thread_crawling(UserRequestDto requestDto, UserResponseDto responseDto, int thread_number) {
        this.requestDto = requestDto;
        this.signupDto = responseDto;
        this.thread_number = thread_number;
    }


    public void run() {

        switch (thread_number){
            case 1 : try {
                System.out.println("Python Call");

                List<String> command = new ArrayList<>();

                command.add("python");
                command.add("C:/Users/root/Desktop/MykeywordProject/pythonfile/crawling.py");
                command.add(requestDto.getUserKeyword().get(0));
                command.add(requestDto.getUserKeyword().get(1));
                command.add(Long.toString(signupDto.getUserNo()));


                try {
                    execPython(command);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                System.out.println("Python Call _ END");
                sleep(1000);
            } catch (Exception e) {
                e.printStackTrace();
            }
            break;

            case 2: try {
                System.out.println("Python Call _ zum");

                List<String> command = new ArrayList<>();

                command.add("python");
                command.add("C:/Users/root/Desktop/MykeywordProject/pythonfile/zumcrawling.py");
                command.add(requestDto.getUserKeyword().get(0));
                command.add(requestDto.getUserKeyword().get(1));
                command.add(Long.toString(signupDto.getUserNo()));
//                CommandLine commandLine = CommandLine.parse(command.get(0));
//                commandLine.addArgument(command.get(1));
//                command.add(Long.toString(signupDto.getUserNo()));


                try {
                    execPython(command);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                System.out.println("Python Call _ Z_END");
                sleep(1000);
            } catch (Exception e) {
                e.printStackTrace();
            }
            break;

            case 3: try {
                System.out.println("Python Call _ being");

                List<String> command = new ArrayList<>();

                command.add("python");
                command.add("C:/Users/root/Desktop/MykeywordProject/pythonfile/beingcrawling.py");
                command.add(requestDto.getUserKeyword().get(0));
                command.add(requestDto.getUserKeyword().get(1));
                command.add(Long.toString(signupDto.getUserNo()));
//                CommandLine commandLine = CommandLine.parse(command.get(0));
//                commandLine.addArgument(command.get(1));
//                command.add(Long.toString(signupDto.getUserNo()));
                try {
                    execPython(command);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                System.out.println("Python Call _ B_END");
                sleep(1000);
            } catch (Exception e) {
                e.printStackTrace();
            }
                break;

            case 4: try {
                System.out.println("Python Call _ nate");

                List<String> command = new ArrayList<>();

                command.add("python");
                command.add("C:/Users/root/Desktop/MykeywordProject/pythonfile/natecrawling.py");
                command.add(requestDto.getUserKeyword().get(0));
                command.add(requestDto.getUserKeyword().get(1));
                command.add(Long.toString(signupDto.getUserNo()));
//                CommandLine commandLine = CommandLine.parse(command.get(0));
//                commandLine.addArgument(command.get(1));
//                command.add(Long.toString(signupDto.getUserNo()));


                try {
                    execPython(command);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                System.out.println("Python Call _ NT_END");
                sleep(1000);
            } catch (Exception e) {
                e.printStackTrace();
            }
                break;

        }


    }

}

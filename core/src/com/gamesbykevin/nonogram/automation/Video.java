package com.gamesbykevin.nonogram.automation;

import java.io.File;

import static com.gamesbykevin.nonogram.automation.Automation.*;

public class Video {

    private static final String COMMAND_OBS_RECORD_START = "C:\\Program Files\\OBSCommand_v1.5.4\\OBSCommand /startrecording";

    private static final String COMMAND_OBS_PATH = "C:\\Program Files\\obs-studio\\bin\\64bit\\";
    private static final String COMMAND_OBS_FILE = "obs64.exe";

    //private static final String COMMAND_OBS_RECORD_START = COMMAND_OBS_PATH + COMMAND_OBS_FILE + " --startrecording";
    private static final String COMMAND_OBS_RECORD_STOP = "C:\\Program Files\\OBSCommand_v1.5.4\\OBSCommand /stoprecording";
    private static final String COMMAND_OBS_CLOSE = "taskkill /im obs64.exe /t /f";

    public static final String EXT_FILE = ".mkv";

    public static final String DIR_RECORDING_SRC = DIR + "videos\\";
    public static final String DIR_RECORDING = DIR_DESTINATION + "recordings\\";

    //get the recorded file, rename it, and move to another directory
    private static void renameFileMove(final String filename) {

        String destPath = DIR_RECORDING + filename;

        File[] files = new File(DIR_RECORDING_SRC).listFiles();

        if (files.length != 1)
            throw new RuntimeException("More than 1 video exists: " + DIR_RECORDING_SRC);

        //get our recording file, there should only be 1 file here
        File recordingSrc = files[0];

        //where we want to move our file to
        File recordingDst = new File(destPath);

        do {
            //keep attempting to move the file
        } while (!recordingSrc.renameTo(recordingDst));

        System.out.println("Recording moved to: " + destPath);
    }

    public static void openAndRecord() {
        runCommand(COMMAND_OBS_PATH, COMMAND_OBS_FILE);
        sleep(1000);
        runCommand(COMMAND_OBS_RECORD_START, null);
    }

    public static void stopAndClose(String filename) {
        runCommand(COMMAND_OBS_RECORD_STOP, null);
        sleep(100);
        renameFileMove(filename);
        runCommand(COMMAND_OBS_CLOSE, null);
    }

    private static void runCommand(String directory, String filename) {
        try {
            Runtime rt = Runtime.getRuntime();

            if (filename == null) {
                rt.exec(directory);
            } else {
                rt.exec(directory + filename, null, new File(directory));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
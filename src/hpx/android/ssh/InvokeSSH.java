//  Copyright (c) 2012 Bryce Adelstein-Lelbach 
//
//  Distributed under the Boost Software License, Version 1.0. (See accompanying
//  file LICENSE_1_0.txt or copy at http://www.boost.org/LICENSE_1_0.txt)

package hpx.android.ssh;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;


public class InvokeSSH
{
    public static String invoke(String host, String[] remoteCommand)
    {
        List<String> command = new ArrayList<String>();
        command.add("ssh");
        command.add(host);

        for (String s : remoteCommand)
            command.add(s);

        ProcessBuilder builder = new ProcessBuilder(command);

        // Combine stderr and stdout.
        builder.redirectErrorStream(true);

        Process sshProcess = null;
        try { sshProcess = builder.start(); }

        catch (IOException e)
        {
            System.out.println("ERROR (starting process): " + e.toString());
            return ""; 
        }

        // Get the output from the process.
        InputStream s = sshProcess.getInputStream();
        InputStreamReader r = new InputStreamReader(s);
        BufferedReader sshOutput = new BufferedReader(r);

        try
        {
            // Print it out.
            String output = "";
            String line;
            while ((line = sshOutput.readLine()) != null)
                output = output + '\n' + line;

            return output;
        }

        catch (IOException e)
        {
            System.out.println("ERROR (printing output): " + e.toString());
            return "";
        }
    }

    public static String invoke(String host, String remoteCommand)
    {
        String[] array = { remoteCommand };
        return invoke(host, array);
    }

    public static Integer invokeConvertInt(String host, String[] remoteCommand)
    {
        // Trim whitespace and newlines.
        return new Integer(invoke(host, remoteCommand).trim());
    }

    public static int invokeConvertInt(String host, String remoteCommand)
    {
        // Trim whitespace and newlines.
        return new Integer(invoke(host, remoteCommand).trim());
    }

    public static void main(String[] args)
    {
        // Single string form.
        invoke("HERMIONE", "ls");

        // Array of strings form.
        String[] lsSandbox = { "ls", "~/sandbox" };
        invoke("HERMIONE", lsSandbox);

        // Conversion form - we need this to get the PBS job ID back from start
        // and restart commands.
        String[] echoInt = { "echo", "1773" };
        Integer mockPBSID = invokeConvertInt("HERMIONE", echoInt);

        if (mockPBSID != 1773)
            System.out.println("ERROR: mockPBSID should be 1773, but it is "
                              + mockPBSID.toString());
    }
}


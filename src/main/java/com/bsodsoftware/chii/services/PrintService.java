package com.bsodsoftware.chii.services;

import java.io.DataOutputStream;
import java.io.File;
import java.net.Socket;
import java.nio.file.Files;

import org.springframework.stereotype.Service;

@Service
public class PrintService {

	public void print(File document, String printerIpAddress)
	{
	    try (Socket socket = new Socket(printerIpAddress, 9100))
	    {
	        DataOutputStream out = new DataOutputStream(socket.getOutputStream());
	        String title = document.getName();
	        byte[] bytes = Files.readAllBytes(document.toPath());

	        out.write(27);
	        out.write("%-12345X@PJL\n".getBytes());
	        out.write(("@PJL SET JOBNAME=" + title + "\n").getBytes());
	        out.write("@PJL SET DUPLEX=ON\n".getBytes());
	        out.write("@PJL SET STAPLEOPTION=ONE\n".getBytes());
	        out.write("@PJL ENTER LANGUAGE=PDF\n".getBytes());
	        out.write(bytes);
	        out.write(27);
	        out.write("%-12345X".getBytes());
	        out.flush();
	        out.close();
	    }
	    catch (Exception e)
	    {
	        System.out.println(e);
	    }
	}
}
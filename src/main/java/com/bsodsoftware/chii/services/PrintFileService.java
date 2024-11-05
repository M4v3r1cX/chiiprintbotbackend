package com.bsodsoftware.chii.services;

import java.awt.print.PageFormat;
import java.awt.print.Paper;
import java.awt.print.Printable;
import java.awt.print.PrinterException;
import java.awt.print.PrinterJob;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.util.Base64;
import java.util.zip.ZipInputStream;

import javax.print.PrintService;

import org.springframework.stereotype.Service;

import com.spire.doc.Document;
import com.spire.pdf.PdfDocument;

@Service
public class PrintFileService {
	
	public boolean setPrint(String toPrint) throws Exception {
		boolean ret = false;
		byte[] bytes = Base64.getDecoder().decode(toPrint);
		Printable printable = null;
		if (isDocx(bytes)) {
			System.out.println("Imprimiendo docx");
			printable = new Document(new ByteArrayInputStream(bytes));
		} else {
			System.out.println("Imprimiendo PDF");
			printable = new PdfDocument(bytes);
		}
		ret = print(printable);
		return ret;
	}
	
	private boolean print(Printable printable) throws Exception {
		boolean ret = true;
		try {
			PrinterJob printerJob = PrinterJob.getPrinterJob();
			PrintService printService = findPrintService("Brother DCP-T710W Printer");
			printerJob.setPrintService(printService);
	        PageFormat pageFormat = printerJob.defaultPage();
	        Paper paper = pageFormat.getPaper();
	        paper.setImageableArea(0, 0, pageFormat.getWidth(), pageFormat.getHeight());
	        pageFormat.setPaper(paper);
	        printerJob.setCopies(1);
	        printerJob.setPrintable(printable,pageFormat);

	        printerJob.print();
		} catch (Exception ex) {
			ret = false;
		}
		
        
        return ret;
	}
	
	private PrintService findPrintService(String printerName) {
		PrintService[] printServices = PrinterJob.lookupPrintServices();
        for (PrintService printService : printServices) {
            if (printService.getName().equals(printerName)) {

                System.out.println(printService.getName());
                return printService;
            }
        }
        return null;
	}
	
	public boolean isDocx(byte[] data) throws Exception {
		return new ZipInputStream(new ByteArrayInputStream(data)).getNextEntry() != null;
	}
}
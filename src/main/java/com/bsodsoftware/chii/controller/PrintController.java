package com.bsodsoftware.chii.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.bsodsoftware.chii.dto.ResponseDTO;
import com.bsodsoftware.chii.services.PrintFileService;

@RestController()
public class PrintController {
	
	@Autowired
	private PrintFileService printService;

	@PostMapping("/print")
	@ResponseBody
	public ResponseDTO imprimir(@RequestBody String toPrint) {	// TODO implementar seguridad web
		ResponseDTO ret = new ResponseDTO();
		try {
			printService.setPrint(toPrint);
			ret.setCode(200);
			ret.setResponse("Funca!");
		} catch (Exception ex) {
			ex.printStackTrace();
			ret.setCode(500);
			ret.setResponse("No funca :(");
		}
		return ret;
	}
}

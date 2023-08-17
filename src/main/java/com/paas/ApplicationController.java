package com.paas;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.paas.app.model.Response;
import com.paas.app.util.ReadExcelUtil;

@RestController
@RequestMapping(path = "/api")
public class ApplicationController {
	@Autowired
	ReadExcelUtil readExcelUtil;
	
	@RequestMapping(value = "/getAppPlatformInfo", method = RequestMethod.GET)
    public Response getPaasInfo(@RequestParam(name = "date", required = false) Date date) {
		Response response = new Response();
		try {
			// Create Workbook instance holding reference to .xlsx file
			response = readExcelUtil.readExcel();
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return response;
    }
}

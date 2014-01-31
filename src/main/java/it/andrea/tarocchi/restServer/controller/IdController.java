package it.andrea.tarocchi.restServer.controller;

import it.andrea.tarocchi.restServer.service.IdService;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class IdController {
	private static Logger log = Logger.getLogger(IdController.class.getName());
	
	@Resource(name="idService")
	private IdService idService;
	
	@RequestMapping(method=RequestMethod.POST, value="/restServer")
	public @ResponseBody String home(@RequestParam (value = "id", required = true) String id)
	{
		log.info("POSTED id: "+id);
		try {
			idService.add(id);
			return "status=success";
		} catch (Exception e) {
			e.printStackTrace();
			return "status=failure";
		}
	}

	public IdService getIdService() {
		return idService;
	}

	public void setIdService(IdService idService) {
		this.idService = idService;
	}
}

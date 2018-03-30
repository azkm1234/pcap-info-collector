package controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import domain.RResult;
import service.KafKaDataSender;

@Controller
public class DataCollector {
	
	@Autowired
	private KafKaDataSender sender;
	@RequestMapping(value = "/message", method = RequestMethod.POST)
	public @ResponseBody RResult MessageHandle(@RequestBody String body) {
		System.out.print(body);
		sender.send(body, "testtopic");
		RResult r = new RResult();
		r.setMessage("ok");
		r.setTag(200);
		return r;
	}
}

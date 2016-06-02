package at.ac.tuwien.cashtechthon.controller;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;
import java.util.List;

@RestController
@RequestMapping("/locations")
public class LocationController extends AbstractRestController {

	@RequestMapping(method=RequestMethod.GET, produces=MediaType.APPLICATION_JSON_VALUE)
	public List<String> getLocations() {
		return Arrays.asList("Vienna", "Graz", "St. Pölten");
	}
}

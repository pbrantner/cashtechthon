package at.ac.tuwien.cashtechthon.controller;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;
import java.util.List;

@RestController
@RequestMapping("/classifications")
public class ClassificationController extends AbstractRestController {

    @RequestMapping(method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public List<String> getClassifications() {
        //return (new Classifications()).add("Groceries").add("Transportation").add("Income");
        return Arrays.asList("Groceries", "Transportation", "Income");
    }
}

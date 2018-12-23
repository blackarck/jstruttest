/*
 * Vivek Sharma
 */
package javapiproj;

import org.json.simple.JSONArray;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@SpringBootApplication
@RestController

@RequestMapping("/")
public class lottoapi {

    lottoservice l;
/*
    lottoapi() {
      //  l = new lottoservice();
    }
*/
    @CrossOrigin(origins = "http://localhost:4200")
    @RequestMapping(path = "/lottoget")
    String home() {
        return "Hello World! cors enabled";
    }

    @CrossOrigin(origins = "http://localhost:9900")
    @RequestMapping(path = "/lottorun")
    String home1() {
        return "Hello run World! cors disabled";
    }

    @CrossOrigin(origins = "http://localhost:4200")
    @RequestMapping(path = "/api")
    public JSONArray getlotto(@RequestParam("persname") String persname,@RequestParam("buyval") Boolean buyval) {
        l=new lottoservice();
        System.out.println("Input param is " + persname + " buy val "+buyval);
        JSONArray getval=l.getlotdtls(persname,buyval);
        return getval;
    }
    
    public static void main(String argsp[]) {
        SpringApplication.run(lottoapi.class, argsp);
    }//end of mains
}

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package javapiproj;

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
        return "Hello World!";
    }

    @RequestMapping(path = "/lottorun")
    String home1() {
        return "Hello run World!";
    }

    @RequestMapping(path = "/api")
    public String getlotto(@RequestParam("persname") String persname) {
        l=new lottoservice();
        System.out.println("Input param is " + persname);
        String getval=l.getlotdtls(persname);
        return getval;
    }
    
    public static void main(String argsp[]) {
        SpringApplication.run(lottoapi.class, argsp);
    }//end of mains
}

package renatius.import_export_accounting.Controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class HomeController {
    @RequestMapping("/home")
    public String home(){
        return "ChooseYourWayPage";
    }
}

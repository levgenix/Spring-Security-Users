package web.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.SessionAttribute;
import web.model.LoginException;

import javax.servlet.http.HttpSession;
import java.util.Map;

@Controller
@RequestMapping("/")
public class IndexController {

	@GetMapping("")
	public String welcomePage(Model model,
							  @SessionAttribute(required = false, name = "Authentication-Exception") LoginException authenticationException,
							  @RequestHeader Map<String, String> headers, HttpSession session) {
		// TODO: при ?logout выводить прощальное сообщение

		if (authenticationException != null) {
			try {
				model.addAttribute("authenticationException", authenticationException);
				session.removeAttribute("Authentication-Exception");
			} catch (Exception e) {
				e.printStackTrace();
			}
		} else {
			model.addAttribute("authenticationException", new LoginException(null));
		}

		return "index";
	}
}
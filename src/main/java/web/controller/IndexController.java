package web.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.SessionAttribute;
import web.model.LoginException;

import javax.servlet.http.HttpSession;

@Controller
@RequestMapping("/")
public class IndexController {

	@GetMapping("")
	public String welcomePage(Model model, HttpSession session,
							  @SessionAttribute(required = false, name = "Authentication-Exception") LoginException authenticationException,
							  @SessionAttribute(required = false, name = "Authentication-Name") String authenticationName) {
		// Восстанавливаем неверно введенные данные
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

		// Выводим прощальное сообщение
		if (authenticationName != null) {
			try {
				model.addAttribute("authenticationName", authenticationName);
				session.removeAttribute("Authentication-Name");
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		return "index";
	}
}
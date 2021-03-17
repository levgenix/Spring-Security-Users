package web.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import web.model.User;

import java.util.Map;

@Controller
@RequestMapping("/")
public class IndexController {

	@GetMapping("")
	public String welcomePage(@ModelAttribute("user") User user,
							  @SessionAttribute(required = false, name = "Authentication-Exception") String message,
							  @RequestHeader Map<String, String> headers) {
		// TODO: при ?logout выводить прощальное сообщение

		// todo: писать серилизованный объект? для неудачного логина и успешного логоута (для прощания)
		System.out.println(message != null ? message : "NO Authentication-Exception");

		// todo: прибить
//		headers.forEach((key, value) -> {
//			System.out.println(String.format("Header '%s' = %s", key, value));
//		});
		// TODO: как отловить неудачный логин "/?error" @PostMapping
		return "index";
	}

	// TODO
//	@GetMapping("login")
//	public String loginPage() {
//		return "login";
//	}
}
package com.ruijie.cloud.macc.dataplatform.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/")
public class RuokController {

	@GetMapping("/ruok")
	public String ruok() {
		return "I'm OK";
	}
}

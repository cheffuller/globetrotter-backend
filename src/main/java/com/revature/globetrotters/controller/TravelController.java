package com.revature.globetrotters.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/accounts")
@CrossOrigin(origins = "http://localhost:5173/")
public class TravelController {
    
}

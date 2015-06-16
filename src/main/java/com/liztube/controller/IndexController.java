package com.liztube.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.liztube.business.AuthBusiness;
import com.liztube.utils.facade.UserConnectedProfile;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

/**
 * Default controller for the SPA application : all pages redirect to the single page index.jsp
 */
@Controller
@RequestMapping("/")
public class IndexController {
    @Autowired
    public AuthBusiness authBusiness;

    @RequestMapping(method = {RequestMethod.GET, RequestMethod.HEAD}, value = {"*", "*/*"})
    public ModelAndView SingleApplicationPage() {
        ModelAndView modelAndView = new ModelAndView("index");

        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.writerWithDefaultPrettyPrinter();

        UserConnectedProfile userConnected = authBusiness.getUserConnectedProfile(false);

        String userConnectedStringified = null;
        try {
            userConnectedStringified = objectMapper.writeValueAsString(userConnected);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        modelAndView.addObject("userConnected", userConnectedStringified);

        return modelAndView;
    }
}

package com.example.autobuild.demo.controller;

import com.example.autobuild.demo.common.Response;
import com.example.autobuild.demo.service.AutoBuildService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@RequestMapping(value = "/v1")
@Controller
public class AutoBuildController {

    @Autowired
    AutoBuildService autoBuildService;

    @ResponseBody
    @RequestMapping(value = "/createVms", method = RequestMethod.GET)
    public Response createVms(
            @RequestParam(name = "thread", required = false, defaultValue = "5") int thread,
            @RequestParam(name = "vms", required = false, defaultValue = "10") int vms,
            @RequestParam(name = "start", required = false, defaultValue = "1") int start) {
        return autoBuildService.createVms(thread, vms, start);
    }

    @ResponseBody
    @RequestMapping(value = "/stopCreatingVms", method = RequestMethod.GET)
    public Response stopCreatingVms() {
        return autoBuildService.stopCreatingVms();
    }

    @ResponseBody
    @RequestMapping(value = "/cleanVms", method = RequestMethod.GET)
    public Response cleanVms(
            @RequestParam(name = "total", required = false, defaultValue = "5") int total,
            @RequestParam(name = "thread", required = false, defaultValue = "5") int thread) {
        return autoBuildService.cleanVms(total, thread);
    }
}

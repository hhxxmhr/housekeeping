package com.mhr.housekeeping.controller;

import com.mhr.housekeeping.service.FundService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class FundController {
    @Autowired
    FundService fundService;
}

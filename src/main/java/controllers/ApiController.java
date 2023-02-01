package controllers;

import com.google.gson.Gson;
import data.Storage;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ApiController {
    @GetMapping("/last_scan")
    public String lastScan() {
        return String.valueOf(Storage.lastScanTimestamp);
    }

    @GetMapping("/recipes")
    public String recipes() {
        Gson gson = new Gson();
        return gson.toJson(Storage.recipes);
    }

    @GetMapping("/components")
    public String components() {
        Gson gson = new Gson();
        return gson.toJson(Storage.components);
    }

    @GetMapping("/prices")
    public String prices() {
        Gson gson = new Gson();
        return gson.toJson(Storage.prices);
    }

    @GetMapping("/total")
    public String total() {
        return String.valueOf(Storage.currentProgressTotal);
    }

    @GetMapping("/current")
    public String current() {
        return String.valueOf(Storage.currentProgress);
    }
}
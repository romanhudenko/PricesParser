package controllers;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import utils.PricesParser;

@Component
public class CMDRunner implements CommandLineRunner {
    private final PricesParser parser;

    public CMDRunner(PricesParser parser) {
        this.parser = parser;
    }

    @Override
    public void run(String... args) throws Exception {
        parser.endlessLoad();
    }
}

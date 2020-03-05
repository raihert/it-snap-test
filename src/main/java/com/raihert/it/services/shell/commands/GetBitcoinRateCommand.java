package com.raihert.it.services.shell.commands;

import com.raihert.it.exceptions.ResultValidateException;
import com.raihert.it.services.bpi.BpiService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import org.springframework.shell.standard.ShellOption;

@ShellComponent
public class GetBitcoinRateCommand {

    @Autowired
    private BpiService service;

    @ShellMethod("enter a currency code (USD, EUR, GBP, etc.)")
    public Object rates(@ShellOption String currency) throws ResultValidateException {
        if (!currency.matches("^[a-zA-Z]{3}$")) {
            return "Try again";
        }

        return service.handle(currency.toUpperCase());
    }
}

package com.example.zeapp.controllers;

import com.example.zeapp.ZeAppApplication;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/sys")
public class SysController {
    @Value("${CONTEXT_RELOAD_KEY}")
    private String crk;

    /**
     * перезапустит контекст
     */
    @GetMapping("/reload")
    public void reloadContext(@RequestParam String crk){
        if (crk.equals(this.crk)) reloadContext();
    }

    /**
     * перезапустит контекст, необходим для перезапуска при изменении  ssl сертификата
     * потом перейду на платный сертификат уберу все это возможно
     */
    private void reloadContext(){
        Thread thread = new Thread(new Runnable() {
        @Override
        public void run() {
            try {
                Thread.sleep(10000); //чтобы положительный ответ успел уйти
            } catch (InterruptedException e) {e.printStackTrace();}
            ZeAppApplication.context.close();
            ZeAppApplication.context = SpringApplication.run(ZeAppApplication.class, ZeAppApplication.savedArgs);
        }
    });
        thread.start();
    }
}
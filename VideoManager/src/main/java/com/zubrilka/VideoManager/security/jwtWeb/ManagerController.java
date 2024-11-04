package com.zubrilka.VideoManager.security.jwtWeb;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * менеджер контроллер для проверки куки аутентификации
 * TODO удалить контроллер после настройки куки аутентификации
 */
@Controller
public class ManagerController {

    @GetMapping("manager")
    public String getManagerPage() {
        return "manager";
    }
}

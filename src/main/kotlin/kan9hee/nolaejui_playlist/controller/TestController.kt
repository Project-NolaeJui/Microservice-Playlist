package kan9hee.nolaejui_playlist.controller

import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/playlist")
class TestController() {

    @GetMapping("/welcome")
    fun welcome():String{
        return "Welcome playlist server"
    }
}
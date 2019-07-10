package com.hand.sharesinfo.globalexceptionhand;

import com.hand.sharesinfo.exceptions.OnlyOnceXiaZai;
import com.hand.sharesinfo.model.Message;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * Author: OuGen
 * Discription:
 * Date: 11:35 2019/7/9
 */

@RestControllerAdvice
public class GlobalExceptionHander {
    @ExceptionHandler(value = Exception.class)
    public Message handler(Exception e){
        if (e instanceof OnlyOnceXiaZai){
            OnlyOnceXiaZai onlyOnceXiaZai = (OnlyOnceXiaZai) e;
            return new Message(onlyOnceXiaZai.getMsg(),onlyOnceXiaZai.getCode());
        }else {
            return new Message(e.getMessage(),-2);
        }
    }
}

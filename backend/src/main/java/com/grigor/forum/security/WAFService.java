package com.grigor.forum.security;

import com.grigor.forum.exceptions.BadRequestException;
import org.springframework.stereotype.Service;
import org.springframework.validation.BindingResult;

@Service
public class WAFService {
    public void checkRequest(BindingResult request) {
        if (!request.hasErrors())
            return;

        //Greska ce biti uhvacena GlobalExceptionHandler i on sluzi kao SIEM komponenta
        throw new BadRequestException("Potential risk of hacker attack!");
    }
}

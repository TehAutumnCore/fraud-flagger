package com.gary.fraudflagger.controller;

import com.gary.fraudflagger.model.FraudFlag;
import com.gary.fraudflagger.repository.FraudFlagRepository;
import com.gary.fraudflagger.service.AuditLogService;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Optional;

@Controller
@RequestMapping("/flags")
public class FlagController {

    private final FraudFlagRepository fraudFlagRepository;
    private final AuditLogService auditLogService;

    public FlagController(FraudFlagRepository fraudFlagRepository,
                          AuditLogService auditLogService) {
        this.fraudFlagRepository = fraudFlagRepository;
        this.auditLogService = auditLogService;
    }

    @PostMapping("/{id}/approve")
    public String approveFlag(@PathVariable Long id, Authentication auth) { //Authentication auth is injected by Spring Security automatically
        Optional<FraudFlag> optional = fraudFlagRepository.findById(id);
        if (optional.isPresent()) {
            FraudFlag flag = optional.get();
            flag.setStatus("Approved");
            fraudFlagRepository.save(flag);
            auditLogService.log(auth.getName(), "APPROVED", id); //auth.getName() gives you the logged in user's username.
        }
        return "redirect:/dashboard";
    }

    @PostMapping("/{id}/dismiss")
    public String dismissFlag(@PathVariable Long id, Authentication auth) {
        Optional<FraudFlag> optional = fraudFlagRepository.findById(id);
        if (optional.isPresent()) {
            FraudFlag flag = optional.get();
            flag.setStatus("DISMISSED");
            fraudFlagRepository.save(flag);
            auditLogService.log(auth.getName(), "DISMISSED", id);
        }
        return "redirect:/dashboard";
    }
}

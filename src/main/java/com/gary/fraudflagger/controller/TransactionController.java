package com.gary.fraudflagger.controller;


import com.gary.fraudflagger.model.Transaction;
import com.gary.fraudflagger.repository.AccountRepository;
import com.gary.fraudflagger.service.TransactionService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.math.BigDecimal;

@Controller
@RequestMapping("/transactions") //all routes in this controller start with /transactions
public class TransactionController {

    private final TransactionService transactionService;
    private final AccountRepository accountRepository;

    public TransactionController(TransactionService transactionService,
                                 AccountRepository accountRepository) {
        this.transactionService = transactionService;
        this.accountRepository = accountRepository;
    }

    //GET /transactions/subbmit — show the empty form
    @GetMapping("/submit") //visitings the URL shows a blank form
    public String showForm(Model model) {
        model.addAttribute("transaction", new Transaction());
        model.addAttribute("accounts", accountRepository.findAll());
        return "submit-transaction";
    }

    //POST /transaction/submit — process the submitted form
    @PostMapping("/submit") // submitting the form sends data here
    public String submitTransaction(@RequestParam Long accountId, //@RequestParam - pulls individual values out of the submitted form
                                    @RequestParam double amount,
                                    @RequestParam String merchant) {
        var account = accountRepository.findById(accountId)
                .orElseThrow(() -> new RuntimeException("Account not found"));

        Transaction transaction = new Transaction();
        transaction.setAccount(account);
        transaction.setAmount(BigDecimal.valueOf(amount));
        transaction.setMerchant(merchant);
        transaction.setTimestamp(LocalDateTime.now());

        transactionService.submitTransaction(transaction);

        return "redirect:/dashboard"; //after saving, sends the user to the dashboard instead of re-rendering a page
    }

}

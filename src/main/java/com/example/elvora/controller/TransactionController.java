package com.example.elvora.controller;

import com.example.elvora.dto.TransactionDto;
import com.example.elvora.model.Category;
import com.example.elvora.model.Login;
import com.example.elvora.model.Transaction;
import com.example.elvora.model.User;
import com.example.elvora.repository.LoginRepository;
import com.example.elvora.repository.UserRepository;
import com.example.elvora.service.CategoryService;
import com.example.elvora.service.TransactionService;

import jakarta.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/expense")
public class TransactionController {

    private final TransactionService transactionService;

    private final UserRepository userRepository;

    private final CategoryService categoryService;

    private final LoginRepository loginRepository;

    public TransactionController(
            TransactionService transactionService,
            UserRepository userRepository,
            CategoryService categoryService,
            LoginRepository loginRepository) {

        this.transactionService = transactionService;
        this.userRepository = userRepository;
        this.categoryService = categoryService;
        this.loginRepository = loginRepository;
    }


    // =========================
    // AVAILABLE CATEGORIES
    // =========================

    private List<Category> getAvailableCategories(
            String email) {

        List<Category> allCategories =
                categoryService.getAllCategory();

        List<Category> categories =
                new ArrayList<>();

        for (Category category : allCategories) {

            Login creator =
                    loginRepository
                            .findById(
                                    category.getCreated_by()
                            )
                            .orElse(null);

            if (creator != null &&
                    creator.getUsertype()
                            .equalsIgnoreCase("admin")) {

                categories.add(category);
            }

            else if (category.getCreated_by()
                    .equals(email)) {

                categories.add(category);
            }
        }

        return categories;
    }


    // =========================
    // ADD EXPENSE PAGE
    // =========================

    @GetMapping("/add")
    public String addExpensePage(
            HttpSession session,
            Model model) {

        String email =
                (String) session.getAttribute("email");

        model.addAttribute(
                "categories",
                getAvailableCategories(email)
        );

        model.addAttribute(
                "transactionDto",
                new TransactionDto()
        );

        return "Transaction/AddExpense";
    }


    // =========================
    // ADD EXPENSE
    // =========================

    @PostMapping("/add")
    public String addExpense(
            HttpSession session,
            @ModelAttribute TransactionDto transactionDto) {

        String email =
                (String) session.getAttribute("email");

        User user =
                userRepository.findByEmail(email);

        Category category =
                categoryService.getCategoryById(
                        transactionDto.getCategory_id()
                );

        if (user == null ||
                category == null) {

            return "redirect:/expense/add";
        }

        Transaction transaction =
                new Transaction();

        transaction.setUser(user);

        transaction.setCategory(category);

        transaction.setAmount(
                transactionDto.getAmount()
        );

        transaction.setDate(
                transactionDto.getDate()
        );

        transaction.setDescription(
                transactionDto.getDescription()
        );

        transaction.setType("EXPENSE");

        transactionService.addExpense(transaction);

        return "redirect:/expense/showall";
    }


    // =========================
    // SHOW USER EXPENSES
    // =========================

    @GetMapping("/showall")
    public String showAllExpenses(
            HttpSession session,
            Model model) {

        String email =
                (String) session.getAttribute("email");

        List<Transaction> expenses =
                transactionService
                        .getUserExpenses(email);

        double totalExpense = expenses.stream().mapToDouble(Transaction::getAmount).sum();
        double maxExpense = expenses.stream().mapToDouble(Transaction::getAmount).max().orElse(0.0);

        model.addAttribute("expenses", expenses);
        model.addAttribute("totalExpense", totalExpense);
        model.addAttribute("maxExpense", maxExpense);

        return "Transaction/ShowExpense";
    }


    // =========================
    // EDIT EXPENSE PAGE
    // =========================

    @GetMapping("/edit")
    public String editExpensePage(
            @RequestParam("id") int id,
            HttpSession session,
            Model model) {

        String email =
                (String) session.getAttribute("email");

        Transaction transaction =
                transactionService
                        .getTransactionById(id);

        if (transaction == null) {

            return "redirect:/expense/showall";
        }

        if (!transaction
                .getUser()
                .getEmail()
                .equals(email)) {

            return "redirect:/AuthError";
        }

        if (!transaction
                .getType()
                .equalsIgnoreCase("EXPENSE")) {

            return "redirect:/expense/showall";
        }

        model.addAttribute(
                "transaction",
                transaction
        );

        model.addAttribute(
                "categories",
                getAvailableCategories(email)
        );

        return "Transaction/EditExpense";
    }


    // =========================
    // UPDATE EXPENSE
    // =========================

    @PostMapping("/edit")
    public String updateExpense(
            HttpSession session,
            @ModelAttribute TransactionDto transactionDto) {

        String email =
                (String) session.getAttribute("email");

        Transaction transaction =
                transactionService
                        .getTransactionById(
                                transactionDto
                                        .getTransaction_id()
                        );

        if (transaction == null) {

            return "redirect:/expense/showall";
        }

        if (!transaction
                .getUser()
                .getEmail()
                .equals(email)) {

            return "redirect:/AuthError";
        }

        if (!transaction
                .getType()
                .equalsIgnoreCase("EXPENSE")) {

            return "redirect:/expense/showall";
        }

        Category category =
                categoryService.getCategoryById(
                        transactionDto.getCategory_id()
                );

        if (category == null) {

            return "redirect:/expense/showall";
        }

        transaction.setCategory(category);

        transaction.setAmount(
                transactionDto.getAmount()
        );

        transaction.setDate(
                transactionDto.getDate()
        );

        transaction.setDescription(
                transactionDto.getDescription()
        );

        transactionService
                .updateExpense(transaction);

        return "redirect:/expense/showall";
    }


    // =========================
    // DELETE EXPENSE PAGE
    // =========================

    @GetMapping("/delete")
    public String deleteExpensePage(
            @RequestParam("id") int id,
            HttpSession session,
            Model model) {

        String email =
                (String) session.getAttribute("email");

        Transaction transaction =
                transactionService
                        .getTransactionById(id);

        if (transaction == null) {

            return "redirect:/expense/showall";
        }

        if (!transaction
                .getUser()
                .getEmail()
                .equals(email)) {

            return "redirect:/AuthError";
        }

        if (!transaction
                .getType()
                .equalsIgnoreCase("EXPENSE")) {

            return "redirect:/expense/showall";
        }

        model.addAttribute(
                "transaction",
                transaction
        );

        return "Transaction/DeleteExpense";
    }


    // =========================
    // DELETE EXPENSE
    // =========================

    @PostMapping("/delete")
    public String deleteExpense(
            @RequestParam("transaction_id") int id,
            HttpSession session) {

        String email =
                (String) session.getAttribute("email");

        Transaction transaction =
                transactionService
                        .getTransactionById(id);

        if (transaction == null) {

            return "redirect:/expense/showall";
        }

        if (!transaction
                .getUser()
                .getEmail()
                .equals(email)) {

            return "redirect:/AuthError";
        }

        if (!transaction
                .getType()
                .equalsIgnoreCase("EXPENSE")) {

            return "redirect:/expense/showall";
        }

        transactionService.deleteExpense(id);

        return "redirect:/expense/showall";
    }
}
package nbcc.resto.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import nbcc.common.service.LoginService;
import nbcc.resto.dto.Table;
import nbcc.resto.service.TableService;
import nbcc.resto.viewmodels.TableListViewModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import static nbcc.common.validation.ModelErrorConverter.addErrorsToBindingResults;


@Controller
@PreAuthorize("isAuthenticated()")
@RequestMapping("/table")
public class TableController {

    private final LoginService loginService;

    private final TableService tableService;

    private final Logger logger = LoggerFactory.getLogger(TableController.class);

    public TableController(LoginService loginService, TableService tableService) {
        this.loginService = loginService;
        this.tableService = tableService;
    }

    @GetMapping
    public String getAll(Model model) {

        var result = tableService.getAll();

        if (result.isError()) {
            model.addAttribute("message", "Error retrieving tables");
            return "error/errorPage";
        }

        TableListViewModel viewModel = new TableListViewModel(result.getValue(), loginService.isLoggedIn());
        model.addAttribute("viewModel", viewModel);
        return "table/list";
    }

    @GetMapping("/create")
    public String create(Model model) {
        model.addAttribute("table", new Table());
        return "table/create";
    }

    @PostMapping("/create")
    public String create(@Valid @ModelAttribute("table") Table table, BindingResult br) {

        if (br.hasErrors()) {
            return "table/create";
        }

        var result = tableService.create(table);

        if (result.isError()) {
            return "error/errorPage";
        }

        if (result.isInvalid()) {
            addErrorsToBindingResults(br, result);
            return "table/create";
        }

        return "redirect:/table";
    }

    @GetMapping("/edit/{id}")
    public String edit(@PathVariable Long id, Model model) {

        var result = tableService.get(id);

        if (result.isError()) {
            return "error/errorPage";
        }

        if (result.isEmpty()) {
            model.addAttribute("message", "The table you are trying to edit was not found");
            return "error/errorPage";
        }

        loadModel(result.getValue(), model);
        return "table/edit";
    }

    @PostMapping("/edit/{id}")
    public String edit(@PathVariable Long id,
                       @Valid @ModelAttribute("table") Table table,
                       BindingResult br,
                       Model model) {

        if (br.hasErrors()) {
            return "table/edit";
        }

        table.setId(id);

        var result = tableService.update(table);

        if (result.isError()) {
            return "error/errorPage";
        }

        if (result.isInvalid()) {
            addErrorsToBindingResults(br, result);
            model.addAttribute("table", table);
            return "table/edit";
        }

        return "redirect:/table";
    }

    @GetMapping("/delete/{id}")
    public String delete(@PathVariable long id, Model model) {

        var result = tableService.get(id);
        if (result.isError()) {
            return "error/errorPage";
        }

        if (result.isEmpty()) {
            model.addAttribute("message", "The table you are trying to delete was not found");
            return "error/errorPage";
        }

        loadModel(result.getValue(), model);

        return "table/delete";
    }

    @PostMapping("/delete/{id}")
    public String delete(@PathVariable Long id) {

        var result = tableService.delete(id);
        if (result.isError()) {
            return "error/errorPage";
        }

        return "redirect:/table";
    }

    private void loadModel(Table table, Model model) {
        model.addAttribute("table", table);
    }

    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public String exceptionHandler(Model model, Exception ex, HttpServletRequest request){
        logger.error("Unexpected Exception on uri {}: on method {} ", request.getRequestURI() , request.getMethod(), ex);
        model.addAttribute("message", "Unexpected Error Occurred");
        return "error/errorPage";
    }
}

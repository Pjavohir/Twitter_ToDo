package uz.pj.simple_trello.controller.project;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import uz.pj.simple_trello.controller.base.AbstractController;
import uz.pj.simple_trello.dto.project.ProjectCreateDto;
import uz.pj.simple_trello.dto.project.ProjectUpdateDto;
import uz.pj.simple_trello.services.project.ProjectService;

@Controller
@RequestMapping("/project/*")
public class ProjectController extends AbstractController<ProjectService> {

    @Autowired
    public ProjectController(ProjectService service) {
        super(service);
    }


    @PreAuthorize("hasAnyRole('SUPER_USER', 'ADMIN')")
    @RequestMapping(value = "create/{id}", method = RequestMethod.GET)
    public String createPage(@PathVariable(name = "id") Long id) {
        return "project/create";
    }

    @PreAuthorize("hasAnyRole('SUPER_USER', 'ADMIN')")
    @RequestMapping(value = "create/{id}", method = RequestMethod.POST)
    public String create(@ModelAttribute ProjectCreateDto dto, @PathVariable(name = "id") Long id) {
        dto.setOrganizationId(id);
        service.create(dto);
        return "redirect:/organization/list";
    }

    @RequestMapping(value = "detail/{id}", method = RequestMethod.GET)
    public String detail(Model model, @PathVariable(name = "id") Long id) {
        model.addAttribute("projects", service.get(id));
        return "project/detail";
    }

    @RequestMapping(value = "list/{id}", method = RequestMethod.GET)
    public String listPage(Model model, @PathVariable(name = "id") Long id) {
        model.addAttribute("projects", service.getAllProjects(id));
        model.addAttribute("id", id);
        return "project/list";
    }

    @RequestMapping(value = "update/{id}", method = RequestMethod.GET)
    public String updatePage(@PathVariable(name = "id") Long id, Model model) {
        model.addAttribute("dto", service.get(id));
        return "project/update";
    }

    @RequestMapping(value = "update/{id}", method = RequestMethod.PATCH)
    public String update(@ModelAttribute ProjectUpdateDto dto) {
        service.update(dto);
        return "project/update";
    }


    @PreAuthorize("hasAnyRole('SUPER_USER', 'ADMIN')")
    @RequestMapping(value = "delete/{id}", method = RequestMethod.GET)
    public String deletePage(@PathVariable(name = "id") Long id) {
        service.delete(id);
        return "project/delete";
    }


    @PreAuthorize("hasAnyRole('SUPER_USER', 'ADMIN')")
    @RequestMapping(value = "delete/{id}", method = RequestMethod.POST)
    public String delete(@PathVariable(name = "id") Long id) {
        service.delete(id);
        return "project/delete";
    }

//-------------------------------------------------------//


    @RequestMapping(value = "project", method = RequestMethod.GET)
    public String projectPage() {
        return "index/project";
    }

    @PreAuthorize("hasAnyRole('SUPER_USER', 'ADMIN')")
    @RequestMapping(value = "index", method = RequestMethod.GET)
    public String indexPage() {
        return "index/index";
    }


}

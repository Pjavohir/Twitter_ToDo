package uz.pj.simple_trello.services.project;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import uz.pj.simple_trello.criteria.GenericCriteria;
import uz.pj.simple_trello.dto.project.ProjectCreateDto;
import uz.pj.simple_trello.dto.project.ProjectDto;
import uz.pj.simple_trello.dto.project.ProjectUpdateDto;
import uz.pj.simple_trello.entity.base.AuditAwareImpl;
import uz.pj.simple_trello.entity.project.Project;
import uz.pj.simple_trello.mapper.project.ProjectMapper;
import uz.pj.simple_trello.reposiroty.project.ProjectRepository;
import uz.pj.simple_trello.services.base.AbstractService;
import uz.pj.simple_trello.utils.BaseUtils;
import uz.pj.simple_trello.utils.validators.project.ProjectValidator;

import java.util.List;
import java.util.Optional;


@Service
public class ProjectServiceImpl extends AbstractService<
        ProjectRepository,
        ProjectMapper,
        ProjectValidator> implements ProjectService {

    private  final AuditAwareImpl auditAware;

    @Autowired
    protected ProjectServiceImpl(
            ProjectRepository repository,
            ProjectMapper mapper,
            ProjectValidator validator,
            BaseUtils baseUtils, AuditAwareImpl auditAware) {
        super(repository, mapper, validator, baseUtils);
        this.auditAware = auditAware;
    }

    @Override
    public Long create(ProjectCreateDto createDto) {
        Project project = mapper.fromCreateDto(createDto);
        Long id = new AuditAwareImpl().getCurrentAuditor().get();
        project.setOrganizationId(createDto.getOrganizationId());
        project.setLastModifiedBy(id);
        return repository.save(project).getId();
    }

    @Override
    public void delete(Long id) {
        Optional<Project> project = repository.findById(id);
        if (project.isEmpty()) {
            throw new RuntimeException("Pr not found");
        }
        repository.delete(project.get());
    }

    @Override
    public void update(ProjectUpdateDto updateDto) {
        Optional<Project> project = repository.findById(updateDto.getId());
        if (project.isEmpty()) {
            throw new RuntimeException("Project not found");
        }
        Project project1 = mapper.fromUpdateDto(updateDto);
        repository.save(project1);
    }

    @Override
    public List<ProjectDto> getAll(GenericCriteria criteria) {
        List<Project> projects = repository.findAll();
        return null;
    }

    public List<ProjectDto> getAllProjects(Long id) {
        List<Project> projectsByOrganizationIdd = repository.findPro(id);
        return mapper.toDto(projectsByOrganizationIdd);
    }


    @Override
    public ProjectDto get(Long id) {

        return null;
    }

    @Override
    public Long totalCount(GenericCriteria criteria) {
        return null;
    }


}
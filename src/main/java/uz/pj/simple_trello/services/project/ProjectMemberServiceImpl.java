package uz.pj.simple_trello.services.project;

import org.springframework.stereotype.Service;
import uz.pj.simple_trello.criteria.GenericCriteria;
import uz.pj.simple_trello.dto.project.ProjectMemberCreateDto;
import uz.pj.simple_trello.dto.project.ProjectMemberDto;
import uz.pj.simple_trello.dto.project.ProjectMemberUpdateDto;
import uz.pj.simple_trello.entity.project.ProjectMember;
import uz.pj.simple_trello.mapper.project.ProjectMemberMapper;
import uz.pj.simple_trello.reposiroty.project.ProjectMemberRepository;
import uz.pj.simple_trello.services.base.AbstractService;
import uz.pj.simple_trello.utils.BaseUtils;
import uz.pj.simple_trello.utils.validators.project.ProjectMemberValidator;

import java.util.List;
import java.util.Optional;

@Service
public class ProjectMemberServiceImpl extends AbstractService<
        ProjectMemberRepository,
        ProjectMemberMapper,
        ProjectMemberValidator> implements ProjectMemberService {


    protected ProjectMemberServiceImpl(ProjectMemberRepository repository, ProjectMemberMapper mapper, ProjectMemberValidator validator, BaseUtils baseUtils) {
        super(repository, mapper, validator, baseUtils);
    }

    @Override
    public Long create(ProjectMemberCreateDto createDto) {
        ProjectMember project=mapper.fromCreateDto(createDto);
        return repository.save(project).getId();
    }

    @Override
    public void delete(Long id) {
        Optional<ProjectMember> projectMember = repository.findById(id);
        if (projectMember.isEmpty()) {
            throw new RuntimeException("User not found");
        }
        repository.deleteById(id);
    }

    @Override
    public void update(ProjectMemberUpdateDto updateDto) {


    }

    @Override
    public List<ProjectMemberDto> getAll(GenericCriteria criteria) {
        return mapper.toDto(repository.findAll());
    }

    @Override
    public ProjectMemberDto get(Long id) {
        ProjectMember projectMember = repository.findById(id).orElseThrow(() -> {
            throw new RuntimeException("Not Found");
        });
        ProjectMemberDto dto = mapper.toDto(projectMember);
        return dto;

    }

    @Override
    public Long totalCount(GenericCriteria criteria) {
        return null;
    }
}

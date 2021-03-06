package uz.pj.simple_trello.services.organization;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import uz.pj.simple_trello.criteria.GenericCriteria;
import uz.pj.simple_trello.dto.organization.OrganizationCreateDto;
import uz.pj.simple_trello.dto.organization.OrganizationDto;
import uz.pj.simple_trello.dto.organization.OrganizationUpdateDto;
import uz.pj.simple_trello.entity.base.AuditAwareImpl;
import uz.pj.simple_trello.entity.organization.Organization;
import uz.pj.simple_trello.mapper.organization.OrganizationMapper;
import uz.pj.simple_trello.reposiroty.organization.OrganizationRepository;
import uz.pj.simple_trello.services.base.AbstractService;
import uz.pj.simple_trello.services.file.FileStorageService;
import uz.pj.simple_trello.utils.BaseUtils;
import uz.pj.simple_trello.utils.validators.organization.OrganizationValidator;

import java.util.List;

@Service
public class OrganizationServiceImpl extends
        AbstractService<OrganizationRepository, OrganizationMapper, OrganizationValidator>
        implements OrganizationService {

    private final FileStorageService fileStorageService;

    @Autowired
    protected OrganizationServiceImpl(OrganizationRepository repository,
                                      OrganizationMapper mapper,
                                      OrganizationValidator validator,
                                      BaseUtils baseUtils,
                                      FileStorageService fileStorageService) {
        super(repository, mapper, validator, baseUtils);
        this.fileStorageService = fileStorageService;
    }

    @Override
    public Long create(OrganizationCreateDto createDto) {
        MultipartFile file = createDto.getLogo();
        String logoPath = fileStorageService.store(file);
        Organization organization = mapper.fromCreateDto(createDto);
        organization.setLogo(logoPath);
        organization.setOwner(new AuditAwareImpl().getCurrentAuditor().get());
        organization.setCreatedBy(new AuditAwareImpl().getCurrentAuditor().get());
        organization.setLastModifiedBy(new AuditAwareImpl().getCurrentAuditor().get());
        repository.save(organization);
        return organization.getId();
    }

    @Override
    public void delete(Long id) {
        repository.deleteById(id);
    }

    @Override
    public void update(OrganizationUpdateDto updateDto) {
        Organization organization = mapper.fromUpdateDto(updateDto);
        repository.save(organization);
    }

    @Override
    public List<OrganizationDto> getAll(GenericCriteria criteria) {
        return mapper.toDto(repository.findAll());
    }

    @Override
    public OrganizationDto get(Long id) {
        Organization organization = repository.findById(id).orElseThrow(() -> {
            throw new RuntimeException("Not Found");
        });
        OrganizationDto dto = mapper.toDto(organization);
        return dto;
    }

    @Override
    public Long totalCount(GenericCriteria criteria) {
        return null;
    }
}

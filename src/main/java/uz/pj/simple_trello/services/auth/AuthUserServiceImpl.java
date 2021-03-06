package uz.pj.simple_trello.services.auth;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import uz.pj.simple_trello.config.security.UserDetailsService;
import uz.pj.simple_trello.criteria.GenericCriteria;
import uz.pj.simple_trello.dto.auth.AuthUserCreateDto;
import uz.pj.simple_trello.dto.auth.AuthUserDto;
import uz.pj.simple_trello.dto.auth.AuthUserUpdateDto;
import uz.pj.simple_trello.dto.auth.LoginDto;
import uz.pj.simple_trello.entity.auth.AuthUser;
import uz.pj.simple_trello.entity.base.AuditAwareImpl;
import uz.pj.simple_trello.mapper.auth.AuthUserMapper;
import uz.pj.simple_trello.reposiroty.auth.AuthRoleRepository;
import uz.pj.simple_trello.reposiroty.auth.AuthUserRepository;
import uz.pj.simple_trello.services.base.AbstractService;
import uz.pj.simple_trello.utils.BaseUtils;
import uz.pj.simple_trello.utils.validators.auth.AuthUserValidator;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class AuthUserServiceImpl extends
        AbstractService<AuthUserRepository, AuthUserMapper, AuthUserValidator>
        implements AuthUserService {


    private final PasswordEncoder encoder;
    private final AuthRoleRepository authRoleRepository;
    private final AuditAwareImpl auditAware;

    @Autowired
    UserDetailsService userDetailsService;

    protected AuthUserServiceImpl(AuthUserRepository repository,
                                  AuthUserMapper mapper,
                                  AuthUserValidator validator,
                                  BaseUtils baseUtils, PasswordEncoder encoder, AuthRoleRepository authRoleRepository, AuditAwareImpl auditAware) {
        super(repository, mapper, validator, baseUtils);
        this.encoder = encoder;
        this.authRoleRepository = authRoleRepository;
        this.auditAware = auditAware;
    }

    @Override
    public Long create(AuthUserCreateDto createDto) {
        createDto.setRole(authRoleRepository.getAuthRoleById(createDto.getRoleId()).get());
        AuthUser user = mapper.fromCreateDto(createDto);
        user.setPassword(encoder.encode(createDto.getPassword()));
        user.setOrganizationId(auditAware.getCredentials().getOrganizationId());
        user.setCreatedBy(new AuditAwareImpl().getCurrentAuditor().get());
        user.setCode(UUID.randomUUID());
        if (createDto.getPassword().equals(createDto.getConPassword()))
            repository.save(user);
        else {
            System.out.println("Error");
        }
        return user.getId();
    }

    @Override
    public void delete(Long id) {
    }

    @Override
    public void update(AuthUserUpdateDto updateDto) {
    }


    @Override
    public List<AuthUserDto> getAll(GenericCriteria criteria) {
        return null;
    }

    @Override
    public AuthUserDto get(Long id) {
        return null;
    }

    @Override
    public Long totalCount(GenericCriteria criteria) {
        return null;
    }


    @Override
    public void login(LoginDto dto) {
        Optional<AuthUser> authUser = repository.findAuthUserByUsername(dto.getUsername());
        if (authUser.isEmpty()) {
            throw new RuntimeException("User not found");
        }
        if (!encoder.matches(dto.getPassword(), authUser.get().getPassword())) {
            throw new UsernameNotFoundException("User not found");
        }
        userDetailsService.loadUserByUsername(dto.getUsername());
    }
}
